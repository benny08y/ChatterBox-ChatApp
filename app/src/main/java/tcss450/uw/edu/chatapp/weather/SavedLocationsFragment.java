package tcss450.uw.edu.chatapp.weather;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import tcss450.uw.edu.chatapp.R;


/**
 * Fragment that automatically populates a list of saved locations from shared preferences to
 * display the weather in each shared location.
 */
public class SavedLocationsFragment extends Fragment {

    private Button savedLocation1, savedLocation2, savedLocation3, savedLocation4, savedLocation5,
            savedLocation6, savedLocation7, savedLocation8, savedLocation9;
    private Button delete1, delete2, delete3, delete4, delete5, delete6, delete7, delete8, delete9;
    private SavedLocationsFragment.OnSavedLocationsFragmentInteractionListener mListener;
    private TextView noSavedLocations;

    public SavedLocationsFragment() {
        // Required empty public constructor
    }

    /**
     * Creates a list of all saved city strings from shared preferences.
     * Initializes all fragment buttons (location buttons and delete buttons) and populates buttons
     * with city string for however many saved locations.
     *
     * Listeners are attached to each set button.
     *
     * Also sets element visibility to "GONE" if buttons are not loaded with a location.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_saved_locations, container, false);

        ArrayList<String> locationsList = new ArrayList<>();

        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        for (int i = 1; i <= 9; i++) {
            if (prefs.getString("keys_prefs_location" + Integer.toString(i), "") != null) {
                locationsList.add(prefs.getString("keys_prefs_location" + Integer.toString(i), ""));
            }
        }

        noSavedLocations = v.findViewById(R.id.noSavedLocationsTextView);

        delete1 = v.findViewById(R.id.delete1);
        delete2 = v.findViewById(R.id.delete2);
        delete3 = v.findViewById(R.id.delete3);
        delete4 = v.findViewById(R.id.delete4);
        delete5 = v.findViewById(R.id.delete5);
        delete6 = v.findViewById(R.id.delete6);
        delete7 = v.findViewById(R.id.delete7);
        delete8 = v.findViewById(R.id.delete8);
        delete9 = v.findViewById(R.id.delete9);

        savedLocation1 = v.findViewById(R.id.savedLocation1);
        savedLocation1.setVisibility(View.GONE);
        if (locationsList.size() > 0) {
            savedLocation1.setText(locationsList.get(0));
            savedLocation1.setVisibility(View.VISIBLE);
            savedLocation1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSavedLocationClicked(savedLocation1.getText().toString());
                }
            });
            delete1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedLocation1.setText("");
                    mListener.onDeleteLocationClicked("1");
                }
            });
        }

        savedLocation2 = v.findViewById(R.id.savedLocation2);
        savedLocation2.setVisibility(View.GONE);
        if (locationsList.size() > 1) {
            savedLocation2.setText(locationsList.get(1));
            savedLocation2.setVisibility(View.VISIBLE);
            savedLocation2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSavedLocationClicked(savedLocation2.getText().toString());
                }
            });
            delete2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedLocation2.setText("");
                    mListener.onDeleteLocationClicked("2");
                }
            });
        }

        savedLocation3 = v.findViewById(R.id.savedLocation3);
        savedLocation3.setVisibility(View.GONE);
        if (locationsList.size() > 2) {
            savedLocation3.setText(locationsList.get(2));
            savedLocation3.setVisibility(View.VISIBLE);
            savedLocation3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSavedLocationClicked(savedLocation3.getText().toString());
                }
            });
            delete3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedLocation3.setText("");
                    mListener.onDeleteLocationClicked("3");
                }
            });
        }

        savedLocation4 = v.findViewById(R.id.savedLocation4);
        savedLocation4.setVisibility(View.GONE);
        if (locationsList.size() > 3) {
            savedLocation4.setText(locationsList.get(3));
            savedLocation4.setVisibility(View.VISIBLE);
            savedLocation4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSavedLocationClicked(savedLocation4.getText().toString());
                }
            });
            delete4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedLocation4.setText("");
                    mListener.onDeleteLocationClicked("4");
                }
            });
        }

        savedLocation5 = v.findViewById(R.id.savedLocation5);
        savedLocation5.setVisibility(View.GONE);
        if (locationsList.size() > 4) {
            savedLocation5.setText(locationsList.get(4));
            savedLocation5.setVisibility(View.VISIBLE);
            savedLocation5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSavedLocationClicked(savedLocation5.getText().toString());
                }
            });
            delete5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedLocation5.setText("");
                    mListener.onDeleteLocationClicked("5");
                }
            });
        }

        savedLocation6 = v.findViewById(R.id.savedLocation6);
        savedLocation6.setVisibility(View.GONE);
        if (locationsList.size() > 5) {
            savedLocation6.setText(locationsList.get(5));
            savedLocation6.setVisibility(View.VISIBLE);
            savedLocation6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSavedLocationClicked(savedLocation6.getText().toString());
                }
            });
            delete6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedLocation6.setText("");
                    mListener.onDeleteLocationClicked("6");
                }
            });
        }

        savedLocation7 = v.findViewById(R.id.savedLocation7);
        savedLocation7.setVisibility(View.GONE);
        if (locationsList.size() > 6) {
            savedLocation7.setText(locationsList.get(6));
            savedLocation7.setVisibility(View.VISIBLE);
            savedLocation7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSavedLocationClicked(savedLocation7.getText().toString());
                }
            });
            delete7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedLocation7.setText("");
                    mListener.onDeleteLocationClicked("7");
                }
            });
        }

        savedLocation8 = v.findViewById(R.id.savedLocation8);
        savedLocation8.setVisibility(View.GONE);
        if (locationsList.size() > 7) {
            savedLocation8.setText(locationsList.get(7));
            savedLocation8.setVisibility(View.VISIBLE);
            savedLocation8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSavedLocationClicked(savedLocation8.getText().toString());
                }
            });
            delete8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedLocation8.setText("");
                    mListener.onDeleteLocationClicked("8");
                }
            });
        }

        savedLocation9 = v.findViewById(R.id.savedLocation9);
        savedLocation9.setVisibility(View.GONE);
        if (locationsList.size() > 8) {
            savedLocation9.setText(locationsList.get(8));
            savedLocation9.setVisibility(View.VISIBLE);
            savedLocation9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onSavedLocationClicked(savedLocation9.getText().toString());
                }
            });
            delete9.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    savedLocation9.setText("");
                    mListener.onDeleteLocationClicked("9");
                }
            });
        }

        boolean content = false;

        if (savedLocation1.getText().toString().equals("") || savedLocation1.getText().toString() == null) {
            savedLocation1.setVisibility(View.GONE);
            delete1.setVisibility(View.GONE);
        } else {
            content = true;
        }
        if (savedLocation2.getText().toString().equals("") || savedLocation2.getText().toString() == null) {
            savedLocation2.setVisibility(View.GONE);
            delete2.setVisibility(View.GONE);
        } else {
            content = true;
        }
        if (savedLocation3.getText().toString().equals("") || savedLocation3.getText().toString() == null) {
            savedLocation3.setVisibility(View.GONE);
            delete3.setVisibility(View.GONE);
        } else {
            content = true;
        }
        if (savedLocation4.getText().toString().equals("") || savedLocation4.getText().toString() == null) {
            savedLocation4.setVisibility(View.GONE);
            delete4.setVisibility(View.GONE);
        } else {
            content = true;
        }
        if (savedLocation5.getText().toString().equals("") || savedLocation5.getText().toString() == null) {
            savedLocation5.setVisibility(View.GONE);
            delete5.setVisibility(View.GONE);
        } else {
            content = true;
        }
        if (savedLocation6.getText().toString().equals("") || savedLocation6.getText().toString() == null) {
            savedLocation6.setVisibility(View.GONE);
            delete6.setVisibility(View.GONE);
        } else {
            content = true;
        }
        if (savedLocation7.getText().toString().equals("") || savedLocation7.getText().toString() == null) {
            savedLocation7.setVisibility(View.GONE);
            delete7.setVisibility(View.GONE);
        } else {
            content = true;
        }
        if (savedLocation8.getText().toString().equals("") || savedLocation8.getText().toString() == null) {
            savedLocation8.setVisibility(View.GONE);
            delete8.setVisibility(View.GONE);
        } else {
            content = true;
        }
        if (savedLocation9.getText().toString().equals("") || savedLocation9.getText().toString() == null) {
            savedLocation9.setVisibility(View.GONE);
            delete9.setVisibility(View.GONE);
        } else {
            content = true;
        }

        if (!content) {
            noSavedLocations.setVisibility(View.VISIBLE);
        }

        return v;
    }

    /**
     * Ensure interface is implemented.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SavedLocationsFragment.OnSavedLocationsFragmentInteractionListener) {
            mListener = (SavedLocationsFragment.OnSavedLocationsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnSavedLocationsFragmentInteractionListener");
        }
    }

    /**
     * Interface for communicating with activities.
     */
    public interface OnSavedLocationsFragmentInteractionListener {

        void onSavedLocationClicked(String cityString);

        void onDeleteLocationClicked(String slot);
    }
}
