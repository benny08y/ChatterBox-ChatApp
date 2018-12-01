package tcss450.uw.edu.chatapp.weather;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import tcss450.uw.edu.chatapp.R;

public class WeatherFragment extends Fragment {

    private WeatherFragment.OnWeatherFragmentInteractionListener mListener;
    private static Button mMyCurrentLocationButton;
    private static Location mCurrentLocation;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather, container, false);

        mMyCurrentLocationButton = (Button) v.findViewById(R.id.weatherFragmentMyCurrentLocationButton);
        mMyCurrentLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMyCurrentLocationButtonClicked(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            }
        });
        Button b = (Button) v.findViewById(R.id.weatherFragmentZipCodeButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onZipCodeButtonClicked();
            }
        });
        b = (Button) v.findViewById(R.id.weatherFragmentMapButton);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMapButtonClicked();
            }
        });

        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WeatherFragment.OnWeatherFragmentInteractionListener) {
            mListener = (WeatherFragment.OnWeatherFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWeatherFragmentInteractionListener");
        }
    }

    public static void setLocation(final Location location) {
        mCurrentLocation = location;
        if (mMyCurrentLocationButton != null) {
            mMyCurrentLocationButton.setText("MY CURRENT LOCATION: " + "\n" + mCurrentLocation.getLatitude() + " " + mCurrentLocation.getLongitude());
        }
    }

    public interface OnWeatherFragmentInteractionListener {

        void onMyCurrentLocationButtonClicked(Double lat, Double lon);

        void onZipCodeButtonClicked();

        void onMapButtonClicked();
    }
}
