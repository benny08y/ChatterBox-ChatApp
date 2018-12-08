package tcss450.uw.edu.chatapp.contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.chatapp.model.Contacts;

/**
 * Aaron Bardsley
 *
 * This class is for handling the tab fragments for the Tabbed Activity for contacts
 */
public class ContactsPagerAdapter extends FragmentPagerAdapter {

    private int mTabCount;
    private String mEmail;
    private Contacts[] mContacts;
    private Contacts[] mRequests;
    private Fragment mFragmentAtPos0;

    public ContactsPagerAdapter(FragmentManager fm, String email, Contacts[] contacts) {
        super(fm);
        mTabCount = 4;
        mEmail = email;
        mContacts = contacts;
        //mRequests = requests;
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

//    @Override
//    public CharSequence getPageTitle(int position) {
//        switch(position) {
//            case 0:
//                return "Contacts";
//            case 1:
//                return "Add Contact";
//            case 2:
//                return "Pending Requests";
//            default:
//                return null;
//        }
//    }

//    public Fragment getFragmentAtPos0() {
//        return mFragmentAtPos0;
//    }
//
//    public void setFragmentAtPos0(Fragment f) {
//        mFragmentAtPos0 = f;
//    }
//
//    @Override
//    public int getItemPosition(Object object) {
//        if (object instanceof ContactsFragment && mFragmentAtPos0 instanceof ContactPageFragment) {
//            return POSITION_NONE;
//        }
//        return POSITION_UNCHANGED;
//    }

    /**
     * Aaron Bardsley
     *
     * Returns a fragment at the given position, used for displaying tabs
     */
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                //if (mFragmentAtPos0 == null) {
                    Fragment contactsFragment = new ContactsFragment();
                    Bundle args = new Bundle();
                    args.putSerializable("email", mEmail);
                    args.putSerializable("contacts", mContacts);
                    contactsFragment.setArguments(args);

                    mFragmentAtPos0 = contactsFragment;
                //}
                return mFragmentAtPos0;
            case 1:
                Fragment searchContacts = new SearchContactsFragment();
                Bundle args1 = new Bundle();
                args1.putSerializable("email", mEmail);
                args1.putSerializable("contacts", mContacts);
                searchContacts.setArguments(args1);
                return searchContacts;
            case 2:
                Fragment requestsInbox = new ContactRequestsInbox();
                Bundle args2 = new Bundle();
                args2.putSerializable("email", mEmail);
                //args2.putSerializable("requests", mRequests);
                requestsInbox.setArguments(args2);
                return requestsInbox;
            case 3:
                Fragment sentRequests = new ContactSentRequests();
                Bundle args3 = new Bundle();
                args3.putSerializable("email", mEmail);
                //args3.putSerializable("contacts", mContacts);
                sentRequests.setArguments(args3);
                return sentRequests;
            default:
                return null;
        }
    }

}
