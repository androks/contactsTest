package com.androks.contactstest.contactdetail;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.androks.contactstest.R;
import com.androks.contactstest.util.ActivityUtils;
import com.androks.contactstest.util.ProvideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ContactDetailActivity extends AppCompatActivity {

    public static final String EXTRA_CONTACT_ID = "CONTACT_ID";

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
        ButterKnife.bind(this);

        setUpToolbar();

        String contactId = getIntent().getStringExtra(EXTRA_CONTACT_ID);

        ContactDetailsFragment contactDetailsFragment
                = (ContactDetailsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(contactDetailsFragment == null){
            contactDetailsFragment = ContactDetailsFragment.newInstance(contactId);

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), contactDetailsFragment,
                    R.id.contentFrame);
        }

        new ContactDetailPresenter(contactId,
                ProvideUtils.provideContactsRepository(getApplicationContext()),
                contactDetailsFragment,
                ProvideUtils.provideScheduleProvider());
    }


    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        ActionBar ab = getSupportActionBar();
        if (ab == null)
            return;
        ab.setDisplayHomeAsUpEnabled(true);
    }
}
