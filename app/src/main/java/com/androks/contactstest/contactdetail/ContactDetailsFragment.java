package com.androks.contactstest.contactdetail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androks.contactstest.R;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDetailsFragment extends Fragment implements ContactDetailContract.View {

    @NonNull
    private static final String ARGUMENT_CONTACT_ID = "ARGUMENT_CONTACT_ID";

    @NonNull
    private static final int REQUEST_EDIT_CONTACT = 188;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    public static ContactDetailsFragment newInstance(String contactId){
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CONTACT_ID, contactId);
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contact_details, container, false);
    }

    @Override
    public void setPresenter(ContactDetailContract.Presenter presenter) {

    }

    @Override
    public void showName(String name) {

    }

    @Override
    public void showSurname(String surname) {

    }

    @Override
    public void showPhones(List<PhoneNumber> phoneNumbers) {

    }

    @Override
    public void showEmails(List<Email> emails) {

    }

    @Override
    public void showEditContact(String contactId) {

    }

    @Override
    public void showContactDeleted() {

    }

    @Override
    public void showSendMailUi(String email) {

    }

    @Override
    public void showCallUi(String phone) {

    }
}
