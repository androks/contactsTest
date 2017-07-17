package com.androks.contactstest.contacts;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androks.contactstest.R;
import com.androks.contactstest.data.entity.Contact;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactsFragment extends Fragment implements ContactsContract.View{


    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance(){
        return new ContactsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void setPresenter(ContactsContract.Presenter presenter) {

    }

    @Override
    public void setLoadingIndicator(boolean active) {

    }

    @Override
    public void showContacts(List<Contact> contacts) {

    }

    @Override
    public void showAddNewContact() {

    }

    @Override
    public void showNoContacts() {

    }

    @Override
    public void showLoadingContactsError() {

    }

    @Override
    public void showContactDetailsUI(String contactId) {

    }
}
