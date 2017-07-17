package com.androks.contactstest.login;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androks.contactstest.R;
import com.androks.contactstest.util.ActivityUtils;

public class LoginActivity extends AppCompatActivity {

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setUpToolbar();

        LoginFragment loginFragment
                = (LoginFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(loginFragment == null){
            //Create the fragment
            loginFragment = LoginFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), loginFragment, R.id.contentFrame);
        }

        //Create the presenter
        loginPresenter = new LoginPresenter(loginFragment);
    }

    private void setUpToolbar(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Contacts");
    }
}
