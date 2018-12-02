package tcss450.uw.edu.chatapp.chats;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.chats.NewChatSingleFragment.OnNewSingleChatListFragmentInteractionListener;
import tcss450.uw.edu.chatapp.model.Contacts;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnNewSingleChatListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Contacts> mValues;
    private final OnNewSingleChatListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<Contacts> items, NewChatSingleFragment.OnNewSingleChatListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.new_chat_single_frag, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContactName.setText(mValues.get(position).getNickname());
        holder.mEmail.setText(mValues.get(position).getEmail());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.newSingleChatFragmentInteraction(holder.mItem);
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
        public final TextView mContactName;
        public final TextView mEmail;
        public Contacts mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContactName = (TextView) view.findViewById(R.id.newchat_single_nickname);
            mEmail = (TextView) view.findViewById(R.id.newchat_single_email);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContactName.getText() + "'";
        }
    }
}
