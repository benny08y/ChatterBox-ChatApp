package tcss450.uw.edu.chatapp.chats;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import tcss450.uw.edu.chatapp.HomeActivity;
import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.utils.MyFirebaseMessagingService;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;


/**
 * Benjamin Yuen
 * Message Fragment that displays the messages sent by you and messages recieved from people in chat.
 */
public class MessageFragment extends Fragment implements WaitFragment.OnFragmentInteractionListener {
    private static final String TAG = "CHAT_FRAG";

    private TextView mMessageOutputTextView;
    private EditText mMessageInputEditText;
    private String mEmail;
    private String mSendUrl;
    private FirebaseMessageReciever mFirebaseMessageReciever;
    private String mNickname;
    private int mChatId;

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private List<Message> mGetAllMessagesList;

    public MessageFragment() {    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootLayout = inflater.inflate(R.layout.fragment_message, container, false);
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_email))) {
            mEmail = prefs.getString(getString(R.string.keys_prefs_email), "");
        } else {
            throw new IllegalStateException("No EMAIL in prefs!");
        }
//        mMessageOutputTextView = rootLayout.findViewById(R.id.text_chat_message_display);
        mMessageInputEditText = rootLayout.findViewById(R.id.edit_chat_message_input);
        mGetAllMessagesList = new ArrayList<>();
        rootLayout.findViewById(R.id.button_chat_send).setOnClickListener(this::handleSendClick);

        mMessageRecycler = (RecyclerView) rootLayout.findViewById(R.id.reyclerview_message_list);
