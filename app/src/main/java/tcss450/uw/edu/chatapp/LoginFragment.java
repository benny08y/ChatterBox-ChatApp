package tcss450.uw.edu.chatapp;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import tcss450.uw.edu.chatapp.model.Credentials;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private OnLoginFragmentInteractionListener mListener;
    private Credentials mCredentials;
    private String mFirebaseToken;
    private Button mResendButton;

    public LoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);
        EditText emailEdit = v.findViewById(R.id.fragLogin_email_editTxt);
        EditText passwordEdit = v.findViewById(R.id.fragLogin_password_editTxt);

        Button b = (Button) v.findViewById(R.id.login_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFirebaseToken(emailEdit.getText().toString(), passwordEdit.getText().toString());
                attemptLogin();
            }
        });
        b = v.findViewById(R.id.register_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterClicked();
            }
        });

        mResendButton = v.findViewById(R.id.login_resend_button);
        mResendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Credentials.Builder builder = new Credentials.Builder(emailEdit.getText().toString(), passwordEdit.getText().toString());
                Credentials credentials = builder.build();
                mListener.onResendClicked(credentials);
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //retrieve the stored credentials from SharedPrefs
        if (prefs.contains(getString(R.string.keys_prefs_email)) &&
                prefs.contains(getString(R.string.keys_prefs_password))) {
            final String email = prefs.getString(getString(R.string.keys_prefs_email), "");
            final String password = prefs.getString(getString(R.string.keys_prefs_password), "");
            //Load the two login EditTexts with the credentials found in SharedPrefs
            getFirebaseToken(email, password);
        }
    }

    private void getFirebaseToken(final String email, final String password) {
        mListener.onWaitFragmentInteractionShow();
        //add this app on this device to listen for the topic all
        FirebaseMessaging.getInstance().subscribeToTopic("all");
        //the call to getInstanceId happens asynchronously. task is an onCompleteListener
        //similar to a promise in JS.
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("FCM: ", "getInstanceId failed", task.getException());
                        mListener.onWaitFragmentInteractionHide();
                        return;
                    }
                    // Get new Instance ID token
                    mFirebaseToken = task.getResult().getToken();
                    Log.d("FCM: ", mFirebaseToken);
                    //the helper method that initiates login service
                    doLogin(email, password);
                });
        //no code here. wait for the Task to complete.
    }

    private void doLogin(String email, String password) {
        EditText emailEdit = getActivity().findViewById(R.id.fragLogin_email_editTxt);
        emailEdit.setText(email);
        EditText passwordEdit = getActivity().findViewById(R.id.fragLogin_password_editTxt);
        passwordEdit.setText(password);
        attemptLogin();
    }

    public void loginButton(View v) {
        Credentials credentials = new Credentials.Builder(
                "email",
                "password")
                .build();

        mListener.onLoginAttempt(credentials);
    }

    private void attemptLogin() {
        EditText email = getActivity().findViewById(R.id.fragLogin_email_editTxt);
        EditText password = getActivity().findViewById(R.id.fragLogin_password_editTxt);
        boolean hasError = false;
        if (email.getText().length() == 0) {
            hasError = true;
            email.setError("Field must not be empty.");
        } else if (email.getText().toString().chars().filter(ch -> ch == '@').count() != 1) {
            hasError = true;
            email.setError("Field must contain a valid email address.");
        }
        if (password.getText().length() == 0) {
            hasError = true;
            password.setError("Field must not be empty.");
        }
        if (!hasError) {
            Credentials credentials = new Credentials.Builder(
                    email.getText().toString(),
                    password.getText().toString())
                    .build();
            //build the web service URL
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_login))
                    .appendPath(getString(R.string.ep_with_token))
                    .build();
            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();
            try {
                msg.put("token", mFirebaseToken);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mCredentials = credentials;
            //instantiate and execute the AsyncTask.
            //Feel free to add a handler for onPreExecution so that a progress bar
            //is displayed or maybe disable buttons.
            new SendPostAsyncTask.Builder(uri.toString(), msg)
                    .onPreExecute(this::handleLoginOnPre)
                    .onPostExecute(this::handleLoginOnPost)
                    .onCancelled(this::handleErrorsInTask)
                    .build().execute();
        }
    }

    private void saveCredentials(final Credentials credentials) {
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_email), credentials.getEmail()).apply();
        prefs.edit().putString(getString(R.string.keys_prefs_password), credentials.getPassword()).apply();
    }

    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    private void handleLoginOnPre() {
    }

    private void handleLoginOnPost(String result) {
        try {
            Log.d("JSON result", result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                //Login was successful. Inform the Activity so it can do its thing.
                saveCredentials(mCredentials);
                mListener.onLoginAttempt(mCredentials);
            } else {
                //Login was unsuccessful. Don’t switch fragments and inform the user
                ((TextView) getView().findViewById(R.id.fragLogin_email_editTxt))
                        .setError("Login Unsuccessful");
                mResendButton.setVisibility(View.VISIBLE);
                mListener.onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            ((TextView) getView().findViewById(R.id.fragLogin_email_editTxt))
                    .setError("Login Unsuccessful");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLoginFragmentInteractionListener) {
            mListener = (OnLoginFragmentInteractionListener) context;
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

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            mListener.onLoginAttempt(mCredentials);
        }
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
    public interface OnLoginFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLoginAttempt(Credentials credentials);

        void onRegisterClicked();

        void onResendClicked(Credentials credentials);
    }
}
