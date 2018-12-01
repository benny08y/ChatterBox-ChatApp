package tcss450.uw.edu.chatapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tcss450.uw.edu.chatapp.weather.WeatherDisplayLatLngFragment;
import tcss450.uw.edu.chatapp.weather.WeatherFragment;
import tcss450.uw.edu.chatapp.weather.WeatherHelpers;


/**
 * A simple {@link Fragment} subclass.
 */
public class LandingPageFragment extends Fragment {

    private LandingPageFragment.OnLandingPageFragmentInteractionListener mListener;
    TextView weatherInfo, weatherIcon;
    Typeface weatherFont;
    Double lat;
    Double lon;
    private static Location mCurrentLocation;

    public LandingPageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_landing_page, container, false);

        if (mCurrentLocation != null) {
            lat = mCurrentLocation.getLatitude();
            lon = mCurrentLocation.getLongitude();
        }
        if (getArguments() != null) {
            lat = getArguments().getDouble("lat");
            lon = getArguments().getDouble("lon");
        }

        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericonsregularwebfont.ttf");

        weatherInfo = v.findViewById(R.id.landingPage_weather);
        weatherIcon = v.findViewById(R.id.landingPage_weatherIcon);
        weatherIcon.setTypeface(weatherFont);

        if (lat != null && lon != null) {
            taskLoadUp(Double.toString(lat), Double.toString(lon));
        }

        weatherInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onWeatherClicked(lat, lon);
            }
        });

        weatherIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onWeatherClicked(lat, lon);
            }
        });

        return v;
    }

    public void taskLoadUp(String query1, String query2) {
        if (WeatherHelpers.isNetworkAvailable(getActivity().getApplicationContext())) {
            LandingPageFragment.DownloadWeather task = new LandingPageFragment.DownloadWeather();
            task.execute(query1, query2);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class DownloadWeather extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(String... args) {
            String xml = WeatherHelpers.excuteGet("http://api.openweathermap.org/data/2.5/weather?lat=" + args[0] +
                    "&lon=" + args[1] + "&units=imperial&appid=" + "4dfb61d8cb257761ac107050df586c2d");

            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject jsonCurrent = new JSONObject(xml);
                if (jsonCurrent != null) {
                    JSONObject details = jsonCurrent.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = jsonCurrent.getJSONObject("main");

                    String s = jsonCurrent.getString("name").toUpperCase(Locale.US) + ", " + jsonCurrent.getJSONObject("sys").getString("country") + "\n" +
                            details.getString("description").toUpperCase(Locale.US) + "\n" +
                                    String.format("%.0f", main.getDouble("temp")) + "°F";
                    weatherInfo.setText(s);
                    weatherIcon.setText(Html.fromHtml(WeatherHelpers.setWeatherIcon(details.getInt("id"),
                            jsonCurrent.getJSONObject("sys").getLong("sunrise") * 1000,
                            jsonCurrent.getJSONObject("sys").getLong("sunset") * 1000)));
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error, Check Location", Toast.LENGTH_LONG).show();
            }

        }
    }

    public static void setLocation(final Location location) {
        mCurrentLocation = location;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LandingPageFragment.OnLandingPageFragmentInteractionListener) {
            mListener = (LandingPageFragment.OnLandingPageFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLandingPageFragmentInteractionListener");
        }
    }

    public interface OnLandingPageFragmentInteractionListener {

        void onWeatherClicked(Double lat, Double lon);
    }

}