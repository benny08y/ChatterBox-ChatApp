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

import tcss450.uw.edu.chatapp.model.Credentials;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnLoginFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class LoginFragment extends Fragment  implements View.OnClickListener {

    private OnLoginFragmentInteractionListener mListener;
    private Credentials mCredentials;

    public LoginFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        Button b = (Button) v.findViewById(R.id.login_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin(v);
            }
        });
        b = v.findViewById(R.id.register_button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRegisterClicked();
            }
        });

        return v;
    }

    public void loginButton(View v){
        Credentials credentials = new Credentials.Builder(
                "email",
                "password")
                .build();

        mListener.onLoginAttempt(credentials);
    }

    private void attemptLogin(final View theButton) {
        EditText email = getActivity().findViewById(R.id.fragLogin_user_editTxt);
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
                    .build();
            //build the JSONObject
            JSONObject msg = credentials.asJSONObject();
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

    private void handleErrorsInTask(String result) {
        Log.e("ASYNCT_TASK_ERROR", result);
    }

    private void handleLoginOnPre() {
        mListener.onWaitFragmentInteractionShow();
    }

    private void handleLoginOnPost(String result) {
        mListener.onWaitFragmentInteractionHide();
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            mListener.onWaitFragmentInteractionHide();
            if (success) {
                //Login was successful. Inform the Activity so it can do its thing.
                mListener.onLoginAttempt(mCredentials);
            } else {
                //Login was unsuccessful. Don’t switch fragments and inform the user
                ((TextView) getView().findViewById(R.id.fragLogin_user_editTxt))
                        .setError("Login Unsuccessful");
            }
        } catch (JSONException e) {
            Log.e("JSON_PARSE_ERROR", result
                    + System.lineSeparator()
                    + e.getMessage());
            mListener.onWaitFragmentInteractionHide();
            ((TextView) getView().findViewById(R.id.fragLogin_user_editTxt))
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
        if(mListener != null) {
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
    public interface OnLoginFragmentInteractionListener extends WaitFragment.OnFragmentInteractionListener{
        // TODO: Update argument type and name
        void onLoginAttempt(Credentials credentials);
        void onRegisterClicked();
    }
}
