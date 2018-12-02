package tcss450.uw.edu.chatapp.contacts;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import tcss450.uw.edu.chatapp.contacts.SearchContactsFragment.OnSearchContactsFragmentInteractionListener;

import java.util.List;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.model.Contacts;

public class SearchContactFragmentRecyclerViewAdapter extends RecyclerView.Adapter<SearchContactFragmentRecyclerViewAdapter.ViewHolder> {

    private final List<Contacts> mValues;
    private final OnSearchContactsFragmentInteractionListener mListener;

    public SearchContactFragmentRecyclerViewAdapter(List<Contacts> items, OnSearchContactsFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
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
        holder.firstNameView.setText((mValues.get(position).getFirstName()));
        holder.lastNameView.setText(mValues.get(position).getLastName());

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

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView nicknameView;
        public final TextView emailView;
        public final TextView firstNameView;
        public final TextView lastNameView;
        public Contacts mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            nicknameView = view.findViewById(R.id.Search_Contacts_Username);
            emailView = view.findViewById(R.id.Search_Contacts_Email);
            firstNameView = view.findViewById(R.id.Search_Contacts_FirstName);
            lastNameView = view.findViewById(R.id.Search_Contacts_LastName);
        }
    }
}
