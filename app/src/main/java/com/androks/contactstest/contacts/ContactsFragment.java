package com.androks.contactstest.contacts;


import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.androks.contactstest.R;
import com.androks.contactstest.addeditcontact.AddEditContactActivity;
import com.androks.contactstest.contactdetail.ContactDetailActivity;
import com.androks.contactstest.data.entity.Contact;
import com.androks.contactstest.settings.SettingsActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment implements ContactsContract.View {

    @BindView(R.id.rv_contacts) RecyclerView contactsRv;
    @BindView(R.id.tv_no_items) TextView noItemsTv;

    private Unbinder unbinder;

    private ContactsContract.Presenter presenter;

    private ContactsRecyclerViewAdapter contactsAdapter;

    private ContactItemListener contactItemListener;

    private SearchView searchView;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        implementContactItemListener();

        contactsAdapter = new ContactsRecyclerViewAdapter(new ArrayList<>(0), contactItemListener);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        setUpRecyclerView();

        setRetainInstance(true);
        setHasOptionsMenu(true);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpFab();
    }

    private void setUpRecyclerView() {
        contactsRv.setLayoutManager(new LinearLayoutManager(getContext()));
        contactsRv.setHasFixedSize(true);
        contactsRv.setAdapter(contactsAdapter);
        contactsAdapter.setEmptyView(noItemsTv);
    }

    private void setUpFab() {
        // Set up floating action button
        getActivity().findViewById(R.id.fab_add_contact)
                .setOnClickListener(__ -> presenter.addNewContact());
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode);
    }

    @Override
    public void setPresenter(ContactsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        //TODO:Write Loading ind implementation
    }

    @Override
    public void showContacts(List<Contact> contacts) {
        contactsAdapter.replaceData(contacts);
    }

    @Override
    public void showAddNewContact() {
        startActivityForResult(new Intent(getContext(), AddEditContactActivity.class),
                AddEditContactActivity.REQUEST_ADD_CONTACT);
    }

    @Override
    public void showContactDetailsUI(String contactId) {
        Intent intent = new Intent(getContext(), ContactDetailActivity.class);
        intent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, contactId);
        startActivityForResult(intent, ContactDetailActivity.REQUEST_DELETE_CONTACT);
    }

    @Override
    public void showSettingUi() {
        startActivity(new Intent(getContext(), SettingsActivity.class));
    }

    @Override
    public void showContactDeletedMessage() {
        showSnackBar(getString(R.string.contact_deleted));
    }

    @Override
    public void showContactAddedMessage() {
        showSnackBar(getString(R.string.contact_added));
    }

    @Override
    public ContactSortType getContactSortType() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String syncConnPref = sharedPref.getString(
                SettingsActivity.KEY_PREF_CONTACTS_SORT_TYPE,
                ContactSortType.NAME.toString());
        try{
            return ContactSortType.valueOf(syncConnPref);
        }catch (IllegalArgumentException e){
            return ContactSortType.NAME;
        }
    }

    private void implementContactItemListener() {
        contactItemListener = new ContactItemListener() {
            @Override
            public void OnContactClick(Contact clickedContact) {
                presenter.openContactDetails(clickedContact);
            }

            @Override
            public void OnLongContactClick(Contact longClickedContact) {
                presenter.openContactDetails(longClickedContact);
            }
        };
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contacts_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint(getString(R.string.search_view_hint));

        //Handle searchView with RxJava
        com.jakewharton.rxbinding2.support.v7.widget.RxSearchView.queryTextChanges(searchView)
                .debounce(600, TimeUnit.MILLISECONDS)
                .map(CharSequence::toString)
                .subscribe(text -> presenter.applyFilterByName(text));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.settings:
                presenter.changeSetting();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
