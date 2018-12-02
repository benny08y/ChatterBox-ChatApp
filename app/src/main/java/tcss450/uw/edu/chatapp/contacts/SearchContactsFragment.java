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
import java.util.List;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.model.Contacts;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class SearchContactsFragment extends Fragment implements WaitFragment.OnFragmentInteractionListener {
    private int mColumnCount = 1;
    private OnFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    View view;
    public SearchContactsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_contacts, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.Search_Contacts_List);
        if(mColumnCount <= 1) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));
        }
        List<Contacts> results = new ArrayList<>();
        //recyclerView.setAdapter(new SearchContactFragmentRecyclerViewAdapter(results, mListener));
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
        try {
            messageJson.put("searchEmail", stringInput);
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("IN_JSON", "didnt put email");
        }

        new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleContactsGetOnPostExecute)
                .onCancelled(error -> Log.e("SEND_TAG", error))
                .build().execute();
    }

    private void handleContactsGetOnPostExecute(final String result) {
        try {
            JSONObject root = new JSONObject(result);

                Log.e("Successfully sent", "Success");
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

            onWaitFragmentInteractionHide();
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
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
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onSearchContactsFragmentInteraction(Contacts contact);
    }
}
