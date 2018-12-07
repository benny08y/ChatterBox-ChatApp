package tcss450.uw.edu.chatapp.weather;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import tcss450.uw.edu.chatapp.R;


/**
 * This class displays the weather from latitude and longitude text strings.
 */
public class WeatherDisplayLatLngFragment extends Fragment {

    private WeatherDisplayLatLngFragment.OnWeatherDisplayLatLngFragmentInteractionListener mListener;
    TextView displayLatLng, cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    TextView hour4dt, hour8dt, hour12dt, hour16dt, hour20dt, hour24dt, hour28dt, hour32dt, hour36dt;
    TextView hour4icon, hour8icon, hour12icon, hour16icon, hour20icon, hour24icon, hour28icon, hour32icon, hour36icon;
    TextView hour4temp, hour8temp, hour12temp, hour16temp, hour20temp, hour24temp, hour28temp, hour32temp, hour36temp;
    TextView day1dt, day2dt, day3dt, day4dt, day5dt, day6dt, day7dt, day8dt, day9dt, day10dt;
    TextView day1icon, day2icon, day3icon, day4icon, day5icon, day6icon, day7icon, day8icon, day9icon, day10icon;
    TextView day1max, day2max, day3max, day4max, day5max, day6max, day7max, day8max, day9max, day10max;
    TextView day1min, day2min, day3min, day4min, day5min, day6min, day7min, day8min, day9min, day10min;
    TextView saveLocationButton;
    ProgressBar loader;
    Typeface weatherFont;
    String lat;
    String lon;
    String cityString;

    public WeatherDisplayLatLngFragment() {
        // Required empty public constructor
    }

