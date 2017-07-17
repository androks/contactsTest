package com.androks.contactstest.addeditcontact;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.androks.contactstest.R;
import com.androks.contactstest.util.ActivityUtils;
import com.androks.contactstest.util.ProvideUtils;

public class AddEditContactActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_CONTACT = 54;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    private AddEditContactPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);

        boolean shouldLoadDataFromRepo = true;

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        AddEditContactFragment addEditContactFragment =
                (AddEditContactFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        String contactId = getIntent().getStringExtra(AddEditContactFragment.ARGUMENT_EDIT_CONTACT_ID);

        if (addEditContactFragment == null) {
            addEditContactFragment = new AddEditContactFragment().newInstance();

            setUpToolbar(getIntent().hasExtra(AddEditContactFragment.ARGUMENT_EDIT_CONTACT_ID));

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditContactFragment, R.id.contentFrame);
        }

        //Create the presenter
        presenter = new AddEditContactPresenter(
                contactId,
                ProvideUtils.provideContactsRepository(getApplicationContext()),
                addEditContactFragment,
                shouldLoadDataFromRepo,
                ProvideUtils.provideScheduleProvider()
        );
    }
    private void setUpToolbar(boolean newContact) {
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        if (newContact) {
            actionBar.setTitle(R.string.edit_contact);
        } else {
            actionBar.setTitle(R.string.add_contact);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, presenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }
}
