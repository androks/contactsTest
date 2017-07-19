package com.androks.contactstest.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.androks.contactstest.data.entity.Contact;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;
import com.androks.contactstest.data.source.ContactsDataSource;
import com.androks.contactstest.data.source.local.entries.ContactEntry;
import com.androks.contactstest.data.source.local.entries.EmailEntry;
import com.androks.contactstest.data.source.local.entries.PhoneNumberEntry;
import com.androks.contactstest.util.schedulers.BaseSchedulerProvider;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.QueryObservable;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by androks on 15.07.17.
 */

public class ContactsLocalDataSource implements ContactsDataSource {

    @Nullable
    private static ContactsLocalDataSource INSTANCE;

    @NonNull
    private final BriteDatabase databaseHelper;

    @NonNull
    private Function<Cursor, Contact> contactsMapperFunction;

    @NonNull
    private Function<Cursor, Email> emailMapperFunction;

    @NonNull
    private Function<Cursor, PhoneNumber> phoneNumberMapperFunction;

    private ContactsLocalDataSource(@NonNull Context context,
                                    @NonNull BaseSchedulerProvider schedulerProvider) {
        ContactsDbHelper dbHelper = new ContactsDbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        databaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, schedulerProvider.io());
        contactsMapperFunction = this::getContact;
        emailMapperFunction = this::getEmail;
        phoneNumberMapperFunction = this::getPhoneNumber;
    }

    @NonNull
    private Email getEmail(@NonNull Cursor c) {
        return Email.newBuilder()
                .id(c.getString(c.getColumnIndexOrThrow(EmailEntry._ID)))
                .contactId(c.getString(c.getColumnIndexOrThrow(EmailEntry._CONTACT_ID)))
                .email(c.getString(c.getColumnIndexOrThrow(EmailEntry._EMAIL)))
                .label(c.getString(c.getColumnIndexOrThrow(EmailEntry._LABEL)))
                .build();
    }

    @NonNull
    private PhoneNumber getPhoneNumber(@NonNull Cursor c) {
        return PhoneNumber.newBuilder()
                .id(c.getString(c.getColumnIndexOrThrow(PhoneNumberEntry._ID)))
                .contactId(c.getString(c.getColumnIndexOrThrow(PhoneNumberEntry._CONTACT_ID)))
                .phone(c.getString(c.getColumnIndexOrThrow(PhoneNumberEntry._PHONE)))
                .label(c.getString(c.getColumnIndexOrThrow(PhoneNumberEntry._LABEL)))
                .build();
    }

    @NonNull
    private Contact getContact(@NonNull Cursor c) {
        return Contact.newBuilder()
                .id(c.getString(c.getColumnIndexOrThrow(ContactEntry._ID)))
                .name(c.getString(c.getColumnIndexOrThrow(ContactEntry._NAME)))
                .surname(c.getString(c.getColumnIndexOrThrow(ContactEntry._SURNAME)))
                .owner(c.getString(c.getColumnIndexOrThrow(ContactEntry._OWNER)))
                .createdAt(c.getString(c.getColumnIndexOrThrow(ContactEntry._CREATED_AT)))
                .build();
    }

    public static ContactsLocalDataSource getInstance(
            @NonNull Context context,
            @NonNull BaseSchedulerProvider schedulerProvider) {
        if (INSTANCE == null) {
            INSTANCE = new ContactsLocalDataSource(context, schedulerProvider);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Observable<List<Contact>> getContacts(String ownerEmail) {
        String sql = String.format("SELECT * FROM %s WHERE %s LIKE ?",
                ContactEntry.TABLE_NAME,
                ContactEntry._OWNER);

        return databaseHelper.createQuery(ContactEntry.TABLE_NAME, sql, ownerEmail)
                .mapToList(contactsMapperFunction).flatMap(
                        list -> {
                            Observable<Contact> contactsObj = Observable.fromIterable(list);
                            return Observable.zip(
                                    contactsObj,
                                    contactsObj.flatMap(contact -> getPhoneNumbers(contact.getId())),
                                    contactsObj.flatMap(contact -> getEmails(contact.getId())),
                                    (contact, phoneNumbers, emails) -> {
                                        contact.addPhoneNumbers(phoneNumbers);
                                        contact.addEmails(emails);
                                        return contact;
                                    }
                            ).toList().toObservable();
                        });
    }

    @Override
    public Observable<Contact> getContact(@NonNull String contactId) {
        String sql = String.format("SELECT * FROM %s WHERE contact.id LIKE ?",
                ContactEntry.TABLE_NAME,
                ContactEntry._ID);
        Observable<Contact> contactObj = databaseHelper
                .createQuery(ContactEntry.TABLE_NAME, sql, contactId)
                .mapToOne(contactsMapperFunction);
        return Observable.zip(
                contactObj,
                contactObj.flatMap(contact -> getPhoneNumbers(contact.getId())),
                contactObj.flatMap(contact -> getEmails(contact.getId())),
                (contact, phoneNumbers, emails) -> {
                    contact.addPhoneNumbers(phoneNumbers);
                    contact.addEmails(emails);
                    return contact;
                }
        );
    }

    @Override
    public void saveContact(@NonNull Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactEntry._ID, contact.getId());
        values.put(ContactEntry._OWNER, contact.getOwner());
        values.put(ContactEntry._NAME, contact.getName());
        values.put(ContactEntry._SURNAME, contact.getSurname());
        values.put(ContactEntry._CREATED_AT, contact.getCreatedAt());

        BriteDatabase.Transaction transaction = databaseHelper.newTransaction();
        try {
            deleteAllContactsEmails(contact.getId());
            deleteAllContactsPhones(contact.getId());

            databaseHelper.insert(ContactEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);

            Observable.fromIterable(contact.getEmails())
                    .subscribe(this::saveEmail);
            Observable.fromIterable(contact.getPhones())
                    .subscribe(this::savePhoneNumber);
            transaction.markSuccessful();
        } finally {
            transaction.end();
        }
    }

    @Override
    public void deleteContact(@NonNull Contact contact) {
        deleteContact(contact.getId());
    }

    @Override
    public void deleteContact(@NonNull String contactId) {
        databaseHelper.delete(ContactEntry.TABLE_NAME, ContactEntry._ID + " LIKE ?", contactId);
    }

    @Override
    public void deleteAllUserContact(@NonNull String ownerEmail) {
        databaseHelper.delete(ContactEntry.TABLE_NAME, ContactEntry._OWNER + " LIKE ?", ownerEmail);
    }

    private void saveEmail(@NonNull Email email) {
        ContentValues values = new ContentValues();
        if(email.getId() != null)
            values.put(EmailEntry._ID, email.getId());
        values.put(EmailEntry._CONTACT_ID, email.getContactId());
        values.put(EmailEntry._EMAIL, email.getEmail());
        values.put(EmailEntry._LABEL, email.getLabel());
        databaseHelper.insert(EmailEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void savePhoneNumber(@NonNull PhoneNumber phoneNumber) {
        ContentValues values = new ContentValues();
        if(phoneNumber.getId() != null)
            values.put(PhoneNumberEntry._ID, phoneNumber.getId());
        values.put(PhoneNumberEntry._CONTACT_ID, phoneNumber.getContactId());
        values.put(PhoneNumberEntry._PHONE, phoneNumber.getPhone());
        values.put(PhoneNumberEntry._LABEL, phoneNumber.getLabel());
        databaseHelper.insert(PhoneNumberEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    private void deleteAllContactsEmails(@NonNull String contactId) {
        databaseHelper.delete(EmailEntry.TABLE_NAME, EmailEntry._CONTACT_ID
                + " LIKE ?", contactId);
    }

    private void deleteAllContactsPhones(@NonNull String contactId) {
        databaseHelper.delete(PhoneNumberEntry.TABLE_NAME, PhoneNumberEntry._CONTACT_ID
                + " LIKE ?", contactId);
    }

    /**
     * @param contactId
     * @return the flow of contact's emails with the id = contactId
     */
    private Observable<List<Email>> getEmails(String contactId) {
        String sql = String.format("SELECT * FROM %s WHERE %s LIKE ?",
                EmailEntry.TABLE_NAME,
                EmailEntry._CONTACT_ID);
        return databaseHelper.createQuery(EmailEntry.TABLE_NAME, sql, contactId)
                .mapToList(emailMapperFunction);
    }

    /**
     * @param contactId
     * @return the flow of contact's PhoneNumbers with the id = contactId
     */
    private Observable<List<PhoneNumber>> getPhoneNumbers(String contactId) {
        String sql = String.format("SELECT * FROM %s WHERE %s LIKE ?",
                PhoneNumberEntry.TABLE_NAME,
                PhoneNumberEntry._CONTACT_ID);
        return databaseHelper.createQuery(PhoneNumberEntry.TABLE_NAME, sql, contactId)
                .mapToList(phoneNumberMapperFunction);
    }

    private QueryObservable getTestPhone(String contactId) {
        List<PhoneNumber> results = new ArrayList<>();
        String sql = String.format("SELECT * FROM %s WHERE %s LIKE ?",
                PhoneNumberEntry.TABLE_NAME,
                PhoneNumberEntry._CONTACT_ID);
        return databaseHelper.createQuery(PhoneNumberEntry.TABLE_NAME, sql, contactId);
    }
}
