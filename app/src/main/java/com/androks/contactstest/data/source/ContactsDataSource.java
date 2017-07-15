package com.androks.contactstest.data.source;

import com.androks.contactstest.data.Contact;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;

/**
 * Created by androks on 15.07.17.
 */

public interface ContactsDataSource {

    Flowable<List<Contact>> getContacts();

    Flowable<Contact> getContact(@NonNull String contactId);

    void saveContact(@NonNull Contact contact);

    void deleteContact(@NonNull String contactId);

}
