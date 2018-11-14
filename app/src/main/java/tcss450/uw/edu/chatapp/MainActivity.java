package tcss450.uw.edu.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import tcss450.uw.edu.chatapp.model.Credentials;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        WaitFragment.OnFragmentInteractionListener {

    public static final String HOME_LOGIN_EMAIL = "email";
    public static final String HOME_LOGIN_PASSWORD = "password";
    private boolean mLoadFromChatNotification = false;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("type")) {
                Log.d(TAG, "type of message: " + getIntent().getExtras().getString("type"));
                mLoadFromChatNotification = getIntent().getExtras().getString("type").equals("msg");
            } else {
                Log.d(TAG, "NO MESSAGE");
            }
        }
        if(savedInstanceState == null) {
            if (findViewById(R.id.main_container) != null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.main_container, new LoginFragment())
                        .commit();
            }
        }
    }


    //Login Fragment Interface Methods
    @Override
    public void onLoginAttempt(Credentials credentials) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//        intent.putExtra(HOME_LOGIN_EMAIL, credentials.getUsername());
        intent.putExtra(HOME_LOGIN_PASSWORD, credentials.getPassword());
        intent.putExtra(getString(R.string.keys_intent_notifification_msg), mLoadFromChatNotification);
        MainActivity.this.startActivity(intent);
        //End this Activity and remove it from the Activity back stack.
        finish();
    }
    @Override
    public void onRegisterClicked() {
        RegisterFragment registerFragment = (RegisterFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_register);
        registerFragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        registerFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, registerFragment)
                .addToBackStack(null)
                .commit();
    }

    //Register Fragment Interface Methods
    @Override
    public void onRegisterAttempt(Credentials credentials) {
        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
        intent.putExtra(HOME_LOGIN_EMAIL, credentials.getEmail());
        intent.putExtra(HOME_LOGIN_PASSWORD, credentials.getPassword());
//        intent.putExtra(HOME_LOGIN_USERNAME, credentials.getUsername());
        startActivity(intent);
    }

    //Wait Fragment Interface Methods
    @Override
    public void onWaitFragmentInteractionShow() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_container, new WaitFragment(), "WAIT")
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
}
