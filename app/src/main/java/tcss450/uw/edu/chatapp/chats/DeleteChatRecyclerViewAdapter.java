package tcss450.uw.edu.chatapp.chats;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.chats.DeleteChatFragment.DeleteChatFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Benjamin Yuen
 *
 * {@link RecyclerView.Adapter} that can display a {@link } and makes a call to the
 * specified {@link DeleteChatFragment.DeleteChatFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class DeleteChatRecyclerViewAdapter extends RecyclerView.Adapter<DeleteChatRecyclerViewAdapter.ViewHolder> {

    private final List<Chats> mValues;
    private final DeleteChatFragmentInteractionListener mListener;
    private ArrayList<Chats> checkedChats;

    public DeleteChatRecyclerViewAdapter(List<Chats> items, DeleteChatFragment.DeleteChatFragmentInteractionListener listener) {
        Log.d("DeleteChat", ""+items.size());
        mValues = items;
        mListener = listener;
        checkedChats = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_deletechat, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mContactName.setText(mValues.get(position).getChatName());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (null != mListener) {
//                    // Notify the active callbacks interface (the activity, if the
//                    // fragment is attached to one) that an item has been selected.
//                    mListener.deleteChatFragmentInteraction(holder.mItem);
//                }
//                CheckBox checkBox = (CheckBox) v;
                if (holder.mCheckBox.isChecked()){
                    holder.mCheckBox.setChecked(false);
                    checkedChats.remove(mValues.get(position));
                    Log.d("Checked_Items", "UnChecked: "+mValues.get(position).getNickname());
                } else {
                    holder.mCheckBox.setChecked(true);
                    checkedChats.add(mValues.get(position));
                    Log.d("Checked_Items", "Checked: "+mValues.get(position).getNickname());
                }
            }
        });
    }

    public ArrayList<Chats> getCheckedChats(){
        if (checkedChats.isEmpty()){
            return null;
        }
        return checkedChats;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContactName;
        public final CheckBox mCheckBox;
        public Chats mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mContactName = (TextView) view.findViewById(R.id.chat_delete_contactName);
            mCheckBox = view.findViewById(R.id.chat_delete_checkbox);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContactName.getText() + "'";
        }
    }
}
