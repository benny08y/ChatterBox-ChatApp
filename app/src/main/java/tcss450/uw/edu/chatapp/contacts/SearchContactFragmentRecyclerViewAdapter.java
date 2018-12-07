package tcss450.uw.edu.chatapp.contacts;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import tcss450.uw.edu.chatapp.contacts.SearchContactsFragment.OnSearchContactsFragmentInteractionListener;

import java.util.List;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.model.Contacts;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;

/**
 * Chris Kim,
 * Sets the recycler view inside of the fragmenr_search_contacts fragment
 */
public class SearchContactFragmentRecyclerViewAdapter extends RecyclerView.Adapter<SearchContactFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<Contacts> mValues;
    private final OnSearchContactsFragmentInteractionListener mListener;
    private String mEmail;
    private Activity mActivity;

    private String successMessage = "You have sent a contact request";

    public SearchContactFragmentRecyclerViewAdapter(Activity activity, String email, List<Contacts> items, OnSearchContactsFragmentInteractionListener listener) {
        mEmail = email;
        mValues = items;
        mListener = listener;
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_search_contact_member, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nicknameView.setText(mValues.get(position).getNickname());
        holder.emailView.setText(mValues.get(position).getEmail());
//        holder.firstNameView.setText((mValues.get(position).getFirstName()));
//        holder.lastNameView.setText(mValues.get(position).getLastName());

        holder.mButton.setOnClickListener(event -> {
            onAddContactPressed(position);
            //Toast.makeText(getActivity(), successMessage, Toast.LENGTH_SHORT).show();
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onSearchContactsFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    private void onAddContactPressed(int position) {
        if (mListener != null) {
            //send contact request using curremail and memail
            //have popup saying that the request was sent
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath("final-project-450.herokuapp.com")
                    .appendPath("contacts")
                    .appendPath("send_request")
                    .build();
            JSONObject messageJson = new JSONObject();


            try {
                messageJson.put("senderEmail", mEmail);
                messageJson.put("receiverEmail", mValues.get(position).getEmail());
                Log.e("IN_JSON", "post body email");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("IN_JSON", "didnt put email");
            }

            new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                    .onPostExecute(this::handleContactsGetOnPostExecute)
                    .onCancelled(error -> Log.e("SEND_TAG", error))
                    .build().execute();

            Toast.makeText(mActivity, successMessage, Toast.LENGTH_SHORT).show();
        }
    }

    private void handleContactsGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            JSONObject resultsJSON = new JSONObject(result);
            boolean success = resultsJSON.getBoolean("success");
            if (success) {
                Log.e("Successfully sent", "Success");
                //Toast.makeText(getActivity(), successMessage, Toast.LENGTH_SHORT).show();

            } else {
                //Toast.makeText(getActivity(), failedMessage, Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nicknameView;
        public final TextView emailView;
        public final Button mButton;
//        public final TextView firstNameView;
//        public final TextView lastNameView;
        public Contacts mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nicknameView = view.findViewById(R.id.contact_search_nickname);
            emailView = view.findViewById(R.id.contact_search_email);
            mButton = view.findViewById(R.id.contact_search_button_add);
//            firstNameView = view.findViewById();
//            lastNameView = view.findViewById();
        }
    }
}
