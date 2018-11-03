package tcss450.uw.edu.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import tcss450.uw.edu.chatapp.chats.Chats;
import tcss450.uw.edu.chatapp.chats.ChatsFragment;
import tcss450.uw.edu.chatapp.chats.MessageFragment;
import tcss450.uw.edu.chatapp.chats.dummy.DummyContent;
import tcss450.uw.edu.chatapp.contacts.Contacts;
import tcss450.uw.edu.chatapp.contacts.ContactsFragment;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ChatsFragment.OnChatListFragmentInteractionListener,
        ContactsFragment.OnListFragmentInteractionListener, MessageFragment.OnMessageListFragmentInteractionListener {

    private LandingPageFragment mLandPageFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        Intent intent = getIntent();

        if(savedInstanceState == null) {
            if (findViewById(R.id.content_home_container) != null) {
                mLandPageFrag = new LandingPageFragment();
                Bundle bundle = new Bundle();
                mLandPageFrag.setArguments(bundle);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.content_home_container, mLandPageFrag)
                        .commit();
            }
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            loadFragment(mLandPageFrag);
        } else if (id == R.id.nav_chat) {
            loadFragment(new ChatsFragment());
        } else if (id == R.id.nav_contacts) {
            loadFragment(new ContactsFragment());
        } else if (id == R.id.nav_logout) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void loadFragment(Fragment frag) {
        FragmentTransaction transaction = getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_home_container, frag)
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onChatListFragmentInteraction(Chats.DummyItem item) {
        MessageFragment messageFragment = new MessageFragment();

        // TODO: use chat item to find and generate previous Message items, if any

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_home_container, messageFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onListFragmentInteraction(Contacts.DummyItem item) {

    }

    @Override
    public void onMessageListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
