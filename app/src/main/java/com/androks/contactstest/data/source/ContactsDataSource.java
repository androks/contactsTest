package com.androks.contactstest.data.source;

import com.androks.contactstest.data.entity.Contact;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;

/**
 * Created by androks on 15.07.17.
 */

public interface ContactsDataSource {

    Observable<Contact> getContacts(String ownerEmail);

    Observable<Contact> getContact(@NonNull String contactId);

    void saveContact(@NonNull Contact contact);

    void deleteContact(@NonNull Contact contact);

    void deleteContact(@NonNull String contactId);

    void deleteAllUserContact(@NonNull String ownerEmail);

}
