package tcss450.uw.edu.chatapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import tcss450.uw.edu.chatapp.model.Credentials;
import tcss450.uw.edu.chatapp.utils.WaitFragment;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRegisterFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class RegisterFragment extends Fragment {

    private OnRegisterFragmentInteractionListener mListener;
    private EditText myEmail;
    private EditText mNickname;
    private EditText myFisrtName;
    private EditText myLastName;
    private EditText myPassword;
    private EditText myRepeatPassword;
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
        myEmail = (EditText)v.findViewById(R.id.fragRegister_email_editText);
        mNickname = (EditText)v.findViewById(R.id.fragRegister_nickname_editText);
        myFisrtName = (EditText)v.findViewById(R.id.fragRegister_firstName_editText);
        myLastName = (EditText)v.findViewById(R.id.fragRegister_lastName_editText3);
        myPassword = (EditText)v.findViewById(R.id.fragRegister_password_editText);
        myRepeatPassword = getActivity().findViewById(R.id.fragRegister_repeatPass_editText2);
        Button b = (Button)v.findViewById(R.id.fragRegister_registerButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidUsername()) {
                    myEmail.setError("You must enter a valid email address");
                }
                if (!isSixCharPassword()) {
                    myPassword.setError("You must enter a 6 character password");
                }
                if (!isMatchingPassword()){
                    myPassword.setError("Passwords must match");
                }
                if (mNickname.getText().toString().isEmpty()){
                    mNickname.setError("Please enter a username");
                }
                if (myFisrtName.getText().toString().isEmpty()){
                    myFisrtName.setError("Please enter you first name");
                }
                if (myLastName.getText().toString().isEmpty()){
                    myLastName.setError("Please enter you last name");
                }

                if (isValidUsername() && isMatchingPassword() && isSixCharPassword() && !isEmptyField()){
                    mCredentials = new Credentials
                            .Builder(myEmail.getText().toString(),myPassword.getText().toString())
                            .build();
                    mListener.onRegisterAttempt(mCredentials);
                }
            }
        });
    }

    //Helper Methods for register validation
    private boolean isValidUsername(){
        return !myEmail.getText().toString().isEmpty()&& myEmail.getText().toString().contains("@");
    }
    private boolean isEmptyField(){
        return mNickname.getText().toString().isEmpty() &&
                myFisrtName.getText().toString().isEmpty() &&
                myLastName.getText().toString().isEmpty();
    }
    private boolean isSixCharPassword(){
        return myPassword.getText().toString().length() >= 6;
    }
    private boolean isMatchingPassword(){
        return myPassword.getText().toString().equals(myRepeatPassword.getText().toString());
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
