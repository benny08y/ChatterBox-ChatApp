package tcss450.uw.edu.chatapp;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import tcss450.uw.edu.chatapp.contacts.ContactPageFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class ZipCodeFragment extends Fragment {

    private EditText mZipCodeEditText;
    private Button mButton;
    private ZipCodeFragment.OnZipCodeFragmentInteractionListener mListener;

    public ZipCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_zip_code, container, false);

        mZipCodeEditText = (EditText) v.findViewById(R.id.zipCodeFragmentEditText);
        mButton = (Button) v.findViewById(R.id.zipCodeFragmentSearchButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String zipCodeString = mZipCodeEditText.getText().toString();
                mListener.onSearchButtonClicked(zipCodeString);
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ZipCodeFragment.OnZipCodeFragmentInteractionListener) {
            mListener = (ZipCodeFragment.OnZipCodeFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnZipCodeFragmentInteractionListener");
        }
    }

    public interface OnZipCodeFragmentInteractionListener {
        void onSearchButtonClicked(String zipCodeString);
    }

}
