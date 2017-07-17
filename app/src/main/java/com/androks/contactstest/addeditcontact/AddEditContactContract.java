package com.androks.contactstest.addeditcontact;

import com.androks.contactstest.BasePresenter;
import com.androks.contactstest.BaseView;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;

import java.util.List;

public interface AddEditContactContract {

    interface View extends BaseView<Presenter>{

        void showEmptyNameError();

        void showEmptySurnameError();

        void showNoEmailError();

        void showNoPhoneError();

        void showContactsList();

        void setName(String name);

        void setSurname(String surname);

        void setEmails(List<Email> emails);

        void setPhoneNumbers(List<PhoneNumber> phoneNumbers);

        void showUnknownError();
    }

    interface Presenter extends BasePresenter {

        void saveContact(String name,
                         String surname,
                         List<MultiImputedViewsViewHolder> emails,
                         List<MultiImputedViewsViewHolder> phones
        );

        void populateTask();

        boolean isDataMissing();
    }
}
