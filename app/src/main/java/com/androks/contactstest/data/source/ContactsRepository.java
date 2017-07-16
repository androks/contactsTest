package com.androks.contactstest.data.source;

import com.androks.contactstest.data.entity.Contact;

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
    private final ContactsDataSource contactLocalDataSource;


    private ContactsRepository(@NonNull ContactsDataSource contactsLocalDataSource) {
        this.contactLocalDataSource = contactsLocalDataSource;

    }

    /**
     * Returns the single instance of this class, creating it if necessary.
     *
     * @param contactsLocalDataSource the device storage data source
     * @return the {@link ContactsRepository} instance
     */
    public static ContactsRepository getInstance(@NonNull ContactsDataSource contactsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ContactsRepository(contactsLocalDataSource);
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
        return contactLocalDataSource.getContacts(ownerEmail);
    }

    @Override
    public Observable<Contact> getContact(@NonNull String contactId) {
        return contactLocalDataSource.getContact(contactId);
    }

    @Override
    public void saveContact(@NonNull Contact contact) {
        contactLocalDataSource.saveContact(contact);
    }

    @Override
    public void deleteContact(@NonNull Contact contact) {
        contactLocalDataSource.deleteContact(contact);
    }

    @Override
    public void deleteContact(@NonNull String contactId) {
        contactLocalDataSource.deleteContact(contactId);
    }

    @Override
    public void deleteAllUserContact(@NonNull String ownerEmail) {
        contactLocalDataSource.deleteAllUserContact(ownerEmail);
    }
}