//        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        return rootLayout;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_email))) {
            mEmail = prefs.getString(getString(R.string.keys_prefs_email), "");
        } else {
            throw new IllegalStateException("No EMAIL in prefs!");
        }

        mChatId = getArguments().getInt(HomeActivity.MESSAGE_CHATID);
        mNickname = getArguments().getString(HomeActivity.MESSAGE_NICKNAME);
        TextView msg_contactname = getActivity().findViewById(R.id.message_contactname);
        msg_contactname.setText(mNickname);

        //Building url to send messages
        mSendUrl = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_send))
                .build()
                .toString();

        //Building url to get all messages and display past messages from a recent chat
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_getAll))
                .build();
        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("chatId", mChatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                .onPostExecute(this::getMessagesPostExecute)
                .onCancelled(error -> Log.e("SEND_TAG", error))
                .build().execute();

    }
    //Post execute for getting all messages.
    private void getMessagesPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            int chatid = root.getInt("chatid");
            JSONArray data = root.getJSONArray("messages");
            for (int i =data.length()-1; i >= 0; i--){
                JSONObject jsonMsg = data.getJSONObject(i);
                String sender = jsonMsg.getString("email");
                String msg = jsonMsg.getString("message");
                String nickname = jsonMsg.getString("username");
                // Getting timestamp
                String timestamp = jsonMsg.getString("timestamp");
                //Formatting timestamp into readable format for users
                int iend = timestamp.indexOf('.');
                if (iend != -1){
                    timestamp = timestamp.substring(0, iend);
                }
                int dend = timestamp.indexOf(' ');
                String date = timestamp.substring(0, dend);
                String time = timestamp.substring(dend+1, timestamp.length() - 3);
                Log.d("TIMEDATE", time + " " +date);
                int hour = Integer.parseInt(time.substring(0, 2));
                Log.d("TIMEDATE", "Hour:" +hour +" " + time.substring(3, time.length()));
                String am_pm = "AM";
                if (hour > 12 || hour == 0){
                    if (hour % 12 == 0){
                        hour = 12;
                    } else {
                        hour = hour % 12;
                        am_pm = "PM";
                    }
                }
                String new_time = String.valueOf(hour) + ":"+time.substring(3, time.length()) +am_pm;
                Log.d("TIMEDATE", new_time);

                Message curMsg = new Message.Builder(sender, nickname, chatid)
                        .addMessage(msg)
                        .addTimeStamp(new_time +"  "+date)
                        .build();
                mGetAllMessagesList.add(curMsg);
//                    mMessageOutputTextView.append(sender + ":" + msg);
//                    mMessageOutputTextView.append(System.lineSeparator());
//                    mMessageOutputTextView.append(System.lineSeparator());
            }
            mMessageAdapter = new MessageListAdapter(getContext(), mGetAllMessagesList, mEmail);
            LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
            mMessageRecycler.setLayoutManager(linearLayout);
            mMessageRecycler.setAdapter(mMessageAdapter);

            //Set recycler view to bottom so that users can see most recent message
            mMessageRecycler.scrollToPosition(mMessageAdapter.getItemCount()-1);
        } catch (JSONException e) {
            Log.e("JSON PARSE", e.toString());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirebaseMessageReciever == null) {
            mFirebaseMessageReciever = new FirebaseMessageReciever();
        }
        IntentFilter iFilter = new IntentFilter(MyFirebaseMessagingService.RECEIVED_NEW_MESSAGE);
        getActivity().registerReceiver(mFirebaseMessageReciever, iFilter);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mFirebaseMessageReciever != null){
            getActivity().unregisterReceiver(mFirebaseMessageReciever);
        }
    }
    //Post excute for sending messages
    private void handleSendClick(final View theButton) {
        String msg = mMessageInputEditText.getText().toString();
        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("email", mEmail);
            messageJson.put("message", msg);
            Log.d("SENT_MSG", String.valueOf(mChatId));
            messageJson.put("chatId", mChatId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("SENT_MSG", msg);
        new SendPostAsyncTask.Builder(mSendUrl, messageJson)
                .onPostExecute(this::endOfSendMsgTask)
                .onCancelled(error -> Log.e(TAG, error))
                .build().execute();
    }
    private void endOfSendMsgTask(final String result) {
        try {
            //This is the result from the web service
            Log.d("SENT_MSG", "trying");
            JSONObject res = new JSONObject(result);
            if(res.has("success") ) {
                //The web service got our message. Time to clear out the input EditText
                mMessageInputEditText.setText("");
                //its up to you to decide if you want to send the message to the output here
                //or wait for the message to come back from the web service.
                String message = res.getString("msg");
                Message curMsg = new Message.Builder(mEmail, "", 0).addMessage(message).build();

            }
        } catch (JSONException e) {
            Log.d("SENT_MSG", "Failed to recieve");
            e.printStackTrace();
        }
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_home_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {    }

    /**
     * A BroadcastReceiver setup to listen for messages sent from
     MyFirebaseMessagingService
     * that Android allows to run all the time.
     */
    private class FirebaseMessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.hasExtra("DATA")) {
                String data = intent.getStringExtra("DATA");
                Log.d("SENT_MSG", "start onRecieve" + data);

                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(data);
                    if(jObj.has("message") && jObj.has("sender")) {
                        String sender = jObj.getString("sender");
                        String nickname = jObj.getString("username");
                        int chatid = jObj.getInt("chatid");
                        String msg = jObj.getString("message");
                        String timestamp = jObj.getString("timestamp");
                        int iend = timestamp.indexOf('.');
                        if (iend != -1){
                            timestamp = timestamp.substring(0, iend);
                        }
                        int dend = timestamp.indexOf(' ');
                        String date = timestamp.substring(0, dend);
                        String time = timestamp.substring(dend+1, timestamp.length() - 3);
                        Log.d("TIMEDATE", time + " " +date);
                        int hour = Integer.parseInt(time.substring(0, 2));
                        Log.d("TIMEDATE", "Hour:" +hour +", " + time.substring(3, time.length()));
                        String am_pm = "AM";
                        if (hour > 12 || hour == 0){
                            if (hour % 12 == 0){
                                hour = 12;
                            } else {
                                hour = hour % 12;
                                am_pm = "PM";
                            }
                        }
                        String new_time = String.valueOf(hour) + ":"+time.substring(3, time.length()) +am_pm;
                        Log.d("TIMEDATE", new_time);

                        Message curMsg = new Message.Builder(sender, nickname, chatid)
                                .addMessage(msg)
                                .addTimeStamp(new_time +" "+ date)
                                .build();

                        //Add sent message to the recycler view
                        mMessageAdapter.addNewMessage(curMsg);
                        //Set recycler view to bottom so that users can see most recent message
                        mMessageRecycler.scrollToPosition(mMessageAdapter.getItemCount()-1);
                        Log.d("SENT_MSG", nickname + " " + msg);
                    }
                } catch (JSONException e) {
                    Log.e("JSON PARSE", e.toString());
                }
            }
        }
    }
}
