package tcss450.uw.edu.chatapp.contacts;

import android.content.Intent;
import android.net.Uri;
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

/**
 * Aaron Bardsley
 *
 * This activity is for giving the user a tab layout flow for contacts
 */
public class ContactsTabActivity extends AppCompatActivity {

    private ContactsPagerAdapter mContactsPagerAdapter;
    private ViewPager mViewPager;
    private String mEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_tab);

        Intent intent = getIntent();
        Bundle intentExtras = intent.getExtras();
        mEmail = intentExtras.getSerializable("email").toString();

        Toolbar toolbar = (Toolbar) findViewById(R.id.contacts_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Create the adapter that will return a fragment for each of the
        // primary sections of the activity.
        mContactsPagerAdapter = new ContactsPagerAdapter(getSupportFragmentManager(),
                mEmail,
                (Contacts[]) intentExtras.getSerializable("contacts"));
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.contacts_pager);
        mViewPager.setAdapter(mContactsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


//        ContactsViewPager contactsViewPager = new ContactsViewPager();
//        Bundle args = new Bundle();
//        args.putSerializable("email", mEmail);
//        args.putSerializable("contacts", (Contacts[]) intentExtras.getSerializable("contacts"));
//        contactsViewPager.setArguments(args);
//
//        getSupportFragmentManager().beginTransaction()
//                .add(R.id.contacts_container, contactsViewPager)
//                .commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts_tab, menu);
        return true;
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

}
