package tcss450.uw.edu.chatapp;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ContactPageFragment extends Fragment {

    private OnContactPageFragmentInteractionListener mListener;

    public ContactPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_page, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        String nickname = getArguments().getString("nickname");
        String email = getArguments().getString("email");
        String firstName = getArguments().getString("firstName");
        String lastName = getArguments().getString("lastName");

        TextView nicknameView = getActivity().findViewById(R.id.contact_page_text_nickname);
        TextView emailView = getActivity().findViewById(R.id.contact_page_text_email);
        TextView firstNameView = getActivity().findViewById(R.id.contact_page_text_fname);
        TextView lastNameView = getActivity().findViewById(R.id.contact_page_text_lname);

        nicknameView.setText(nickname);
        emailView.setText(email);
        firstNameView.setText(firstName);
        lastNameView.setText(lastName);

        FloatingActionButton newChatFab = getActivity().findViewById(R.id.contact_page_newchat_fab);
        newChatFab.setOnClickListener(v -> onFabPressed(email));
    }

    public void onFabPressed(String email) {
        if (mListener != null) {
            mListener.onContactPageFragmentInteraction(email);
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

    public interface OnContactPageFragmentInteractionListener {
        void onContactPageFragmentInteraction(String email);
    }
}
