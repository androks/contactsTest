package com.androks.contactstest.addeditcontact;


import android.os.Bundle;
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
public class AddEditContactFragment extends Fragment implements AddEditContactContract.View {

    public static final String ARGUMENT_EDIT_CONTACT_ID = "EDIT_CONTACT_ID";

    public AddEditContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_edit_contact, container, false);
    }

    public AddEditContactFragment newInstance() {
        return new AddEditContactFragment();
    }

    @Override
    public void setPresenter(AddEditContactContract.Presenter presenter) {

    }

    @Override
    public void showEmptyNameError() {

    }

    @Override
    public void showEmptySurnameError() {

    }

    @Override
    public void showNoEmailError() {

    }

    @Override
    public void showNoPhoneError() {

    }

    @Override
    public void showContactsList() {

    }

    @Override
    public void setName(String name) {

    }

    @Override
    public void setSurname(String surname) {

    }

    @Override
    public void setEmails(List<Email> emails) {

    }

    @Override
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {

    }

    @Override
    public void showUnknownError() {

    }
}
