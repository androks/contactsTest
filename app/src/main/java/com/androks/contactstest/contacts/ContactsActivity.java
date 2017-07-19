package com.androks.contactstest.contacts;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androks.contactstest.R;
import com.androks.contactstest.data.source.ContactsRepository;
import com.androks.contactstest.util.ActivityUtils;
import com.androks.contactstest.util.ProvideUtils;

public class ContactsActivity extends AppCompatActivity {

    private ContactsPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        setUpToolbar();

        ContactsFragment contactsFragment
                = (ContactsFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if(contactsFragment == null){
            //Create the fragment
            contactsFragment = ContactsFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), contactsFragment, R.id.contentFrame);
        }

        //Create the presenter
        presenter = new ContactsPresenter(
                ContactsRepository.getInstance(ProvideUtils.provideContactsRepository(this)),
                contactsFragment,
                ProvideUtils.provideScheduleProvider()
        );

        handleIntent(getIntent());
    }


    private void setUpToolbar(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle("Contacts");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        presenter.onBackPressed();
    }
}
