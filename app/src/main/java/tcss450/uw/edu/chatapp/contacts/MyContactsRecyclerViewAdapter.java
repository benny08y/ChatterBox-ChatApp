package tcss450.uw.edu.chatapp.contacts;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.model.Contacts;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;

import java.util.List;

/**
 * Aaron Bardsley
 *
 * This class is a RecyclerView used for displaying the user's existing contacts.
 */
public class MyContactsRecyclerViewAdapter extends RecyclerView.Adapter<MyContactsRecyclerViewAdapter.ViewHolder> {

    private final List<Contacts> mValues;
    private String mEmail;
    private String mRemovedEmail;
    private RecyclerView mRecyclerView;

    public MyContactsRecyclerViewAdapter(RecyclerView recyclerView,
                                         String email,
                                         List<Contacts> items) {
        mValues = items;
        mEmail = email;
        mRecyclerView = recyclerView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contacts, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Aaron Bardsley
     *
     * end point: contacts/delete_contact
     * remove contact from existing contacts
     */
    private void onRemoveButtonPress(final ViewHolder holder, int position) {
        mRemovedEmail = mValues.get(position).getEmail();

        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath("final-project-450.herokuapp.com")
                .appendPath("contacts")
                .appendPath("delete_contact")
                .build();
        JSONObject messageJson = new JSONObject();

        if (!mEmail.isEmpty()) {
            try {
                messageJson.put("userEmail", mEmail);
                messageJson.put("removedEmail", mRemovedEmail);
            } catch (JSONException e) {
                Log.d("ContactsFragment onButtonPress: ", "invalid user email");
            }
            new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                    //.onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleContactRemoveOnPostExecute)
                    .onCancelled(error -> Log.e("SEND_TAG", error))
                    .build().execute();
            mValues.remove(position);
            mRecyclerView.removeViewAt(position);
            this.notifyItemRemoved(position);
            this.notifyItemRangeChanged(position, mValues.size());
        } else {
            Log.d("ContactsFragment onButtonPress: ", "invalid user email");
        }
    }

    /**
     * Aaron Bardsley
     *
     * Simple response for removed contact
     */
    private void handleContactRemoveOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            boolean success = root.getBoolean("success");
            if (success) {
                //Log.d("ContactsFragment: ", "Successfully sent");
            }
            //onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR in handleContactsGetOnPostExecute: ", e.getMessage());
            //notify user
            //onWaitFragmentInteractionHide();
        }
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nicknameView.setText(mValues.get(position).getNickname());
        holder.emailView.setText(mValues.get(position).getEmail());
        holder.fnameView.setText(mValues.get(position).getFirstName());
        holder.lnameView.setText(mValues.get(position).getLastName());

        holder.mRemoveButton.setOnClickListener(e -> {
            onRemoveButtonPress(holder, position);
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nicknameView;
        public final TextView emailView;
        public final TextView fnameView;
        public final TextView lnameView;
        public Contacts mItem;
        public Button mRemoveButton;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nicknameView = view.findViewById(R.id.contact_fragment_nickname);
            emailView = view.findViewById(R.id.contact_fragment_email);
            mRemoveButton = view.findViewById(R.id.contact_button_remove);
            fnameView = view.findViewById(R.id.contact_fragment_firstname);
            lnameView = view.findViewById(R.id.contact_fragment_lastname);
        }
    }
}
