package tcss450.uw.edu.chatapp.contacts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;

public class ContactPageFragment extends Fragment {

    private OnContactPageFragmentInteractionListener mListener;
    private FloatingActionButton mFab;
    private String mEmail;
    private String mCurrEmail;
    private String mNickname;
    private String mFirstname;
    private String mLastName;
    private Contacts mContacts;

    public ContactPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact_page, container, false);
        return v;
    }
    @Override
    public void onStart() {
        super.onStart();
        mNickname = getArguments().getString("nickname");
        mEmail = getArguments().getString("email");
        mCurrEmail = getArguments().getString("currEmail");
        mFirstname = getArguments().getString("firstName");
        mLastName = getArguments().getString("lastName");

        TextView nicknameView = getActivity().findViewById(R.id.contact_page_text_nickname);
        TextView emailView = getActivity().findViewById(R.id.contact_page_text_email);
        TextView firstNameView = getActivity().findViewById(R.id.contact_page_text_fname);
        TextView lastNameView = getActivity().findViewById(R.id.contact_page_text_lname);

        nicknameView.setText(mNickname);
        emailView.setText(mEmail);
        firstNameView.setText(mFirstname);
        lastNameView.setText(mLastName);

        FloatingActionButton newChatFab = getActivity().findViewById(R.id.contact_page_newchat_fab);
        newChatFab.setOnClickListener(v -> onFabPressed(mEmail));
    }

    public void onFabPressed(String email) {
        if (mListener != null) {
            startNewChat();
//            mListener.onContactPageFragmentInteraction(email);
        }
    }
    private void startNewChat(){
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_messaging_base))
                .appendPath(getString(R.string.ep_messaging_getAll))
                .build();
        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("chatName", mEmail);
            messageJson.put("email1", mCurrEmail);
            messageJson.put("email2", mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                .onPostExecute(this::newChatPostExecute)
                .onCancelled(error -> Log.e("SEND_TAG", error))
                .build().execute();
    }
    private void newChatPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            mListener.onContactPageFragmentInteraction(mNickname+ " (" +mFirstname+" "+mLastName+")");
        } catch (JSONException e) {
            Log.e("JSON PARSE", e.toString());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactPageFragmentInteractionListener) {
            mListener = (OnContactPageFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContactPageFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setContacts(Contacts contacts){
        mContacts = contacts;
    }

    public interface OnContactPageFragmentInteractionListener {
        void onContactPageFragmentInteraction(String name);
    }
}
