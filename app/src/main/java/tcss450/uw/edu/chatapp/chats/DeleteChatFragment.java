package tcss450.uw.edu.chatapp.chats;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.contacts.Contacts;
import tcss450.uw.edu.chatapp.contacts.ContactsFragment;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link DeleteChatFragmentInteractionListener}
 * interface.
 */
public class DeleteChatFragment extends Fragment implements WaitFragment.OnFragmentInteractionListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private DeleteChatFragmentInteractionListener mListener;

    public static final String ARG_CHATS = "list of chats";
    private List<Chats> mChatsList;
    private DeleteChatRecyclerViewAdapter mDeleteChatAdapter;
    private String mEmail;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DeleteChatFragment() {   }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static DeleteChatFragment newInstance(int columnCount) {
        DeleteChatFragment fragment = new DeleteChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mEmail = getArguments().getString("email");
        if (getArguments() != null) {
            mChatsList = new ArrayList<Chats>(
                    Arrays.asList((Chats[]) getArguments().getSerializable(ARG_CHATS)));
            Log.d("DeleteChat", mChatsList.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_deletechat_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mDeleteChatAdapter = new DeleteChatRecyclerViewAdapter(mChatsList, mListener);
            recyclerView.setAdapter(mDeleteChatAdapter);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.chats_delete_cancel, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean result = false;
        if(id == R.id.menu_delete_chats_cancel){
            //open ChatsFragment
            Chats[] chatsAsArray = new Chats[mChatsList.size()];
            chatsAsArray = mChatsList.toArray(chatsAsArray);
            Bundle args = new Bundle();
            args.putSerializable(ChatsFragment.ARG_CHATS, chatsAsArray);
            ChatsFragment chatsFragment = new ChatsFragment();;
            chatsFragment.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_home_container, chatsFragment)
                    .addToBackStack(null)
                    .commit();
            result = true;
        } else if(id == R.id.menu_delete_icon){
            if (mDeleteChatAdapter.getCheckedChats() == null){
                Snackbar.make(getView(), "Please select 1 or more chats to delete", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setCancelable(true);
                builder.setTitle("Are you sure you want to delete these chat(s)?");
                builder.setMessage("This will delete all messages in the chat(s).");
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteChats();
                    }
                });
                builder.show();
            }
            result = true;
        }
        return result;
    }
    private void deleteChats(){
        ArrayList<Chats> checkedChats=null;
        if (mDeleteChatAdapter.getCheckedChats() != null){
            checkedChats = mDeleteChatAdapter.getCheckedChats();
            Log.d("Checked_Items", checkedChats.get(0).getChatID()+"");
            // make call to delete chat endpoint
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_chats_base))
                    .appendPath(getString(R.string.ep_delete_chats))
                    .build();
            onWaitFragmentInteractionShow();
            for (int i = 0; i < checkedChats.size(); i++){
                JSONObject messageJson = new JSONObject();
                try {
                    Log.d("deleteCHAT", "post body email"+ checkedChats.get(i).getChatID());
                    messageJson.put("chatid", checkedChats.get(i).getChatID());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                        .onPostExecute(this::handleDeleteChatPostExecute)
                        .onCancelled(error -> Log.e("SEND_TAG", error))
                        .build().execute();
            }

        }
    }
    private void handleDeleteChatPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject root = new JSONObject(result);
            Log.d("deleteCHAT", "Success boolean: "+root.getBoolean("success"));
            if (root.has("success") && root.getBoolean("success")) {
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_chats_base))
                        .appendPath(getString(R.string.ep_getallchats))
                        .build();
                JSONObject messageJson = new JSONObject();
                try {
                    messageJson.put("email", mEmail);
                    Log.e("IN_JSON", "post body email");
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.e("IN_JSON", "didnt put email");
                }
                new SendPostAsyncTask.Builder(uri.toString(), messageJson)
//                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleChatsPostExecute)
                        .onCancelled(error -> Log.e("SEND_TAG", error))
                        .build().execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }
    private void handleChatsPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.getBoolean("success")) {

                JSONArray data = root.getJSONArray("data");
                ArrayList<Chats> chatList = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonChats = data.getJSONObject(i);
                    chatList.add(new Chats.Builder("",
                            "", "")
                            .addChatID(jsonChats.getInt("chatid"))
                            .addChatName(jsonChats.getString("name"))
                            .build());
                }
                Chats[] chatsAsArray = new Chats[chatList.size()];
                chatsAsArray = chatList.toArray(chatsAsArray);
                Bundle args = new Bundle();
                args.putString("email", mEmail);
                args.putSerializable(ChatsFragment.ARG_CHATS, chatsAsArray);
                ChatsFragment chatFrag = new ChatsFragment();
                chatFrag.setArguments(args);
                onWaitFragmentInteractionHide();
                loadFragment(chatFrag);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }
    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_home_container, frag)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DeleteChatFragmentInteractionListener) {
            mListener = (DeleteChatFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DeleteChatFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_home_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void onWaitFragmentInteractionHide() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .remove(getActivity().getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface DeleteChatFragmentInteractionListener {
        // TODO: Update argument type and name
        void deleteChatFragmentInteraction(Chats item);
    }
}
