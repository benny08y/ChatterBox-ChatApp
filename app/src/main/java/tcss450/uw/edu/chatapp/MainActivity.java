package tcss450.uw.edu.chatapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import tcss450.uw.edu.chatapp.model.Credentials;
import tcss450.uw.edu.chatapp.utils.WaitFragment;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginFragmentInteractionListener,
        RegisterFragment.OnRegisterFragmentInteractionListener{

    public static final String HOME_LOGIN_EMAIL = "email";
    public static final String HOME_LOGIN_PASSWORD = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        intent.putExtra(HOME_LOGIN_EMAIL, credentials.getEmail());
        intent.putExtra(HOME_LOGIN_PASSWORD, credentials.getPassword());
        MainActivity.this.startActivity(intent);
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
        MainActivity.this.startActivity(intent);
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
