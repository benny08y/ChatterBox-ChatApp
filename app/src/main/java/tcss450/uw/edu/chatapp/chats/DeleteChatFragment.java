package tcss450.uw.edu.chatapp.chats;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import tcss450.uw.edu.chatapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link DeleteChatFragmentInteractionListener}
 * interface.
 */
public class DeleteChatFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private DeleteChatFragmentInteractionListener mListener;

    public static final String ARG_CHATS = "list of chats";
    private List<Chats> mChatsList;

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
            recyclerView.setAdapter(new DeleteChatRecyclerViewAdapter(mChatsList, mListener));
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

            result = true;
        }
        return result;
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
