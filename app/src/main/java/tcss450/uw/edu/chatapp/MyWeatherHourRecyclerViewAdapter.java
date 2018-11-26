package tcss450.uw.edu.chatapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.chatapp.WeatherHourFragment.OnListFragmentInteractionListener;
import tcss450.uw.edu.chatapp.dummy.WeatherHourContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link WeatherHourContent} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyWeatherHourRecyclerViewAdapter extends RecyclerView.Adapter<MyWeatherHourRecyclerViewAdapter.ViewHolder> {

    private final List<WeatherHourContent> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyWeatherHourRecyclerViewAdapter(List<WeatherHourContent> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_weatherhour, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mDt.setText(mValues.get(position).getDt());
        holder.mIcon.setText(mValues.get(position).getIcon());
        holder.mTemperature.setText(mValues.get(position).getTemperature());
        holder.mDetails.setText(mValues.get(position).getDetails());
        holder.mHumidity.setText(mValues.get(position).getHumidity());
        holder.mPressure.setText(mValues.get(position).getPressure());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mDt;
        public final TextView mIcon;
        public final TextView mTemperature;
        public final TextView mDetails;
        public final TextView mHumidity;
        public final TextView mPressure;
        public WeatherHourContent mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mDt = (TextView) view.findViewById(R.id.twenty_four_weather_dt);
            mIcon = (TextView) view.findViewById(R.id.twenty_four_weather_icon);
            mTemperature = (TextView) view.findViewById(R.id.twenty_four_current_temperature_field);
            mDetails = (TextView) view.findViewById(R.id.twenty_four_details_field);
            mHumidity = (TextView) view.findViewById(R.id.twenty_four_humidity_field);
            mPressure = (TextView) view.findViewById(R.id.twenty_four_pressure_field);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDt.getText() + "'";
        }
    }
}
