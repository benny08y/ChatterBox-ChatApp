package tcss450.uw.edu.chatapp.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.model.Contacts;

/**
 * Aaron Bardsley
 *
 * This class is a RecycleView for displaying all contact requests sent out by the user.
 */
public class ContactSentRequestsRecyclerViewAdapter extends RecyclerView.Adapter<ContactSentRequestsRecyclerViewAdapter.ViewHolder> {

    private final List<Contacts> mValues;

    public ContactSentRequestsRecyclerViewAdapter(List<Contacts> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact_sent_requests, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.nicknameView.setText(mValues.get(position).getNickname());
        holder.emailView.setText(mValues.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nicknameView;
        public final TextView emailView;
        public Contacts mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nicknameView = view.findViewById(R.id.contact_sent_nickname);
            emailView = view.findViewById(R.id.contact_sent_email);
        }
    }
}
