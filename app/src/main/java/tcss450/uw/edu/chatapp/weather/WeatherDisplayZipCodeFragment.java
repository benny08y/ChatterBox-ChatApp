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
public class WeatherDisplayZipCodeFragment extends Fragment {

    TextView selectZipCode, cityField, detailsField, currentTemperatureField, humidity_field, pressure_field, weatherIcon, updatedField;
    TextView hour4dt, hour8dt, hour12dt, hour16dt, hour20dt, hour24dt, hour28dt, hour32dt;
    TextView hour4icon, hour8icon, hour12icon, hour16icon, hour20icon, hour24icon, hour28icon, hour32icon;
    TextView hour4temp, hour8temp, hour12temp, hour16temp, hour20temp, hour24temp, hour28temp, hour32temp;
    TextView day1dt, day2dt, day3dt, day4dt, day5dt, day6dt, day7dt, day8dt, day9dt, day10dt;
    TextView day1icon, day2icon, day3icon, day4icon, day5icon, day6icon, day7icon, day8icon, day9icon, day10icon;
    TextView day1max, day2max, day3max, day4max, day5max, day6max, day7max, day8max, day9max, day10max;
    TextView day1min, day2min, day3min, day4min, day5min, day6min, day7min, day8min, day9min, day10min;
    ProgressBar loader;
    Typeface weatherFont;
    String zipCode = "98006";

    public WeatherDisplayZipCodeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_weather_display, container, false);

        if (getArguments() != null) {
            zipCode = getArguments().getString("zip code");
        }

        loader = (ProgressBar) v.findViewById(R.id.loader);
        selectZipCode = (TextView) v.findViewById(R.id.selectZipCode);
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
        hour4temp = v.findViewById(R.id.hour4temp);
        hour8temp = v.findViewById(R.id.hour8temp);
        hour12temp = v.findViewById(R.id.hour12temp);
        hour16temp = v.findViewById(R.id.hour16temp);
        hour20temp = v.findViewById(R.id.hour20temp);
        hour24temp = v.findViewById(R.id.hour24temp);
        hour28temp = v.findViewById(R.id.hour28temp);
        hour32temp = v.findViewById(R.id.hour32temp);
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

        taskLoadUp(zipCode);

        selectZipCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(v.getContext());
                alertDialog.setTitle("Change Zip Code");
                final EditText input = new EditText(v.getContext());
                input.setText(zipCode);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Change",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                zipCode = input.getText().toString();
                                taskLoadUp(zipCode);
                            }
                        });
                alertDialog.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                alertDialog.show();
            }
        });

        return v;
    }

    public void taskLoadUp(String query) {
        if (WeatherHelpers.isNetworkAvailable(getActivity().getApplicationContext())) {
            DownloadWeather task = new DownloadWeather();
            task.execute(query);
        } else {
            Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
        }
    }

    class DownloadWeather extends AsyncTask<String, Void, String[]> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loader.setVisibility(View.VISIBLE);

        }

        protected String[] doInBackground(String... args) {
            String current = WeatherHelpers.excuteGet("http://api.openweathermap.org/data/2.5/weather?zip=" + args[0] +
                    "&units=imperial&appid=" + "4dfb61d8cb257761ac107050df586c2d");
            String hour = WeatherHelpers.excuteGet("http://api.openweathermap.org/data/2.5/forecast?zip=" + args[0] +
                    "&units=imperial&appid=" + "4dfb61d8cb257761ac107050df586c2d");
            String day = WeatherHelpers.excuteGet("http://api.openweathermap.org/data/2.5/forecast/daily?zip=" + args[0] +
                    "&units=imperial&appid=" + "4dfb61d8cb257761ac107050df586c2d");
            String[] xml = new String[3];
            xml[0] = current;
            xml[1] = hour;
            xml[2] = day;

            return xml;
        }

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

                    cityField.setText(jsonCurrent.getString("name").toUpperCase(Locale.US) + ", " + jsonCurrent.getJSONObject("sys").getString("country"));
                    detailsField.setText(details.getString("description").toUpperCase(Locale.US));
                    currentTemperatureField.setText(String.format("%.2f", main.getDouble("temp")) + "°F");
                    humidity_field.setText("Humidity: " + main.getString("humidity") + "%");
                    pressure_field.setText("Pressure: " + main.getString("pressure") + " hPa");
                    updatedField.setText(df.format(new Date(jsonCurrent.getLong("dt") * 1000)));
                    weatherIcon.setText(Html.fromHtml(WeatherHelpers.setWeatherIcon(details.getInt("id"),
                            jsonCurrent.getJSONObject("sys").getLong("sunrise") * 1000,
                            jsonCurrent.getJSONObject("sys").getLong("sunset") * 1000)));

                    loader.setVisibility(View.GONE);

                }
            } catch (JSONException e) {
                Toast.makeText(getActivity().getApplicationContext(), "Error, Check Zip Code", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
