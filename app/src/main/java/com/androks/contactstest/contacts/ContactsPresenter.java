package com.androks.contactstest.contacts;

import com.androks.contactstest.data.entity.Contact;
import com.androks.contactstest.data.source.ContactsRepository;
import com.androks.contactstest.util.schedulers.BaseSchedulerProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by androks on 17.07.17.
 */

public class ContactsPresenter implements ContactsContract.Presenter {

    @NonNull
    private final ContactsRepository contactsRepository;

    @NonNull
    private final ContactsContract.View view;

    @NonNull
    private final BaseSchedulerProvider schedulerProvider;

    @NonNull
    private CompositeDisposable subscriptions;

    public ContactsPresenter(ContactsRepository contactsRepository,
                             ContactsContract.View view,
                             BaseSchedulerProvider provider) {
        this.contactsRepository = contactsRepository;
        this.view = view;
        schedulerProvider = provider;

        subscriptions = new CompositeDisposable();
        view.setPresenter(this);
    }

    @Override
    public void subscribe() {
        loadContacts();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadContacts() {
        loadContacts(true);
    }

    @Override
    public void addNewContact() {
        view.showAddNewContact();
    }

    private void loadContacts(boolean showLoadingUI) {
        view.setLoadingIndicator(showLoadingUI);

        subscriptions.clear();
        contactsRepository
                .getContacts(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(
                        //OnNext
                        this::processContacts,
                        //OnError
                        throwable -> view.showLoadingContactsError(),
                        //OnCompleted
                        () -> view.setLoadingIndicator(false),
                        //OnSubscribe
                        d -> subscriptions.add(d));

    }

    private void processContacts(@NonNull List<Contact> contacts) {
        if (contacts.isEmpty())
            view.showNoContacts();
        //Show the list of the tasks
        view.showContacts(contacts);

    }


    @Override
    public void openContactDetails(@NonNull Contact requestedTask) {
        view.showContactDetailsUI(requestedTask.getId());
    }
}
