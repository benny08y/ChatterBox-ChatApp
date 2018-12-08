package tcss450.uw.edu.chatapp.contacts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.model.Contacts;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;

/**
 * Aaron Bardsley
 *
 * This fragment is for handling existing contacts
 */
public class ContactsFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;

    public static final String ARG_CONTACTS_LIST = "contacts list";
    private List<Contacts> mContacts;
    private String mEmail;
    private View view;
    private RecyclerView recyclerView;
    private MyContactsRecyclerViewAdapter mAdapter;
    private SwipeRefreshLayout mSwipeContainer;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ContactsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ContactsFragment newInstance(int columnCount) {
        ContactsFragment fragment = new ContactsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mContacts = new ArrayList<>(Arrays.asList((Contacts[]) getArguments().getSerializable("contacts")));
            mEmail = getArguments().getSerializable("email").toString();
        }
        //getContacts();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_contacts_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            getContacts();

//            mSwipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.contacts_swipe);
//            mSwipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//                @Override
//                public void onRefresh() {
//                    getContacts();
//                    mAdapter.clear();
//                    mAdapter.addAll(mContacts);
//                    mSwipeContainer.setRefreshing(false);
//                }
//            });
        }

        return view;
    }

    /**
     * Aaron Bardsley
     *
     * end point: contacts/getAllContacts
     * retrieve all existing contacts associated with the user.
     */
    private void getContacts() {
        //Log.e("CONTACTS FRAGMENT TAB", "HIT GET-CONTACTS");
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendPath(getString(R.string.ep_contacts_getAllContacts))
                .build();
        //Log.e("CONTACTS FRAGMENT TAB", "URI EP: " + uri.toString());
        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("email", mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("CONTACTS FRAGMENT TAB", "SENDING ASNYC mEmail: " + mEmail);
        new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                //.onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleContactsGetOnPostExecute)
                .onCancelled(error -> Log.e("SEND_TAG", error))
                .build().execute();
    }

    /**
     * Aaron Bardsley
     *
     * Build a RecyclerView to display all existing contacts for the user
     */
    private void handleContactsGetOnPostExecute(final String result) {
        //Log.e("CONTACTS FRAGMENT TAB", "HIT POST-EXECUTE");
        //parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.getBoolean("success")) {
                //Log.e("CONTACTS FRAGMENT TAB", "SUCCESS");
                JSONArray data = root.getJSONArray("data");
                mContacts = new ArrayList<>();
                for (int i = 0; i < data.length(); i++) {
                    JSONObject jsonContacts = data.getJSONObject(i);
                    mContacts.add(new Contacts.Builder(jsonContacts.getString("username"),
                            jsonContacts.getString("email"))
                            .addFirstName(jsonContacts.getString("firstname"))
                            .addLastName(jsonContacts.getString("lastname"))
                            .build());
                }
                //onWaitFragmentInteractionHide();
                mAdapter = new MyContactsRecyclerViewAdapter(recyclerView, mEmail, mContacts);
                recyclerView.setAdapter(mAdapter);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ContactsFragment handleContactsGetOnPostExecute: ", e.getMessage());
            //notify user
            //onWaitFragmentInteractionHide();
        }
    }

}
