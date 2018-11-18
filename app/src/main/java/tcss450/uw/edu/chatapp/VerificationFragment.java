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
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import tcss450.uw.edu.chatapp.model.Credentials;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class VerificationFragment extends Fragment implements View.OnClickListener {

    private OnVerificationFragmentInteractionListener mListener;
    private Credentials mCredentials;
    private TextView confirmation;

    public VerificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_verification, container, false);

        Button resend_button = v.findViewById(R.id.verification_resend_button);
        resend_button.setOnClickListener(this);
        Button login_button = v.findViewById(R.id.verification_login_button);
        login_button.setOnClickListener(this);
        confirmation = v.findViewById(R.id.verification_resend_confirmation);

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnVerificationFragmentInteractionListener) {
            mListener = (OnVerificationFragmentInteractionListener) context;
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
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            mCredentials = (Credentials) getArguments().get("credentials");
        }
    }

    @Override
    public void onClick(View v) {
        if (mListener != null) {
            switch (v.getId()) {
                case R.id.verification_resend_button:

                    Uri uri = new Uri.Builder()
                            .scheme("https")
                            .appendPath(getString(R.string.ep_base_url))
                            .appendPath(getString(R.string.ep_verification))
                            .build();
                    JSONObject msg = mCredentials.asJSONObject();
                    new SendPostAsyncTask.Builder(uri.toString(), msg)
                            .onPreExecute(this::handleResendOnPre)
                            .onPostExecute(this::handleResendOnPost)
                            .onCancelled(this::handleErrorsInTask)
                            .build().execute();

//                    mListener.onResendClicked();
                    break;
                case R.id.verification_login_button:
                    mListener.onLoginClicked();
                    break;
                default:
                    Log.wtf("", "Didn't expect to see me...");
            }
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
    private void handleResendOnPre() {
    }

    /**
     * Handle onPostExecute of the AsynceTask. The result from our webservice is
     * a JSON formatted String. Parse it for success or failure.
     * @param result the JSON formatted String response from the web service
     */
    private void handleResendOnPost(String result) {
        try {
            Log.d("JSON result",result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                confirmation.setText("Re-sent!");
            } else {
                confirmation.setText("Resend failed!");

//                //Login was unsuccessful. Don’t switch fragments and inform the user
//                ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.fragRegister_email_editText))
//                        .setError("Registration Unsuccessful");
            }
        } catch (JSONException e) {

            confirmation.setText("Resend failed!");

//            //It appears that the web service didn’t return a JSON formatted String
//            //or it didn’t have what we expected in it.
//            Log.e("JSON_PARSE_ERROR", result
//                    + System.lineSeparator()
//                    + e.getMessage());
//            ((TextView) Objects.requireNonNull(getView()).findViewById(R.id.fragRegister_email_editText))
//                    .setError("Registration Unsuccessful");
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
    public interface OnVerificationFragmentInteractionListener {
        // TODO: Update argument type and name
//        void onResendClicked();
        void onLoginClicked();
    }
}