    /**
     * Initializes fragment elements.
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
        View v = inflater.inflate(R.layout.fragment_weather_display, container, false);

        if (getArguments() != null) {
            lat = Double.toString(getArguments().getDouble("lat"));
            lon = Double.toString(getArguments().getDouble("lon"));
        }

        loader = (ProgressBar) v.findViewById(R.id.loader);
        displayLatLng = (TextView) v.findViewById(R.id.selectZipCode);
        cityField = (TextView) v.findViewById(R.id.city_field);
        updatedField = (TextView) v.findViewById(R.id.updated_field);
        detailsField = (TextView) v.findViewById(R.id.details_field);
        currentTemperatureField = (TextView) v.findViewById(R.id.current_temperature_field);
        humidity_field = (TextView) v.findViewById(R.id.humidity_field);
        pressure_field = (TextView) v.findViewById(R.id.pressure_field);
        weatherIcon = (TextView) v.findViewById(R.id.weather_icon);
        weatherFont = Typeface.createFromAsset(getActivity().getAssets(), "fonts/weathericonsregularwebfont.ttf");
        weatherIcon.setTypeface(weatherFont);
        hour4dt = v.findViewById(R.id.hour4dt);
        hour8dt = v.findViewById(R.id.hour8dt);
        hour12dt = v.findViewById(R.id.hour12dt);
        hour16dt = v.findViewById(R.id.hour16dt);
        hour20dt = v.findViewById(R.id.hour20dt);
        hour24dt = v.findViewById(R.id.hour24dt);
        hour28dt = v.findViewById(R.id.hour28dt);
        hour32dt = v.findViewById(R.id.hour32dt);
        hour36dt = v.findViewById(R.id.hour36dt);
        hour4icon = v.findViewById(R.id.hour4icon);
        hour4icon.setTypeface(weatherFont);
        hour8icon = v.findViewById(R.id.hour8icon);
        hour8icon.setTypeface(weatherFont);
        hour12icon = v.findViewById(R.id.hour12icon);
        hour12icon.setTypeface(weatherFont);
        hour16icon = v.findViewById(R.id.hour16icon);
        hour16icon.setTypeface(weatherFont);
        hour20icon = v.findViewById(R.id.hour20icon);
        hour20icon.setTypeface(weatherFont);
        hour24icon = v.findViewById(R.id.hour24icon);
        hour24icon.setTypeface(weatherFont);
        hour28icon = v.findViewById(R.id.hour28icon);
        hour28icon.setTypeface(weatherFont);
        hour32icon = v.findViewById(R.id.hour32icon);
        hour32icon.setTypeface(weatherFont);
        hour36icon = v.findViewById(R.id.hour36icon);
        hour36icon.setTypeface(weatherFont);
        hour4temp = v.findViewById(R.id.hour4temp);
        hour8temp = v.findViewById(R.id.hour8temp);
        hour12temp = v.findViewById(R.id.hour12temp);
        hour16temp = v.findViewById(R.id.hour16temp);
        hour20temp = v.findViewById(R.id.hour20temp);
        hour24temp = v.findViewById(R.id.hour24temp);
        hour28temp = v.findViewById(R.id.hour28temp);
        hour32temp = v.findViewById(R.id.hour32temp);
        hour36temp = v.findViewById(R.id.hour36temp);
        day1dt = v.findViewById(R.id.day1dt);
        day2dt = v.findViewById(R.id.day2dt);
        day3dt = v.findViewById(R.id.day3dt);
        day4dt = v.findViewById(R.id.day4dt);
        day5dt = v.findViewById(R.id.day5dt);
        day6dt = v.findViewById(R.id.day6dt);
        day7dt = v.findViewById(R.id.day7dt);
        day8dt = v.findViewById(R.id.day8dt);
        day9dt = v.findViewById(R.id.day9dt);
        day10dt = v.findViewById(R.id.day10dt);
        day1icon = v.findViewById(R.id.day1icon);
        day1icon.setTypeface(weatherFont);
        day2icon = v.findViewById(R.id.day2icon);
        day2icon.setTypeface(weatherFont);
        day3icon = v.findViewById(R.id.day3icon);
        day3icon.setTypeface(weatherFont);
        day4icon = v.findViewById(R.id.day4icon);
        day4icon.setTypeface(weatherFont);
        day5icon = v.findViewById(R.id.day5icon);
        day5icon.setTypeface(weatherFont);
        day6icon = v.findViewById(R.id.day6icon);
        day6icon.setTypeface(weatherFont);
        day7icon = v.findViewById(R.id.day7icon);
        day7icon.setTypeface(weatherFont);
        day8icon = v.findViewById(R.id.day8icon);
        day8icon.setTypeface(weatherFont);
        day9icon = v.findViewById(R.id.day9icon);
        day9icon.setTypeface(weatherFont);
        day10icon = v.findViewById(R.id.day10icon);
        day10icon.setTypeface(weatherFont);
        day1max = v.findViewById(R.id.day1max);
        day2max = v.findViewById(R.id.day2max);
        day3max = v.findViewById(R.id.day3max);
        day4max = v.findViewById(R.id.day4max);
        day5max = v.findViewById(R.id.day5max);
        day6max = v.findViewById(R.id.day6max);
        day7max = v.findViewById(R.id.day7max);
        day8max = v.findViewById(R.id.day8max);
        day9max = v.findViewById(R.id.day9max);
        day10max = v.findViewById(R.id.day10max);
        day1min = v.findViewById(R.id.day1min);
        day2min = v.findViewById(R.id.day2min);
        day3min = v.findViewById(R.id.day3min);
        day4min = v.findViewById(R.id.day4min);
        day5min = v.findViewById(R.id.day5min);
        day6min = v.findViewById(R.id.day6min);
        day7min = v.findViewById(R.id.day7min);
        day8min = v.findViewById(R.id.day8min);
        day9min = v.findViewById(R.id.day9min);
        day10min = v.findViewById(R.id.day10min);
        saveLocationButton = v.findViewById(R.id.saveLocationsButton);

        taskLoadUp(lat, lon);

        saveLocationButton.setOnClickListener(new View.OnClickListener() {
            /**
             * Calls the activity to save location.
             *
             * @param v
             */
            @Override
            public void onClick(View v) {
                mListener.onSaveLocationButtonClicked(cityString);
            }
        });

        return v;
    }

    /**
     * Starts API information retrieval from latitude string and longitude string.
     *
     * @param query
     */
    public void taskLoadUp(String query1, String query2) {
        if (WeatherHelpers.isNetworkAvailable(getActivity().getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query1, query2);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Fetches weather information from API calls for current conditions, 24-hour, and 10-day weather
     * using a String for the latitude and string for longitude.
     */
    class DownloadWeather extends AsyncTask<String, Void, String[]> {

        /**
         * Displays the loader while information is being loaded onto fragment.
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }

        /**
         * API information retrieval from webservice and stores JSON string results into a list.
         *
         * @param args
         * @return
         */
        protected String[] doInBackground(String... args) {
            String current = WeatherHelpers.excuteGet("http://api.openweathermap.org/data/2.5/weather?lat=" + args[0] +
                    "&lon=" + args[1] + "&units=imperial&appid=" + "4dfb61d8cb257761ac107050df586c2d");
            String hour = WeatherHelpers.excuteGet("http://api.openweathermap.org/data/2.5/forecast?lat=" + args[0] +
                    "&lon=" + args[1] + "&units=imperial&appid=" + "4dfb61d8cb257761ac107050df586c2d");
            String day = WeatherHelpers.excuteGet("http://api.openweathermap.org/data/2.5/forecast/daily?lat=" + args[0] +
                    "&lon=" + args[1] + "&cnt=10&units=imperial&appid=" + "4dfb61d8cb257761ac107050df586c2d");
            String[] xml = new String[3];
            xml[0] = current;
            xml[1] = hour;
            xml[2] = day;

            return xml;
        }

        /**
         * Parses JSON of the current conditions, 24-hour, and 10-day weather into String fields
         * and sets fragment display elements to display the correct information.
         *
         * @param xml
         */
        @Override
        protected void onPostExecute(String[] xml) {

            try {
                JSONObject jsonCurrent = new JSONObject(xml[0]);
                JSONObject jsonHour = new JSONObject(xml[1]);
                JSONObject jsonDay = new JSONObject(xml[2]);
                if (jsonCurrent != null && jsonHour != null && jsonDay != null) {
                    JSONObject details = jsonCurrent.getJSONArray("weather").getJSONObject(0);
                    JSONObject main = jsonCurrent.getJSONObject("main");
                    DateFormat df = DateFormat.getDateTimeInstance();
                    DateFormat dfHour = new SimpleDateFormat("ha");
                    DateFormat dfDay = new SimpleDateFormat("EEEEE");

                    cityString = jsonCurrent.getString("name").toUpperCase(Locale.US) + ", " + jsonCurrent.getJSONObject("sys").getString("country");
                    cityField.setText(cityString);
                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.0f", main.getDouble("temp")) + "°F");
                    humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                    pressure_field.setText("Pressure: " + (((double) Math.round(main.getDouble("pressure") / 1013.25 * 1000d)) / 1000d) + " atm");
                    updatedField.setText(df.format(new Date(jsonCurrent.getLong("dt") * 1000)));
                    weatherIcon.setText(Html.fromHtml(WeatherHelpers.setWeatherIcon(details.getInt("id"),
                            jsonCurrent.getJSONObject("sys").getLong("sunrise") * 1000,
                            jsonCurrent.getJSONObject("sys").getLong("sunset") * 1000)));

                    JSONObject weatherHourObject = jsonHour.getJSONArray("list").getJSONObject(0);
                    main = weatherHourObject.getJSONObject("main");
                    details = weatherHourObject.getJSONArray("weather").getJSONObject(0);
                    hour4dt.setText(dfHour.format(new Date(weatherHourObject.getLong("dt") * 1000)));
                    hour4icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    hour4temp.setText(String.format("%.0f", main.getDouble("temp")) + "°F");

                    weatherHourObject = jsonHour.getJSONArray("list").getJSONObject(1);
                    main = weatherHourObject.getJSONObject("main");
                    details = weatherHourObject.getJSONArray("weather").getJSONObject(0);
                    hour8dt.setText(dfHour.format(new Date(weatherHourObject.getLong("dt") * 1000)));
                    hour8icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    hour8temp.setText(String.format("%.0f", main.getDouble("temp")) + "°F");

                    weatherHourObject = jsonHour.getJSONArray("list").getJSONObject(2);
                    main = weatherHourObject.getJSONObject("main");
                    details = weatherHourObject.getJSONArray("weather").getJSONObject(0);
                    hour12dt.setText(dfHour.format(new Date(weatherHourObject.getLong("dt") * 1000)));
                    hour12icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    hour12temp.setText(String.format("%.0f", main.getDouble("temp")) + "°F");

                    weatherHourObject = jsonHour.getJSONArray("list").getJSONObject(3);
                    main = weatherHourObject.getJSONObject("main");
                    details = weatherHourObject.getJSONArray("weather").getJSONObject(0);
                    hour16dt.setText(dfHour.format(new Date(weatherHourObject.getLong("dt") * 1000)));
                    hour16icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    hour16temp.setText(String.format("%.0f", main.getDouble("temp")) + "°F");

                    weatherHourObject = jsonHour.getJSONArray("list").getJSONObject(4);
                    main = weatherHourObject.getJSONObject("main");
                    details = weatherHourObject.getJSONArray("weather").getJSONObject(0);
                    hour20dt.setText(dfHour.format(new Date(weatherHourObject.getLong("dt") * 1000)));
                    hour20icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    hour20temp.setText(String.format("%.0f", main.getDouble("temp")) + "°F");

                    weatherHourObject = jsonHour.getJSONArray("list").getJSONObject(5);
                    main = weatherHourObject.getJSONObject("main");
                    details = weatherHourObject.getJSONArray("weather").getJSONObject(0);
                    hour24dt.setText(dfHour.format(new Date(weatherHourObject.getLong("dt") * 1000)));
                    hour24icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    hour24temp.setText(String.format("%.0f", main.getDouble("temp")) + "°F");

                    weatherHourObject = jsonHour.getJSONArray("list").getJSONObject(6);
                    main = weatherHourObject.getJSONObject("main");
                    details = weatherHourObject.getJSONArray("weather").getJSONObject(0);
                    hour28dt.setText(dfHour.format(new Date(weatherHourObject.getLong("dt") * 1000)));
                    hour28icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    hour28temp.setText(String.format("%.0f", main.getDouble("temp")) + "°F");

                    weatherHourObject = jsonHour.getJSONArray("list").getJSONObject(7);
                    main = weatherHourObject.getJSONObject("main");
                    details = weatherHourObject.getJSONArray("weather").getJSONObject(0);
                    hour32dt.setText(dfHour.format(new Date(weatherHourObject.getLong("dt") * 1000)));
                    hour32icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    hour32temp.setText(String.format("%.0f", main.getDouble("temp")) + "°F");

                    weatherHourObject = jsonHour.getJSONArray("list").getJSONObject(8);
                    main = weatherHourObject.getJSONObject("main");
                    details = weatherHourObject.getJSONArray("weather").getJSONObject(0);
                    hour36dt.setText(dfHour.format(new Date(weatherHourObject.getLong("dt") * 1000)));
                    hour36icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    hour36temp.setText(String.format("%.0f", main.getDouble("temp")) + "°F");

                    JSONObject weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(0);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day1dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day1icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day1max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day1min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(1);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day2dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day2icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day2max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day2min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(2);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day3dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day3icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day3max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day3min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(3);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day4dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day4icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day4max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day4min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(4);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day5dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day5icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day5max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day5min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(5);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day6dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day6icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day6max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day6min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(6);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day7dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day7icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day7max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day7min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(7);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day8dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day8icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day8max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day8min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(8);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day9dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day9icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day9max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day9min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    weatherDayObject = jsonDay.getJSONArray("list").getJSONObject(9);
                    details = weatherDayObject.getJSONArray("weather").getJSONObject(0);
                    main = weatherDayObject.getJSONObject("temp");
                    day10dt.setText(dfDay.format(new Date(weatherDayObject.getLong("dt") * 1000)));
                    day10icon.setText(Html.fromHtml(WeatherHelpers.setWeatherIconHour(details.getInt("id"),
                            details.getString("icon").substring(details.getString("icon").length() - 1))));
                    day10max.setText(String.format("%.0f", main.getDouble("max")) + "°F");
                    day10min.setText(String.format("%.0f", main.getDouble("min")) + "°F");

                    displayLatLng.setText(lat + ", " + lon);

                    saveLocationButton.setText("Save Location");
                    saveLocationButton.setTextColor(Color.BLUE);

                    loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error, Check Latitude And Longitude", Toast.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Ensures activities implement a listener to enable location saving button.
     *
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof WeatherDisplayLatLngFragment.OnWeatherDisplayLatLngFragmentInteractionListener) {
            mListener = (WeatherDisplayLatLngFragment.OnWeatherDisplayLatLngFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnWeatherDisplayLatLngFragmentInteractionListener");
        }
    }

    /**
     * Interface for activity interaction.
     */
    public interface OnWeatherDisplayLatLngFragmentInteractionListener {
        void onSaveLocationButtonClicked(String cityString);
    }
}
