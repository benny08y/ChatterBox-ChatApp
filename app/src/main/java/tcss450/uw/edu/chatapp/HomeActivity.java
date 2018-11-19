package tcss450.uw.edu.chatapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.chatapp.chats.Chats;
import tcss450.uw.edu.chatapp.chats.ChatsFragment;
import tcss450.uw.edu.chatapp.contacts.ContactPageFragment;
import tcss450.uw.edu.chatapp.contacts.Contacts;
import tcss450.uw.edu.chatapp.contacts.ContactsFragment;
import tcss450.uw.edu.chatapp.utils.GetAsyncTask;
import tcss450.uw.edu.chatapp.chats.MessageFragment;
import tcss450.uw.edu.chatapp.utils.MyFirebaseMessagingService;
import tcss450.uw.edu.chatapp.utils.SendPostAsyncTask;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

public class HomeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        ChatsFragment.OnChatListFragmentInteractionListener,
        ContactsFragment.OnContactListFragmentInteractionListener,
        WaitFragment.OnFragmentInteractionListener,
        ContactPageFragment.OnContactPageFragmentInteractionListener {

    private FloatingActionButton mFab;
    Bundle thisBundle;
    private String mEmail;
    private ArrayList<Chats> mChats;
    private String mSender;
    private FirebaseMessageReciever mFirebaseMessageReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFab.hide();
                getContacts();
            }
        });
        mFab.hide();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("args");
        mEmail = intent.getStringExtra(MainActivity.HOME_LOGIN_EMAIL);
        Log.v("EMAIL", mEmail);
        thisBundle = bundle;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView t = (TextView) headerView.findViewById(R.id.header_curEmail);
        t.setText(mEmail);

        LandingPageFragment landingPageFragment = new LandingPageFragment();
        landingPageFragment.setArguments(bundle);

        if(savedInstanceState == null) {
            if (findViewById(R.id.content_home_container) != null) {
                Fragment fragment;
                if (getIntent().getBooleanExtra(getString(R.string.keys_intent_notifification_msg), false)) {
                    fragment = new LandingPageFragment();
//                    for (int i =0 ; i< mChats.size(); i++){
//                        Chats currChat = mChats.get(i);
//                        if(currChat.getEmail().equals(mSender)){
//                            mFab.hide();
//                            MessageFragment messageFragment = new MessageFragment();
//                            messageFragment.setChat(currChat);
//                            messageFragment.setName(currChat.getNickname()+ " (" +currChat.getFirstname()+" "+currChat.getLastname()+")");
//                            getSupportFragmentManager()
//                                    .beginTransaction()
//                                    .replace(R.id.content_home_container, messageFragment)
//                                    .addToBackStack(null)
//                                    .commit();
//                        }
//                    }
                } else {
                    fragment = new LandingPageFragment();
                    fragment.setArguments(bundle);
                }
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_home_container, fragment)
                        .commit();
            }
        }
    }

    private void getContacts(){
        Uri uri = new Uri.Builder()
                .scheme("https")
                .appendPath(getString(R.string.ep_base_url))
                .appendPath(getString(R.string.ep_contacts))
                .appendPath(getString(R.string.ep_contacts_getAllContacts))
                .build();
        JSONObject messageJson = new JSONObject();
        try {
            messageJson.put("email", mEmail);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                .onPreExecute(this::onWaitFragmentInteractionShow)
                .onPostExecute(this::handleContactsGetOnPostExecute)
                .onCancelled(error -> Log.e("SEND_TAG", error))
                .build().execute();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mFab.hide();
            loadFragment(new LandingPageFragment());
        } else if (id == R.id.nav_chat) {
            Uri uri = new Uri.Builder()
                    .scheme("https")
                    .appendPath(getString(R.string.ep_base_url))
                    .appendPath(getString(R.string.ep_chats_base))
                    .appendPath(getString(R.string.ep_getallchats))
                    .build();
            JSONObject messageJson = new JSONObject();
            try {
                messageJson.put("email", mEmail);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new SendPostAsyncTask.Builder(uri.toString(), messageJson)
                    .onPreExecute(this::onWaitFragmentInteractionShow)
                    .onPostExecute(this::handleChatsPostExecute)
                    .onCancelled(error -> Log.e("SEND_TAG", error))
                    .build().execute();
        } else if (id == R.id.nav_contacts) {
            mFab.hide();
            getContacts();
        } else if (id == R.id.nav_logout) {
            logout();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleContactsGetOnPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.getBoolean("success")) {
                JSONArray data = root.getJSONArray("data");
                List<Contacts> contacts = new ArrayList<>();
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonContacts = data.getJSONObject(i);
                    contacts.add(new Contacts.Builder(jsonContacts.getString("username"),
                        jsonContacts.getString("email"))
                        .addFirstName(jsonContacts.getString("firstname"))
                        .addLastName(jsonContacts.getString("lastname"))
                        .build());
                }
                Contacts[] contactsAsArray = new Contacts[contacts.size()];
                contactsAsArray = contacts.toArray(contactsAsArray);
                Bundle args = new Bundle();
                args.putSerializable(ContactsFragment.ARG_CONTACTS_LIST, contactsAsArray);
                Fragment frag = new ContactsFragment();
                frag.setArguments(args);
                onWaitFragmentInteractionHide();
                loadFragment(frag);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    private void handleChatsPostExecute(final String result) {
        //parse JSON
        try {
            JSONObject root = new JSONObject(result);
            if (root.has("success") && root.getBoolean("success")) {

                JSONArray data = root.getJSONArray("data");
                mChats = new ArrayList<>();
                for(int i = 0; i < data.length(); i++) {
                    JSONObject jsonChats = data.getJSONObject(i);
                    mChats.add(new Chats.Builder(jsonChats.getString("email"),
                    jsonChats.getString("firstname"), jsonChats.getString("lastname"))
                            .addChatID(jsonChats.getInt("chatid"))
                            .addNickname(jsonChats.getString("username"))
                    .build());
                }
                Chats[] chatsAsArray = new Chats[mChats.size()];
                chatsAsArray = mChats.toArray(chatsAsArray);
                Bundle args = new Bundle();
                args.putSerializable( ChatsFragment.ARG_CHATS , chatsAsArray);
                ChatsFragment frag = new ChatsFragment();
                frag.setArguments(args);
                frag.setFab(mFab);
                onWaitFragmentInteractionHide();
                loadFragment(frag);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e("ERROR!", e.getMessage());
            //notify user
            onWaitFragmentInteractionHide();
        }
    }

    private void logout() {
        SharedPreferences prefs =
                getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        //remove the saved credentials from StoredPrefs
        prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
        prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();
        //close the app
//        finishAndRemoveTask();
        //or close this activity and bring back the Login
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
        //End this Activity and remove it from the Activity back stack.
        finish();
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_home_container, frag)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onChatListFragmentInteraction(Chats item) {
        mFab.hide();
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setChat(item);
        messageFragment.setName(item.getNickname()+ " (" +item.getFirstname()+" "+item.getLastname()+")");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_home_container, messageFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.content_home_container, new WaitFragment(), "WAIT")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onWaitFragmentInteractionHide() {
        getSupportFragmentManager()
                .beginTransaction()
                .remove(getSupportFragmentManager().findFragmentByTag("WAIT"))
                .commit();
    }

    @Override
    public void onContactListFragmentInteraction(Contacts contact) {
        ContactPageFragment contactPageFragment = new ContactPageFragment();
        contactPageFragment.setContacts(contact);
        Bundle args = new Bundle();
        args.putString("nickname", contact.getNickname());
        args.putString("email", contact.getEmail());
        args.putString("firstName", contact.getFirstName());
        args.putString("lastName", contact.getLastName());
        args.putString("currEmail", mEmail);

        contactPageFragment.setArguments(args);
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_home_container, contactPageFragment)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onContactPageFragmentInteraction(String name) {
        MessageFragment messageFragment = new MessageFragment();
        messageFragment.setName(name);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_home_container, messageFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mFirebaseMessageReciever == null) {
            mFirebaseMessageReciever = new FirebaseMessageReciever();
        }
        IntentFilter iFilter = new IntentFilter(MyFirebaseMessagingService.RECEIVED_NEW_MESSAGE);
        registerReceiver(mFirebaseMessageReciever, iFilter);
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mFirebaseMessageReciever != null){
            unregisterReceiver(mFirebaseMessageReciever);
        }
    }

    // Deleting the InstanceId (Firebase token) must be done asynchronously. Good thing
    // we have something that allows us to do that.
    class DeleteTokenAsyncTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            //since we are already doing stuff in the background, go ahead
            //and remove the credentials from shared prefs here.
            SharedPreferences prefs =
                    getSharedPreferences(
                            getString(R.string.keys_shared_prefs),
                            Context.MODE_PRIVATE);
            prefs.edit().remove(getString(R.string.keys_prefs_password)).apply();
            prefs.edit().remove(getString(R.string.keys_prefs_email)).apply();
            try {
                //this call must be done asynchronously.
                FirebaseInstanceId.getInstance().deleteInstanceId();
            } catch (IOException e) {
                Log.e("FCM", "Delete error!");
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //close the app
            finishAndRemoveTask();
            //or close this activity and bring back the Login
             Intent i = new Intent(HomeActivity.this, MainActivity.class);
             startActivity(i);
             //Ends this Activity and removes it from the Activity back stack.
//             finish();
        }
    }

    private class FirebaseMessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("FCM Chat Frag", "start onRecieve");
            if(intent.hasExtra("DATA")) {
                String data = intent.getStringExtra("DATA");
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(data);
                    if(jObj.has("message") && jObj.has("sender")) {
                        mSender = jObj.getString("sender");
                        String msg = jObj.getString("message");
                        Log.i("FCM Chat Frag", mSender + " " + msg);
                    }
                } catch (JSONException e) {
                    Log.e("JSON PARSE", e.toString());
                }
            }
        }
    }
}
