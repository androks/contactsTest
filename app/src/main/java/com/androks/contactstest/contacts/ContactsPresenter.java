package com.androks.contactstest.contacts;

import android.text.TextUtils;

import com.androks.contactstest.data.entity.Contact;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;
import com.androks.contactstest.data.source.ContactsRepository;
import com.androks.contactstest.util.schedulers.BaseSchedulerProvider;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
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

    @Nullable
    private String queryPartOfName;

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
                .flatMap(list -> {
                    Observable<Contact> contacts = Observable.fromIterable(list);
                    if(TextUtils.isEmpty(queryPartOfName))
                        return contacts.toList().toObservable();
                    return Observable.fromIterable(list)
                            .filter(this::filterContact).toList().toObservable();
                })
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

    private boolean filterContact(Contact contact){
        if(contact.getName().toLowerCase().contains(queryPartOfName))
            return true;
        if(contact.getSurname().toLowerCase().contains(queryPartOfName))
            return true;
        if((contact.getName().toLowerCase() + " " + contact.getSurname().toLowerCase())
                .contains(queryPartOfName))
            return true;
        for(PhoneNumber phone: contact.getPhones()){
            if(phone.getPhone().contains(queryPartOfName))
                return true;
        }
        for(Email email: contact.getEmails()){
            if(email.getEmail().toLowerCase().contains(queryPartOfName))
                return true;
        }
        return false;
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

    @Override
    public void applyFilterByName(String partOfName) {
        queryPartOfName = partOfName.trim().toLowerCase();
        loadContacts();
    }

    @Override
    public void clearFilter() {
        queryPartOfName = null;
        loadContacts();
    }

    public void onBackPressed() {
        view.onBackPressed();
    }
}
