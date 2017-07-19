package com.androks.contactstest.contacts;

import com.androks.contactstest.BasePresenter;
import com.androks.contactstest.BaseView;
import com.androks.contactstest.data.entity.Contact;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by androks on 16.07.17.
 */

public interface ContactsContract {

    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void showContacts(List<Contact> contacts);

        void showAddNewContact();

        void showNoContacts();

        void showLoadingContactsError();

        void showContactDetailsUI(String contactId);

        void showSettingUi();

        void showContactDeletedMessage();

        void showContactAddedMessage();

        ContactSortType getContactSortType();
    }


    interface Presenter extends BasePresenter {

        void onActivityResult(int requestCode, int resultCode);

        void loadContacts();

        void addNewContact();

        void openContactDetails(@NonNull Contact requestedTask);

        void applyFilterByName(String partOfName);

        void clearFilter();

        void changeSetting();
    }
}
