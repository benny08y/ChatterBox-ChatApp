package tcss450.uw.edu.chatapp.contacts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnContactListFragmentInteractionListener}
 * interface.
 */
public class ContactsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnContactListFragmentInteractionListener mListener;

    public static final String ARG_CONTACTS_LIST = "contacts list";
    private List<Contacts> mContacts;
    private String mEmail;
    private View view;
    private RecyclerView recyclerView;

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
            mContacts = new ArrayList<>(Arrays.asList((Contacts[]) getArguments().getSerializable("contacts")));
            mEmail = getArguments().getSerializable("email").toString();
        }
        //getContacts();
    }

//    private void getContacts() {
//        Uri uri = new Uri.Builder()
//                .scheme("https")
//                .appendPath(getString(R.string.ep_base_url))
//                .appendPath(getString(R.string.ep_contacts))
//                .appendPath(getString(R.string.ep_contacts_getAllContacts))
//                .build();
//        JSONObject messageJson = new JSONObject();
//        try {
//            messageJson.put("email", mEmail);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        new SendPostAsyncTask.Builder(uri.toString(), messageJson)
//                //.onPreExecute(this::onWaitFragmentInteractionShow)
//                .onPostExecute(this::handleContactsGetOnPostExecute)
//                .onCancelled(error -> Log.e("SEND_TAG", error))
//                .build().execute();
//    }
//
//    private void handleContactsGetOnPostExecute(final String result) {
//        //parse JSON
//        try {
//            JSONObject root = new JSONObject(result);
//            if (root.has("success") && root.getBoolean("success")) {
//                JSONArray data = root.getJSONArray("data");
//                mContacts = new ArrayList<>();
//                for (int i = 0; i < data.length(); i++) {
//                    JSONObject jsonContacts = data.getJSONObject(i);
//                    mContacts.add(new Contacts.Builder(jsonContacts.getString("username"),
//                            jsonContacts.getString("email"))
//                            .addFirstName(jsonContacts.getString("firstname"))
//                            .addLastName(jsonContacts.getString("lastname"))
//                            .build());
//                }
//                //onWaitFragmentInteractionHide();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//            Log.e("ContactsFragment handleContactsGetOnPostExecute: ", e.getMessage());
//            //notify user
//            //onWaitFragmentInteractionHide();
//        }
//    }

//    @Override
//    public void onWaitFragmentInteractionShow() {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.content_home_container, new WaitFragment(), "WAIT")
//                .addToBackStack(null)
//                .commit();
//    }
//
//    @Override
//    public void onWaitFragmentInteractionHide() {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .remove(getActivity().getSupportFragmentManager().findFragmentByTag("WAIT"))
//                .commit();
//    }


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
            recyclerView.setAdapter(new MyContactsRecyclerViewAdapter(mEmail, mContacts, mListener));
        }
//        Button remove = view.findViewById(R.id.contact_button_remove);
//        remove.setOnClickListener(e -> {
//            onRemoveButtonPress();
//        });
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnContactListFragmentInteractionListener) {
            mListener = (OnContactListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnContactListFragmentInteractionListener");
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
    public interface OnContactListFragmentInteractionListener {
        void onContactListFragmentInteraction(Contacts contact);
    }
}
