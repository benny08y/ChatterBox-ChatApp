package tcss450.uw.edu.chatapp.contacts;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import tcss450.uw.edu.chatapp.model.Contacts;

public class ContactsPagerAdapter extends FragmentPagerAdapter {

    private int mTabCount;
    private String mEmail;
    private Contacts[] mContacts;

    public ContactsPagerAdapter(FragmentManager fm, String email, Contacts[] contacts) {
        super(fm);
        mTabCount = 3;
        mEmail = email;
        mContacts = contacts;
    }

    @Override
    public int getCount() {
        return mTabCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0:
                return "Contacts";
            case 1:
                return "Add Contact";
            case 2:
                return "Pending Requests";
            default:
                return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                Fragment contactsFragment0 = new ContactsFragment();
                Bundle args0 = new Bundle();
                args0.putSerializable("email", mEmail);
                args0.putSerializable("contacts", mContacts);
                contactsFragment0.setArguments(args0);
                return contactsFragment0;
            case 1:
                Fragment contactsFragment1 = new ContactsFragment();
                Bundle args1 = new Bundle();
                args1.putSerializable("email", mEmail);
                args1.putSerializable("contacts", mContacts);
                contactsFragment1.setArguments(args1);
                return contactsFragment1;
            case 2:
                Fragment contactsFragment2 = new ContactsFragment();
                Bundle args2 = new Bundle();
                args2.putSerializable("email", mEmail);
                args2.putSerializable("contacts", mContacts);
                contactsFragment2.setArguments(args2);
                return contactsFragment2;
            default:
                return null;
        }
    }

}
