package tcss450.uw.edu.chatapp.weather;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import tcss450.uw.edu.chatapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentConditionsLatLngFragment extends Fragment {

    TextView displayLatLng, cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    ProgressBar loader;
    Typeface weatherFont;
    String lat;
    String lon;

    public CurrentConditionsLatLngFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_current_conditions_lat_lng, container, false);

        setHasOptionsMenu(true);

        if (getArguments() != null) {
            lat = Double.toString(getArguments().getDouble("lat"));
            lon = Double.toString(getArguments().getDouble("lon"));
        }

        loader = (ProgressBar) v.findViewById(R.id.loader);
        displayLatLng = (TextView) v.findViewById(R.id.displayLatLng);
        cityField = (TextView) v.findViewById(R.id.city_field);
        updatedField = (TextView) v.findViewById(R.id.updated_field);
        detailsField = (TextView) v.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) v.findViewById(R.id.current_temperature_field);
        humidity_field = (TextView) v.findViewById(R.id.humidity_field);
        pressure_field = (TextView) v.findViewById(R.id.pressure_field);
        weatherIcon = (TextView) v.findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericonsregularwebfont.ttf");
        weatherIcon.setTypeface(weatherFont);

        taskLoadUp(lat, lon);

        return v;
    }

    public void taskLoadUp(String query1, String query2) {
        if (WeatherHelpers.isNetworkAvailable(getActivity().getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query1, query2);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class DownloadWeather extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }

        protected String doInBackground(String... args) {
            String xml = WeatherHelpers.excuteGet("http://api.openweathermap.org/data/2.5/weather?lat=" + args[0] +
                    "&lon=" + args[1] + "&units=imperial&appid=" + "4dfb61d8cb257761ac107050df586c2d");
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {

            try {
                JSONObject json = new JSONObject(xml);
                if (json != null) {
                    JSONObject details = json.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = json.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();

                    cityField.setText(json.getString("name").toUpperCase(Locale.US) + ", " + json.getJSONObject("sys").getString("country"));
                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + "°F");
                    humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                    pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
                    updatedField.setText(df.format(new Date(json.getLong("dt") * 1000)));
                    weatherIcon.setText(Html.fromHtml(WeatherHelpers.setWeatherIcon(details.getInt("id"),
                            json.getJSONObject("sys").getLong("sunrise") * 1000,
                            json.getJSONObject("sys").getLong("sunset") * 1000)));

                    loader.setVisibility(View.GONE);
                    displayLatLng.setText(lat + ", " + lon);
                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error, Check Latitude And Longitude", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.weather, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_current) {
            // TODO switch to current view
            return true;
        } else if (id == R.id.action_hour) {
            // TODO switch to hour view
            return true;
        } else if (id == R.id.action_day) {
            // TODO switch to day view
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
