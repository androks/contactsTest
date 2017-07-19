package com.androks.contactstest.contactdetail;

import com.androks.contactstest.BasePresenter;
import com.androks.contactstest.BaseView;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;

import java.util.List;

/**
 * Created by androks on 17.07.17.
 */

public interface ContactDetailContract {

    interface View extends BaseView<Presenter>{

        void showFullName(String name, String surname);

        void showPhones(List<PhoneNumber> phoneNumbers);

        void showEmails(List<Email> emails);

        void showEditContact(String contactId);

        void showContactDeleted();

        void showSendMailUi(String email);

        void showCallUi(String phone);

        void showCreatedAt(String createdAt);

        void showUserSavedMessage();

    }

    interface Presenter extends BasePresenter {

        void editContact();

        void deleteContact();

        void call(String phoneNumber);

        void sendMail(String email);

        void onActivityResult(int requestCode, int resultCode);
    }
}
