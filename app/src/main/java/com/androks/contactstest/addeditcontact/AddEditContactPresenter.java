package com.androks.contactstest.addeditcontact;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.androks.contactstest.data.entity.Contact;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;
import com.androks.contactstest.data.source.ContactsDataSource;
import com.androks.contactstest.util.schedulers.BaseSchedulerProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by androks on 17.07.17.
 */

public class AddEditContactPresenter implements AddEditContactContract.Presenter {

    @NonNull
    private final ContactsDataSource contactsRepository;

    @NonNull
    private final AddEditContactContract.View view;

    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    @Nullable
    private String contactId;

    private boolean isConfigChanged;

    @NonNull
    private CompositeDisposable subscriptions;

    public AddEditContactPresenter(@Nullable String contactId,
                                   @NonNull ContactsDataSource contactsRepository,
                                   @NonNull AddEditContactContract.View view,
                                   boolean isConfigChanged,
                                   @NonNull BaseSchedulerProvider schedulerProvider) {
        this.contactId = contactId;
        this.contactsRepository = contactsRepository;
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        this.isConfigChanged = isConfigChanged;

        subscriptions = new CompositeDisposable();

        view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (!isNewContact())
            populateContact();
        else {
            initNewContact();
        }
    }

    private void initNewContact() {
        view.showNewPhoneInputLayout();
        view.showNewEmailInputLayout();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void saveContact(String name,
                            String surname,
                            List<EmailPhoneInputViewGroup> emailViews,
                            List<EmailPhoneInputViewGroup> phoneViews) {
        if (isNewContact())
            createContact(name, surname, emailViews, phoneViews);
        else
            updateContact(name, surname, emailViews, phoneViews);
    }

    private void updateContact(String name,
                               String surname,
                               List<EmailPhoneInputViewGroup> emailViews,
                               List<EmailPhoneInputViewGroup> phoneViews) {
        Contact contact = Contact.newBuilder()
                .id(contactId)
                .owner(getCurrentUserEmail())
                .name(name)
                .surname(surname)
                .build();
        contact.addEmails(convertViewsToEmails(emailViews));
        contact.addPhoneNumbers(convertViewsToPhones(phoneViews));
        saveContact(contact);
    }

    private void createContact(String name,
                               String surname,
                               List<EmailPhoneInputViewGroup> emailViews,
                               List<EmailPhoneInputViewGroup> phoneViews) {
        Contact contact = Contact.newBuilder()
                .owner(getCurrentUserEmail())
                .name(name)
                .surname(surname)
                .build();
        contactId = contact.getId();
        contact.addEmails(convertViewsToEmails(emailViews));
        contact.addPhoneNumbers(convertViewsToPhones(phoneViews));
        saveContact(contact);
    }

    private boolean validateContact(Contact contact) {
        view.clearAllErrors();
        if (TextUtils.isEmpty(contact.getName())) {
            view.showEmptyNameError();
            return false;
        } else if (TextUtils.isEmpty(contact.getSurname())) {
            view.showEmptySurnameError();
            return false;
        } else if (contact.getEmails().isEmpty()) {
            view.showNoEmailError();
            return false;
        } else if (contact.getPhones().isEmpty()) {
            view.showNoPhoneError();
            return false;
        } else
            return true;

    }

    private void saveContact(Contact contact) {
        if (!validateContact(contact))
            return;
        contactsRepository.saveContact(contact);
        view.showContactsList();
    }

    private List<PhoneNumber> convertViewsToPhones(List<EmailPhoneInputViewGroup> phoneViews) {
        List<PhoneNumber> phones = new ArrayList<>(phoneViews.size());
        if (phoneViews.isEmpty())
            return phones;
        for (EmailPhoneInputViewGroup view : phoneViews) {
            if (view.data.getText().toString().trim().isEmpty())
                continue;
            phones.add(PhoneNumber.newBuilder()
                    .contactId(contactId)
                    .phone(view.data.getText().toString().trim())
                    .label(view.label.getText().toString().trim())
                    .build());
        }
        return phones;
    }

    private List<Email> convertViewsToEmails(List<EmailPhoneInputViewGroup> emailViews) {
        List<Email> emails = new ArrayList<>(emailViews.size());
        if (emailViews.isEmpty())
            return emails;
        for (EmailPhoneInputViewGroup view : emailViews) {
            if (view.data.getText().toString().trim().isEmpty())
                continue;
            emails.add(Email.newBuilder()
                    .contactId(contactId)
                    .email(view.data.getText().toString().trim())
                    .label(view.label.getText().toString().trim())
                    .build());
        }
        return emails;
    }

    @Override
    public void populateContact() {
        subscriptions.add(contactsRepository
                .getContact(contactId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        //OnNext
                        contact -> {
                            view.setName(contact.getName());
                            view.setSurname(contact.getSurname());
                            if (!isConfigChanged) {
                                view.setEmails(contact.getEmails());
                                view.setPhoneNumbers(contact.getPhones());
                            }
                        },
                        //OnError
                        __ -> {
                            view.showUnknownError();
                        }));
    }

    @Override
    public boolean isConfigChanged() {
        return isConfigChanged;
    }

    @Override
    public void addNewEmailInputLayout() {
        view.showNewEmailInputLayout();
    }

    @Override
    public void addNewPhoneInputLayout() {
        view.showNewPhoneInputLayout();
    }

    private boolean isNewContact() {
        return contactId == null;
    }

    private String getCurrentUserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
}
