package com.androks.contactstest.contactdetail;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.androks.contactstest.data.entity.Contact;
import com.androks.contactstest.data.source.ContactsRepository;
import com.androks.contactstest.util.schedulers.BaseSchedulerProvider;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by androks on 17.07.17.
 */

public class ContactDetailPresenter implements ContactDetailContract.Presenter {

    @NonNull
    private final ContactsRepository contactsRepository;

    @NonNull
    private final ContactDetailContract.View view;

    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    @Nullable
    private String contactId;

    @NonNull
    private CompositeDisposable subscriptions;

    public ContactDetailPresenter(@Nullable String contactId,
                                  @NonNull ContactsRepository contactsRepository,
                                  @NonNull ContactDetailContract.View view,
                                  @NonNull BaseSchedulerProvider schedulerProvider) {
        this.contactId = contactId;
        this.contactsRepository = contactsRepository;
        this.view = view;
        this.schedulerProvider = schedulerProvider;

        subscriptions = new CompositeDisposable();
        view.setPresenter(this);
    }


    @Override
    public void editContact() {
        view.showEditContact(contactId);
    }

    @Override
    public void deleteContact() {
        contactsRepository.deleteContact(contactId);
        view.showContactDeleted();
    }

    @Override
    public void call(String phoneNumber) {
        view.showCallUi(phoneNumber);
    }

    @Override
    public void sendMail(String email) {
        view.showSendMailUi(email);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode) {
        if(requestCode == ContactDetailsFragment.REQUEST_EDIT_CONTACT &&
                resultCode == Activity.RESULT_OK)
            view.showUserSavedMessage();
    }

    @Override
    public void subscribe() {
        openContact();
    }

    private void openContact() {
        subscriptions.add(contactsRepository.getContact(contactId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        //OnNext
                        this::showContact
                ));
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    private void showContact(@NonNull Contact contact){
        view.showFullName(contact.getName(), contact.getSurname());
        view.showEmails(contact.getEmails());
        view.showPhones(contact.getPhones());
    }
}
