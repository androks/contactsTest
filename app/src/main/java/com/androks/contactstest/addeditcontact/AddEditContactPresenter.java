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

    private boolean isDataMissing;

    @NonNull
    private CompositeDisposable subscriptions;

    public AddEditContactPresenter(@Nullable String contactId,
                                   @NonNull ContactsDataSource contactsRepository,
                                   @NonNull AddEditContactContract.View view,
                                   boolean shouldLoadDataFromSource,
                                   @NonNull BaseSchedulerProvider schedulerProvider) {
        this.contactId = contactId;
        this.contactsRepository = contactsRepository;
        this.view = view;
        this.schedulerProvider = schedulerProvider;
        isDataMissing = shouldLoadDataFromSource;

        subscriptions = new CompositeDisposable();

        view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        if (!isNewTask() && isDataMissing)
            populateTask();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void saveContact(String name,
                            String surname,
                            List<MultiImputedViewsViewHolder> emailViews,
                            List<MultiImputedViewsViewHolder> phoneViews) {
        saveContact(Contact.newBuilder()
                .addEmails(convertViewsToEmails(emailViews))
                .addPhones(convertViewsToPhones(phoneViews))
                .name(name)
                .surname(surname)
                .build());
    }

    private boolean validateContact(Contact contact) {
        if (TextUtils.isEmpty(contact.getName())) {
            view.showEmptyNameError();
            return false;
        } else if (TextUtils.isEmpty(contact.getSurname())) {
            view.showEmptySurnameError();
            return false;
        } else if (contact.getPhones().isEmpty()) {
            view.showNoPhoneError();
            return false;
        } else if (contact.getEmails().isEmpty()) {
            view.showNoEmailError();
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

    private List<PhoneNumber> convertViewsToPhones(List<MultiImputedViewsViewHolder> phoneViews) {
        List<PhoneNumber> phones = new ArrayList<>(phoneViews.size());
        for (MultiImputedViewsViewHolder view : phoneViews) {
            phones.add(PhoneNumber.newBuilder()
                    .contactId(getCurrentuserEmail())
                    .phone(view.data.toString())
                    .label(view.data.toString())
                    .build());
        }
        return phones;
    }

    private List<Email> convertViewsToEmails(List<MultiImputedViewsViewHolder> emailViews) {
        List<Email> emails = new ArrayList<>(emailViews.size());
        for (MultiImputedViewsViewHolder view : emailViews) {
            emails.add(Email.newBuilder()
                    .contactId(getCurrentuserEmail())
                    .email(view.data.toString())
                    .label(view.data.toString())
                    .build());
        }
        return emails;
    }

    @Override
    public void populateTask() {
        subscriptions.add(contactsRepository
                .getContact(contactId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        //OnNext
                        contact -> {
                            view.setName(contact.getName());
                            view.setSurname(contact.getSurname());
                            view.setEmails(contact.getEmails());
                            view.setPhoneNumbers(contact.getPhones());
                        },
                        //OnError
                        __ -> {
                            view.showUnknownError();
                        }));
    }

    @Override
    public boolean isDataMissing() {
        return isDataMissing;
    }

    private boolean isNewTask() {
        return contactId == null;
    }

    private String getCurrentuserEmail() {
        return FirebaseAuth.getInstance().getCurrentUser().getEmail();
    }
}
