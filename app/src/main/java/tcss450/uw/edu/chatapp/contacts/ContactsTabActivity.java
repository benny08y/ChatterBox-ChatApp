package tcss450.uw.edu.chatapp.contacts;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import tcss450.uw.edu.chatapp.R;
import tcss450.uw.edu.chatapp.chats.MessageFragment;
import tcss450.uw.edu.chatapp.model.Contacts;

public class ContactsTabActivity extends AppCompatActivity implements
        ContactPageFragment.OnContactPageFragmentInteractionListener,
        ContactsFragment.OnContactListFragmentInteractionListener,
        SearchContactsFragment.OnSearchContactsFragmentInteractionListener {

    private ContactsPagerAdapter mContactsPagerAdapter;
    private ViewPager mViewPager;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Bundle args = intent.getExtras();
        mEmail = args.getSerializable("email").toString();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mContactsPagerAdapter = new ContactsPagerAdapter(getSupportFragmentManager(),
                mEmail,
                (Contacts[]) args.getSerializable("contacts"));
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.contacts_pager);
        mViewPager.setAdapter(mContactsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_tab, menu);
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

    @Override
    public void onContactPageFragmentInteraction(String name) {
        MessageFragment messageFragment = new MessageFragment();
//        messageFragment.setName(name);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, messageFragment)
                .addToBackStack(null)
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
                .replace(R.id.main_content, contactPageFragment) //incorrect container id?
                .addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSearchContactsFragmentInteraction(Contacts contact) {

    }
}
