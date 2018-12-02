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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcss450.uw.edu.chatapp.HomeActivity;
import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.contacts.Contacts;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnNewSingleChatListFragmentInteractionListener}
 * interface.
 */
public class NewChatSingleFragment extends Fragment implements WaitFragment.OnFragmentInteractionListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnNewSingleChatListFragmentInteractionListener mListener;

    public static final String ARG_NEWSINGLE_CHAT = "list of contacts";
    private List<Contacts> mContactsList;
    private String mEmail;
    private boolean newGroupChatSelected=false;
    private NewChatRecyclerViewAdapter mNewChatAdapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NewChatSingleFragment() {    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NewChatSingleFragment newInstance(int columnCount) {
        NewChatSingleFragment fragment = new NewChatSingleFragment();
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
            mContactsList = new ArrayList<Contacts>(
                    Arrays.asList((Contacts[]) getArguments().getSerializable(ARG_NEWSINGLE_CHAT)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_chat_single_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mNewChatAdapter = new NewChatRecyclerViewAdapter(mContactsList, mListener);
            recyclerView.setAdapter(mNewChatAdapter);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_new_chats, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public void onPrepareOptionsMenu (Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (newGroupChatSelected) {
            menu.findItem(R.id.menu_new_chat_group_icon).setVisible(false);
            menu.findItem(R.id.menu_new_chat_single_icon).setVisible(true);
            menu.findItem(R.id.menu_new_chat_cleargroup).setVisible(true);
            menu.findItem(R.id.menu_newchat_comfirmGroup).setVisible(true);
        } else {
            menu.findItem(R.id.menu_new_chat_group_icon).setVisible(true);
            menu.findItem(R.id.menu_new_chat_single_icon).setVisible(false);
        }
//        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean result = false;
        getActivity().invalidateOptionsMenu();
        if (id == R.id.menu_new_chat_group_icon) {
            newGroupChatSelected = true;
            mNewChatAdapter.setCheckBoxes(true);
            result = true;
        } else if (id == R.id.menu_newchat_comfirmGroup){
            newGroupChatSelected = true;        //get list of contacts and call endpoint to make new group chat
            if (mNewChatAdapter.getCheckedContacts() == null){
                Snackbar.make(getView(), "Please select 1 or more contacts", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            } else {
                ArrayList<Contacts> listOfContacts = mNewChatAdapter.getCheckedContacts();
                Uri uri = new Uri.Builder()
                        .scheme("https")
                        .appendPath(getString(R.string.ep_base_url))
                        .appendPath(getString(R.string.ep_chats_base))
                        .appendPath(getString(R.string.ep_new_group_chat))
                        .build();
                JSONObject messageJson = new JSONObject();

                String[] contactsArray = new String[listOfContacts.size()];
                JSONArray jsonArray = new JSONArray();
                jsonArray.put(mEmail);
                String groupName = "";
                for (int i =0; i < listOfContacts.size(); i++){
                    groupName += listOfContacts.get(i).getNickname() + " ";
                    jsonArray.put(listOfContacts.get(i).getEmail());
                }
//                for (int i =0; i < listOfContacts.size(); i++){
                try {
                    messageJson.put("chatName", "Group: " + groupName);  //NEED TO CREATE UNIQUE group chatname
                    messageJson.put("emaillist", jsonArray);
                } catch (JSONException e) {
                    Log.d("NEWCHAT", "array wrong");
                    e.printStackTrace();
                }
                new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                        .onPreExecute(this::onWaitFragmentInteractionShow)
                        .onPostExecute(this::handleNewChat)
                        .onCancelled(error -> Log.e("SEND_TAG", error))
                        .build().execute();
//                }
            }

            result = true;
        } else if (id == R.id.menu_new_chat_single_icon){
            newGroupChatSelected = false;
            mNewChatAdapter.setCheckBoxes(false);
            result = true;
        } else if (id == R.id.menu_new_chat_cleargroup){
            newGroupChatSelected = true;
            mNewChatAdapter.setEnabledCheckBoxes(true);
            result = true;
        }
        mNewChatAdapter.notifyDataSetChanged();
        return result;
    }

    private void handleNewChat(final String result) {
        try {
            JSONObject root = new JSONObject(result);
            Log.d("NEWCHAT", "Should be true: "+root.getBoolean("success"));
            Log.d("NEWCHAT", "Result of post: "+result);
            if (root.has("success") && root.getBoolean("success")) {
                String chatName = root.getString("chatname");
                chatName.replace(mEmail, "");
                int chatID = root.getInt("chatid");
//                JSONArray data = root.getJSONArray("data");
//                for (int i =0; i < data.length(); i++){
//                    JSONObject currContact = data.getJSONObject(i);
//                    String email = currContact.getString("email");
//                    String nickname = currContact.getString("username");
//                    String firstname = currContact.getString("firstname");
//                    String lastname = currContact.getString("lastname");
//                }
                MessageFragment messageFragment = new MessageFragment();
                Bundle args = new Bundle();
                args.putString(HomeActivity.MESSAGE_NICKNAME, chatName);
                args.putInt(HomeActivity.MESSAGE_CHATID, chatID);
                messageFragment.setArguments(args);
                onWaitFragmentInteractionHide();
                loadFragment(messageFragment);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("NEWCHAT", "ERROR: "+e.getMessage());
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
        if (context instanceof OnNewSingleChatListFragmentInteractionListener) {
            mListener = (OnNewSingleChatListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnNewSingleChatListFragmentInteractionListener");
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
    public interface OnNewSingleChatListFragmentInteractionListener {
        // TODO: Update argument type and name
        void newSingleChatFragmentInteraction(Contacts item);
    }
}
