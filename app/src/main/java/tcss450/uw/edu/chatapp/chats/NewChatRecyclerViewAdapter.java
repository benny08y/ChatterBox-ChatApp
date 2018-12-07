package tcss450.uw.edu.chatapp.chats;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.chats.NewChatFragment.OnNewChatListFragmentInteractionListener;
import tcss450.uw.edu.chatapp.model.Contacts;

import java.util.ArrayList;
import java.util.List;

/**
 * Benjamin Yuen
 * NewChatRecyclerViewAdapter that displays the all of the current user's contacts that the user can start a chat with.
 *
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link OnNewChatListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class NewChatRecyclerViewAdapter extends RecyclerView.Adapter<NewChatRecyclerViewAdapter.ViewHolder> {

    private final List<Contacts> mValues;
    private final OnNewChatListFragmentInteractionListener mListener;
    private boolean isCheckBoxVisible;
    private boolean mIsEnabled;
    private ArrayList<Contacts> mCheckedContacts;
    private String mCurrentUsername;

    public NewChatRecyclerViewAdapter(List<Contacts> items,
                                      OnNewChatListFragmentInteractionListener listener, String username) {
        mValues = items;
        mListener = listener;
        mCheckedContacts=  new ArrayList<>();
        mCurrentUsername = username;
        Log.d("CURRENTUSER", "NewChatFrag NewCHatRecycler: " + mCurrentUsername);
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
        if (isCheckBoxVisible){
            holder.mCheckBox.setVisibility(View.VISIBLE);
        } else {
            holder.mCheckBox.setVisibility(View.INVISIBLE);
        }
        //Clear all checked contacts
        if (mIsEnabled){
            holder.mCheckBox.setChecked(false);
            mCheckedContacts.clear();
            Log.d("NewChat", mCheckedContacts.toString());
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    if (isCheckBoxVisible){
                        // Add or remove contacts to be selected for a group chat
                        if (holder.mCheckBox.isChecked()){
                            holder.mCheckBox.setChecked(false);
                            mCheckedContacts.remove(mValues.get(position));
                            Log.d("NewChat", "remove"+mCheckedContacts.toString());
                        } else {
                            holder.mCheckBox.setChecked(true);
                            mCheckedContacts.add(mValues.get(position));
                            Log.d("NewChat", "add"+mCheckedContacts.toString());
                        }
                    } else {
                        // Notify the active callbacks interface (the activity, if the
                        // fragment is attached to one) that an item has been selected.
                        addSingleChat(holder);
                    }
                }
            }
        });
    }

    //Make a new single chat
    private void addSingleChat(ViewHolder holder){
        mListener.newChatFragmentInteraction(holder.mItem, mCurrentUsername);
    }

    public ArrayList<Contacts> getCheckedContacts(){
        if (mCheckedContacts.isEmpty()){
            return null;
        }
        return mCheckedContacts;
    }
    public void setCheckBoxes(boolean ischecked){
        isCheckBoxVisible = ischecked;
    }
    public void setEnabledCheckBoxes(boolean isEnabled){
        mIsEnabled = isEnabled;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContactName;
        public final TextView mEmail;
        public final CheckBox mCheckBox;
        public Contacts mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContactName = (TextView) view.findViewById(R.id.newchat_single_nickname);
            mEmail = (TextView) view.findViewById(R.id.newchat_single_email);
            mCheckBox = view.findViewById(R.id.new_chat_checkbox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContactName.getText() + "'";
        }
    }
}
