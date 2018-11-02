package tcss450.uw.edu.chatapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import tcss450.uw.edu.chatapp.model.Credentials;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnRegisterFragmentInteractionListener mListener;
    private String myEmail;
    private String mNickname;
    private String myFirstName;
    private String myLastName;
    private String myPassword;
    private String myRepeatPassword;
    private Credentials mCredentials;

    public RegisterFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_register, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View v, Bundle savedInstanceState){
        Button b = (Button)v.findViewById(R.id.fragRegister_registerButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptRegistration(v);
            }
        });
    }

    private void attemptRegistration(View v) {
        final EditText email = getActivity().findViewById(R.id.fragRegister_email_editText);
        final EditText nickname = getActivity().findViewById(R.id.fragRegister_nickname_editText);
        final EditText firstName = getActivity().findViewById(R.id.fragRegister_firstName_editText);
        final EditText lastName = getActivity().findViewById(R.id.fragRegister_lastName_editText3);
        final EditText password = getActivity().findViewById(R.id.fragRegister_password_editText);
        final EditText repeatPassword = getActivity().findViewById(R.id.fragRegister_repeatPass_editText2);

        myEmail = email.getText().toString();
        mNickname = nickname.getText().toString();
        myFirstName = firstName.getText().toString();
        myLastName = lastName.getText().toString();
        myPassword = password.getText().toString();
        myRepeatPassword = repeatPassword.getText().toString();

        if (!isValidEmail()) {
            email.setError("You must enter a valid email address");
        }
        if (!isSixCharPassword()) {
            password.setError("You must enter a 6 character password");
        }
        if (!isMatchingPassword()){
            password.setError("Passwords must match");
        }
        if (mNickname.isEmpty()){
            nickname.setError("Please enter a nickname");
        }
        if (myFirstName.isEmpty()){
            firstName.setError("Please enter your first name");
        }
        if (myLastName.isEmpty()){
            lastName.setError("Please enter your last name");
        }

        if (isValidEmail() && isMatchingPassword() && isSixCharPassword() && !isEmptyField()){

            Credentials.Builder builder = new Credentials.Builder(myEmail.toString(), myPassword.toString());
            builder.addFirstName(myFirstName.toString());
            builder.addLastName(myLastName.toString());
            builder.addNickname(mNickname.toString());
            Credentials credentials = builder.build();


            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_register))
                    .build();
            JSONObject msg = credentials.asJSONObject();
            mCredentials = credentials;
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleRegistrationOnPre)
                    .onPostExecute(this::handleRegistrationOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();

            //mListener.onRegisterAttempt(mCredentials);
        }
    }

    /**
     * Handle errors that may occur during the AsyncTask.
     * @param result the error message provide from the AsyncTask
     */
    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    /**
     * Handle the setup of the UI before the HTTP call to the webservice.
     */
    private void handleRegistrationOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleRegistrationOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            mListener.onWaitFragmentInteractionHide();
            if (success) {
                //Login was successful. Inform the Activity so it can do its thing.
                mListener.onRegisterAttempt(mCredentials);
            } else {
                //Login was unsuccessful. Don’t switch fragments and inform the user
                ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.fragRegister_email_editText))
                        .setError("Registration Unsuccessful");
            }
        } catch (JSONException e) {
            //It appears that the web service didn’t return a JSON formatted String
            //or it didn’t have what we expected in it.
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.fragRegister_email_editText))
                    .setError("Registration Unsuccessful");
        }
    }

    //Helper Methods for register validation
    private boolean isValidEmail(){
        return !myEmail.isEmpty()&& myEmail.contains("@");
    }
    private boolean isEmptyField(){
        return mNickname.isEmpty() &&
                myFirstName.isEmpty() &&
                myLastName.isEmpty();
    }
    private boolean isSixCharPassword(){
        return myPassword.length() >= 6;
    }
    private boolean isMatchingPassword(){
        return myPassword.equals(myRepeatPassword);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnRegisterFragmentInteractionListener) {
            mListener = (OnRegisterFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLoginFragmentInteractionListener");
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
    public interface OnRegisterFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        void onRegisterAttempt(Credentials credentials);
    }
}
