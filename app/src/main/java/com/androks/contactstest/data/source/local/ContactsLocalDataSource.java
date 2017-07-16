package com.androks.contactstest.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;

import com.androks.contactstest.data.Contact;
import com.androks.contactstest.data.source.ContactsDataSource;
import com.androks.contactstest.util.schedulers.BaseSchedulerProvider;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

/**
 * Created by androks on 15.07.17.
 */

public class ContactsLocalDataSource implements ContactsDataSource{

    @Nullable
    private static ContactsLocalDataSource INSTANCE;

    @NonNull
    private final BriteDatabase databaseHelper;

    @NonNull
    private Function<Cursor, Contact> contactsMapperFunction;

    private ContactsLocalDataSource(@NonNull Context context,
                                    @NonNull BaseSchedulerProvider schedulerProvider){
        ContactsDbHelper dbHelper = new ContactsDbHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        databaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, schedulerProvider.io());
        contactsMapperFunction = this::getContact;
    }

    @NonNull
    private Contact getContact(@NonNull Cursor c) {
        return Contact.newBuilder()
                .id(c.getString(c.getColumnIndexOrThrow(ContactEntry._ID)))
                .name(c.getString(c.getColumnIndexOrThrow(ContactEntry._NAME)))
                .surname(c.getString(c.getColumnIndexOrThrow(ContactEntry._SURNAME)))
                .email(c.getString(c.getColumnIndexOrThrow(ContactEntry._EMAIL)))
                .additionalEmail(c.getString(c.getColumnIndexOrThrow(ContactEntry._ADDITIONAL_EMAIL)))
                .phone(c.getString(c.getColumnIndexOrThrow(ContactEntry._PHONE)))
                .secondPhone(c.getString(c.getColumnIndexOrThrow(ContactEntry._SECOND_PHONE)))
                .thirdPhone(c.getString(c.getColumnIndexOrThrow(ContactEntry._THIRD_PHONE)))
                .owner(c.getString(c.getColumnIndexOrThrow(ContactEntry._OWNER)))
                .createAt(c.getString(c.getColumnIndexOrThrow(ContactEntry._CREATED_AT)))
                .build();
    }

    public static ContactsLocalDataSource getInstance(
            @android.support.annotation.NonNull Context context,
            @android.support.annotation.NonNull BaseSchedulerProvider schedulerProvider) {
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
                .mapToList(contactsMapperFunction);
    }

    @Override
    public Observable<List<Contact>> getContact(@NonNull String contactId) {
        String sql = String.format("SELECT * FROM %s WHERE %s LIKE ?",
                ContactEntry.TABLE_NAME,
                ContactEntry._ID);
        return databaseHelper.createQuery(ContactEntry.TABLE_NAME, sql, contactId)
                .mapToList(contactsMapperFunction);
    }

    @Override
    public void saveContact(@NonNull Contact contact) {
        ContentValues values = new ContentValues();
        values.put(ContactEntry._ID, contact.getId());
        values.put(ContactEntry._OWNER, contact.getOwner());
        values.put(ContactEntry._NAME, contact.getName());
        values.put(ContactEntry._SURNAME, contact.getSurname());
        values.put(ContactEntry._EMAIL, contact.getEmail());
        values.put(ContactEntry._ADDITIONAL_EMAIL, contact.getAdditionalEmail());
        values.put(ContactEntry._PHONE, contact.getPhone());
        values.put(ContactEntry._SECOND_PHONE, contact.getSecondPhone());
        values.put(ContactEntry._THIRD_PHONE, contact.getThirdPhone());
        values.put(ContactEntry._CREATED_AT, contact.getCreatedAt());
        databaseHelper.insert(ContactEntry.TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
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
}
