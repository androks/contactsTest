package com.androks.contactstest.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.androks.contactstest.R;
import com.androks.contactstest.login.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_CONTACTS_SORT_TYPE = "pref_contact_sort_type";

    @BindView(R.id.btn_log_out) Button logOutBtn;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        setUpToolbar();
        getFragmentManager().beginTransaction()
                .replace(R.id.contentFrame, new SettingsFragment()).commit();
    }

    @OnClick(R.id.btn_log_out)
    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        ActionBar ab = getSupportActionBar();
        if (ab == null)
            return;
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.settings);
    }
}
