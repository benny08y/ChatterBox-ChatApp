package tcss450.uw.edu.chatapp.chats;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcss450.uw.edu.chatapp.HomeActivity;
import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.utils.MyFirebaseMessagingService;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;


/**
 * A simple {@link Fragment} subclass.
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

        mSendUrl = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_send))
                .build()
                .toString();
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

    private void getMessagesPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            int chatid = root.getInt("chatid");
            JSONArray data = root.getJSONArray("messages");
//            for (int i =data.length()-1; i >= 0; i--){
            for (int i =0; i< data.length(); i++){
                JSONObject jsonMsg = data.getJSONObject(i);
                String sender = jsonMsg.getString("email");
                String msg = jsonMsg.getString("message");
                String nickname = jsonMsg.getString("username");
                Message curMsg = new Message.Builder(sender, nickname, chatid).addMessage(msg).build();
                mGetAllMessagesList.add(curMsg);
//                    mMessageOutputTextView.append(sender + ":" + msg);
//                    mMessageOutputTextView.append(System.lineSeparator());
//                    mMessageOutputTextView.append(System.lineSeparator());
            }
            Log.d("MesgAdapter", "MessageFragment_after_parse" + mGetAllMessagesList.get(0));
            mMessageAdapter = new MessageListAdapter(getContext(), mGetAllMessagesList, mEmail);
            LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
//            linearLayout.setReverseLayout(true);
            linearLayout.setStackFromEnd(true);
            mMessageRecycler.setLayoutManager(linearLayout);
            mMessageRecycler.setAdapter(mMessageAdapter);
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
                mMessageAdapter.addNewMessage(curMsg);
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
            Log.d("SENT_MSG", "start onRecieve");
            if(intent.hasExtra("DATA")) {
                String data = intent.getStringExtra("DATA");
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(data);
                    if(jObj.has("message") && jObj.has("sender")) {
                        String sender = jObj.getString("sender");
                        String nickname = jObj.getString("username");
                        int chatid = jObj.getInt("chatid");
                        String msg = jObj.getString("message");
//                        Message curMsg = new Message.Builder(sender, nickname, chatid).addMessage(msg).build();
//                        mMessageAdapter.addNewMessage(curMsg);
//                        mMessageOutputTextView.append(sender + ":" + msg);
//                        mMessageOutputTextView.append(System.lineSeparator());
//                        mMessageOutputTextView.append(System.lineSeparator());
                        Log.d("SENT_MSG", sender + " " + msg);
                    }
                } catch (JSONException e) {
                    Log.e("JSON PARSE", e.toString());
                }
            }
        }
    }
}
