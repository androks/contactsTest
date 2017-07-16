package com.androks.contactstest.data.source;

import com.androks.contactstest.data.Contact;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Created by androks on 15.07.17.
 */

public class ContactsRepository implements ContactsDataSource {

    @Nullable
    private static ContactsRepository INSTANCE = null;

    @NonNull
    private final ContactsDataSource contactlocalDataSource;


    private ContactsRepository(@NonNull ContactsDataSource contactsLocalDataSource) {
        this.contactlocalDataSource = contactsLocalDataSource;

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param tasksLocalDataSource the device storage data source
     * @return the {@link ContactsRepository} instance
     */
    public static ContactsRepository getInstance(@NonNull ContactsDataSource tasksLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ContactsRepository(tasksLocalDataSource);
        }
        return INSTANCE;
    }

    /**
     * Used to force {@link #getInstance(ContactsDataSource)} to create a new instance
     * next time it's called.
     */
    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public Observable<List<Contact>> getContacts(String ownerEmail) {
        return contactlocalDataSource.getContacts(ownerEmail);
    }

    @Override
    public Observable<List<Contact>> getContact(@NonNull String contactId) {
        return contactlocalDataSource.getContact(contactId);
    }

    @Override
    public void saveContact(@NonNull Contact contact) {
        contactlocalDataSource.saveContact(contact);
    }

    @Override
    public void deleteContact(@NonNull Contact contact) {
        contactlocalDataSource.deleteContact(contact);
    }

    @Override
    public void deleteContact(@NonNull String contactId) {
        contactlocalDataSource.deleteContact(contactId);
    }

    @Override
    public void deleteAllUserContact(@NonNull String ownerEmail) {
        contactlocalDataSource.deleteAllUserContact(ownerEmail);
    }
}

