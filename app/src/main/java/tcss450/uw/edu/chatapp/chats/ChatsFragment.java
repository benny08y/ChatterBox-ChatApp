package tcss450.uw.edu.chatapp.chats;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.contacts.Contacts;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnChatListFragmentInteractionListener}
 * interface.
 */
public class ChatsFragment extends Fragment  implements WaitFragment.OnFragmentInteractionListener {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnChatListFragmentInteractionListener mListener;

    public static final String ARG_CHATS = "list of chats";
    private List<Chats> mChatsList;

    private FloatingActionButton mFAB;
    private ArrayList<Contacts> mContactsList;
    private MyChatsRecyclerViewAdapter mChatsAdapters;
    private Menu mMenu;
    private String mEmail;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ChatsFragment() {    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ChatsFragment newInstance(int columnCount) {
        ChatsFragment fragment = new ChatsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    public void setContacts(ArrayList<Contacts> contacts){
        mContactsList = contacts;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEmail = getArguments().getString("email");
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
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleChatsPostExecute)
                .onCancelled(error -> Log.e("SEND_TAG", error))
                .build().execute();
        mFAB = (FloatingActionButton) this.getActivity().findViewById(R.id.fab);
        mFAB.show();
        mFAB.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_chat_black_24dp));
        mFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mChatsList = new ArrayList<Chats>(
                    Arrays.asList((Chats[]) getArguments().getSerializable(ARG_CHATS)));
        }
    }
    private void handleChatsPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.getBoolean("success")) {

                JSONArray data = root.getJSONArray("data");
                ArrayList<Chats> chatList = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonChats = data.getJSONObject(i);
                    chatList.add(new Chats.Builder(jsonChats.getString("email"),
                            jsonChats.getString("firstname"), jsonChats.getString("lastname"))
                            .addChatID(jsonChats.getInt("chatid"))
                            .addNickname(jsonChats.getString("username"))
                            .build());
                }
                Chats[] chatsAsArray = new Chats[chatList.size()];
                chatsAsArray = chatList.toArray(chatsAsArray);
                Bundle args = new Bundle();
                args.putSerializable(ChatsFragment.ARG_CHATS, chatsAsArray);
                ChatsFragment chatFrag = new ChatsFragment();
                chatFrag.setArguments(args);
                onWaitFragmentInteractionHide();
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            mChatsAdapters = new MyChatsRecyclerViewAdapter(mChatsList, mListener);
            recyclerView.setAdapter(mChatsAdapters);
        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        mMenu = menu;
        inflater.inflate(R.menu.chats_delete, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        boolean result = false;
        if(id == R.id.menu_delete_chats){
            //open DeleteChatFragment
            mFAB.hide();
            Chats[] chatsAsArray = new Chats[mChatsList.size()];
            chatsAsArray = mChatsList.toArray(chatsAsArray);
            Bundle args = new Bundle();
            args.putSerializable(DeleteChatFragment.ARG_CHATS, chatsAsArray);
            DeleteChatFragment deleteChatFragment = new DeleteChatFragment();;
            deleteChatFragment.setArguments(args);
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.content_home_container, deleteChatFragment)
                    .addToBackStack(null)
                    .commit();
            result = true;
        }
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnChatListFragmentInteractionListener) {
            mListener = (OnChatListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnChatListFragmentInteractionListener");
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
    public interface OnChatListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onChatListFragmentInteraction(Chats item);
    }
}
