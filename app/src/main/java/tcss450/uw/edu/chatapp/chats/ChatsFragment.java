package tcss450.uw.edu.chatapp.chats;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.contacts.Contacts;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnChatListFragmentInteractionListener}
 * interface.
 */
public class ChatsFragment extends Fragment  {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnChatListFragmentInteractionListener mListener;

    public static final String ARG_CHATS = "list of chats";
    private List<Chats> mChatsList;

    private FloatingActionButton mFAB;
    private ArrayList<Contacts> mContactsList;
    private MyChatsRecyclerViewAdapter mChatsAdapters;
    private Menu mMenu;


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
