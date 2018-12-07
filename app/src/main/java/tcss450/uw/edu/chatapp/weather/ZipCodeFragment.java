package tcss450.uw.edu.chatapp.weather;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import tcss450.uw.edu.chatapp.R;


/**
 * Fragment prompting the user for a zip code input to display the weather results.
 */
public class ZipCodeFragment extends Fragment {

    private EditText mZipCodeEditText;
    private Button mButton;
    private ZipCodeFragment.OnZipCodeFragmentInteractionListener mListener;

    public ZipCodeFragment() {
        // Required empty public constructor
    }

    /**
     * Initializes fragment elements and sets button listener to open weather display fragment.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_zip_code, container, false);

        mZipCodeEditText = (EditText) v.findViewById(R.id.zipCodeFragmentEditText);
        mButton = (Button) v.findViewById(R.id.zipCodeFragmentSearchButton);
        mButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Clicking button will send the zip code input to a new fragment displaying weather
             * for that zip code.
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                String zipCodeString = mZipCodeEditText.getText().toString();
                mListener.onSearchButtonClicked(zipCodeString);
            }
        });

        return v;
    }

    /**
     * Ensures activities implement fragment listener.
     *
     * @param context
     */
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

    /**
     * Interface for activity interaction.
     */
    public interface OnZipCodeFragmentInteractionListener {
        void onSearchButtonClicked(String zipCodeString);
    }

}
