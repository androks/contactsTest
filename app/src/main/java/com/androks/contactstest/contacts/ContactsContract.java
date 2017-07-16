package com.androks.contactstest.contacts;

import com.androks.contactstest.BasePresenter;
import com.androks.contactstest.BaseView;
import com.androks.contactstest.data.Contact;

import java.util.List;

/**
 * Created by androks on 16.07.17.
 */

public interface ContactsContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void showContacts(List<Contact> contacts);

        void showAddContact();

        void showNoContacts();
    }


    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode);

        void loadContacts();

        void addNewContact();

        void openContactDetails();
    }
}
