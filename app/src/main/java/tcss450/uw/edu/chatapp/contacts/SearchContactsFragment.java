package tcss450.uw.edu.chatapp.contacts;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class SearchContactsFragment extends Fragment { //implements WaitFragment.OnFragmentInteractionListener {
    private int mColumnCount = 1;
    private OnSearchContactsFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private View view;
    private List<Contacts> mContacts;
    private String mEmail;

    public SearchContactsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mContacts = new ArrayList<>(
                    Arrays.asList((Contacts[]) getArguments().getSerializable("contacts")));
            mEmail = getArguments().getSerializable("email").toString();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_contacts, container, false);
        // Set the adapter
        //if (view instanceof RecyclerView) {
            Context context = view.getContext();
            //recyclerView = (RecyclerView) view;
        recyclerView = (RecyclerView) view.findViewById(R.id.Search_Contacts_List);
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
       // }
        Button button = (Button) view.findViewById(R.id.Search_Contacts_Button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(v);
            }
        });
        return view;
    }


    private void onButtonClick(View v) {
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendPath(getString(R.string.ep_contacts_search_email))
                .build();
        JSONObject messageJson = new JSONObject();
        Bundle bundle = getArguments();
        EditText input = view.findViewById(R.id.search_contact_email);
        String stringInput = input.getText().toString();
        //String Email = bundle.getString(stringInput);
        //String Email = "cjkim00@gmail.com";

        if (!stringInput.isEmpty()) {
            try {
                messageJson.put("searchEmail", stringInput);

            } catch (JSONException e) {
                input.setError("Enter a valid email");
                Log.e("IN_JSON", "didnt put email");
            }
            new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                    //.onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleContactsGetOnPostExecute)
                    .onCancelled(error -> Log.e("SEND_TAG", error))
                    .build().execute();
        } else {
            input.setError("Enter a valid email");
        }
    }

    private void handleContactsGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);

                Log.d("SearchContactsFragment: ", "Successfully sent");
                JSONArray members = root.getJSONArray("data");
                ArrayList<Contacts> membersArray = new ArrayList<>();
                for(int i = 0; i < members.length(); i++) {
                    JSONObject search = members.getJSONObject(i);
                    membersArray.add(new Contacts.Builder(search.getString("username"),
                            search.getString("email"))
                            .addFirstName(search.getString("firstname"))
                            .addLastName(search.getString("lastname"))
                            .build());
                }

                Contacts[] resultsArray = new Contacts[membersArray.size()];
                //resultsArray = membersArray.toArray(resultsArray);
                List<Contacts> results = membersArray;
                recyclerView.setAdapter(new SearchContactFragmentRecyclerViewAdapter(results, mListener));

            //onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR in handleContactsGetOnPostExecute: ", e.getMessage());
            //notify user
            //onWaitFragmentInteractionHide();
        }
    }

//    @Override
//    public void onWaitFragmentInteractionShow() {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.content_home_container, new WaitFragment(), "WAIT")
//                .addToBackStack(null)
//                .commit();
//    }
//    @Override
//    public void onWaitFragmentInteractionHide() {
//        getActivity().getSupportFragmentManager()
//                .beginTransaction()
//                .remove(getActivity().getSupportFragmentManager().findFragmentByTag("WAIT"))
//                .commit();
//    }
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
        if (context instanceof OnSearchContactsFragmentInteractionListener) {
            mListener = (OnSearchContactsFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnSearchContactsFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchContactsFragmentInteraction(Contacts contact);
    }
}
