package tcss450.uw.edu.chatapp.contacts;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.model.Contacts;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class AddContactsFragment extends Fragment {
    private FloatingActionButton mFab;
    private String mEmail;
    private String mCurrEmail;
    private String mNickname;
    private String mFirstname;
    private String mLastName;
    private Contacts mContacts;
    private String successMessage = "You have sent a contact request";
    private String failedMessage = "The contact request has not been sent";
    private OnFragmentInteractionListener mListener;

    public AddContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contacts, container, false);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputManager = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        // check if no view has focus:
        View currentFocusedView = activity.getCurrentFocus();
        if (currentFocusedView != null) {
            inputManager.hideSoftInputFromWindow(currentFocusedView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


    public void onStart() {
        super.onStart();

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);


        mNickname = getArguments().getString("nickname");
        mEmail = getArguments().getString("email");
        mCurrEmail = getArguments().getString("currEmail");
        mFirstname = getArguments().getString("firstName");
        mLastName = getArguments().getString("lastName");

        TextView nicknameView = getActivity().findViewById(R.id.search_contact_page_text_nickname);
        TextView emailView = getActivity().findViewById(R.id.search_contact_page_text_email);
        TextView firstNameView = getActivity().findViewById(R.id.search_contact_page_text_fname);
        TextView lastNameView = getActivity().findViewById(R.id.search_contact_page_text_lname);

        nicknameView.setText(mNickname);
        emailView.setText(mEmail);
        firstNameView.setText(mFirstname);
        lastNameView.setText(mLastName);

        FloatingActionButton addContactFab = getActivity().findViewById(R.id.search_contact_page_newchat_fab);
        addContactFab.setImageResource(R.drawable.ic_person_add_black_24dp);
        addContactFab.setOnClickListener(v -> onFabPressed(mCurrEmail, mEmail));
    }

    public void onFabPressed(String userEmail, String receiverEmail) {
        if (mListener != null) {
            //send contact request using curremail and memail
            //have popup saying that the request was sent
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_contacts))
                    .appendPath(getString(R.string.ep_contacts_send_request))
                    .build();
            JSONObject messageJson = new JSONObject();
            Bundle bundle = getArguments();


            try {
                messageJson.put("senderEmail", mCurrEmail);
                messageJson.put("receiverEmail", mEmail);
                Log.e("IN_JSON", "post body email");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("IN_JSON", "didnt put email");
            }

            new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                    .onPostExecute(this::handleContactsGetOnPostExecute)
                    .onCancelled(error -> Log.e("SEND_TAG", error))
                    .build().execute();

            Toast.makeText(getActivity(), successMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleContactsGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Log.e("Successfully sent", "Success");
                //Toast.makeText(getActivity(), successMessage, Toast.LENGTH_SHORT).show();

            } else {
                //Toast.makeText(getActivity(), failedMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
