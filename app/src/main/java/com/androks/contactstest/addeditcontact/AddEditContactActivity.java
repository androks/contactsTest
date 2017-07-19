package com.androks.contactstest.addeditcontact;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.androks.contactstest.R;
import com.androks.contactstest.util.ActivityUtils;
import com.androks.contactstest.util.ProvideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddEditContactActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_CONTACT = 5544;

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    @BindView(R.id.toolbar) Toolbar toolbar;

    private AddEditContactPresenter presenter;

    private  AddEditContactFragment addEditContactFragment;

    private boolean shouldLoadDataFromRepo = true;

    private String editContactId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);
        ButterKnife.bind(this);

        editContactId = getIntent().getStringExtra(AddEditContactFragment.ARGUMENT_EDIT_CONTACT_ID);

        setUpToolbar();

        addEditContactFragment =
                (AddEditContactFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if (addEditContactFragment == null) {
            addEditContactFragment = new AddEditContactFragment().newInstance();

            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),
                    addEditContactFragment, R.id.contentFrame);
        }

        // Prevent the presenter from loading data from the repository if this is a config change.
        if (savedInstanceState != null) {
            // Data might not have loaded when the config change happen, so we saved the state.
            shouldLoadDataFromRepo = savedInstanceState.getBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY);
        }

        //Create the presenter
        presenter = new AddEditContactPresenter(
                editContactId,
                ProvideUtils.provideContactsRepository(getApplicationContext()),
                addEditContactFragment,
                shouldLoadDataFromRepo,
                ProvideUtils.provideScheduleProvider()
        );
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(view -> onBackPressed());
        ActionBar ab = getSupportActionBar();
        if (ab == null)
            return;
        ab.setDisplayHomeAsUpEnabled(true);
        if (TextUtils.isEmpty(editContactId))
            ab.setTitle(R.string.add_contact);
        else
            ab.setTitle(R.string.edit_contact);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Save the state so that next time we know if we need to refresh data.
        outState.putBoolean(SHOULD_LOAD_DATA_FROM_REPO_KEY, presenter.isDataMissing());
        super.onSaveInstanceState(outState);
    }
}
