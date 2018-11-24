package tcss450.uw.edu.chatapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import tcss450.uw.edu.chatapp.model.Credentials;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener,
        WaitFragment.OnFragmentInteractionListener,
        VerificationFragment.OnVerificationFragmentInteractionListener {

    public static final String HOME_LOGIN_EMAIL = "email";
    public static final String HOME_LOGIN_PASSWORD = "password";
    private boolean mLoadFromChatNotification = false;
    private static final String TAG = "MainActivity_Notification";
    private Credentials mCredentials;
    private String mSender;
    private int mNotifChatId;
    private FirebaseMessageReciever mFirebaseMessageReciever;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("type")) {
                mLoadFromChatNotification = true;
                String chatID = getIntent().getExtras().getString("chatid");
                Log.d(TAG, "CHATID: "+chatID+", type of message: " + getIntent().getExtras().getString("type"));
//                mLoadFromChatNotification = getIntent().getExtras().getString("type").equals("msg");
            } else {
                Log.d(TAG, "NO MESSAGE");
            }
        }
        if (savedInstanceState == null) {
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
        intent.putExtra(HOME_LOGIN_EMAIL, credentials.getEmail());
        intent.putExtra(HOME_LOGIN_PASSWORD, credentials.getPassword());
        intent.putExtra(getString(R.string.keys_intent_notifification_msg), mLoadFromChatNotification);
        MainActivity.this.startActivity(intent);
        //End this Activity and remove it from the Activity back stack.
        finish();
    }

    @Override
    public void onRegisterClicked() {
        RegisterFragment registerFragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        registerFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, registerFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onResendClicked(Credentials credentials) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("credentials", credentials);
        VerificationFragment verificationFragment = new VerificationFragment();
        verificationFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, verificationFragment)
                .addToBackStack(null)
                .commit();
    }

    //Register Fragment Interface Methods
    @Override
    public void onRegisterAttempt(Credentials credentials) {

        mCredentials = credentials;

        Bundle bundle = new Bundle();
        bundle.putSerializable("credentials", credentials);
        VerificationFragment verificationFragment = new VerificationFragment();
        verificationFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, verificationFragment)
                .addToBackStack(null)
                .commit();

//        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
//        intent.putExtra(HOME_LOGIN_EMAIL, credentials.getEmail());
//        intent.putExtra(HOME_LOGIN_PASSWORD, credentials.getPassword());
////        intent.putExtra(HOME_LOGIN_USERNAME, credentials.getUsername());
//        startActivity(intent);
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

    //    @Override
//    public void onResendClicked() {
//        // TODO: resend verification email
//    }
    @Override
    public void onLoginClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, new LoginFragment())
                .addToBackStack(null)
                .commit();
    }

    //    @Override
//    public void onResume() {
//        super.onResume();
//        startLocationUpdates();
//        if (mFirebaseMessageReciever == null) {
//            mFirebaseMessageReciever = new FirebaseMessageReciever();
//        }
//        IntentFilter iFilter = new IntentFilter(MyFirebaseMessagingService.RECEIVED_NEW_MESSAGE);
//        registerReceiver(mFirebaseMessageReciever, iFilter);
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//        stopLocationUpdates();
//        if (mFirebaseMessageReciever != null) {
//            unregisterReceiver(mFirebaseMessageReciever);
//        }
//    }

    private class FirebaseMessageReciever extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("DATA")) {
                String data = intent.getStringExtra("DATA");
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(data);
                    if (jObj.has("message") && jObj.has("sender")) {
                        mSender = jObj.getString("sender");
                        mNotifChatId = jObj.getInt("chatid");
                        String msg = jObj.getString("message");
                        Log.d("MainActivity_Notification", mSender + " " + msg +", ChatID:" + mNotifChatId);
                    }
                } catch (JSONException e) {
                    Log.e("JSON PARSE", e.toString());
                }
            }
        }
    }
}
