package com.androks.contactstest.contacts;

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
    }


    private void setUpToolbar(){
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Contacts");
    }
}
