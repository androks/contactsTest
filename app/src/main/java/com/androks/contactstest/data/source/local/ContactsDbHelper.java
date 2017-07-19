package com.androks.contactstest.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.androks.contactstest.data.source.local.entries.ContactEntry;
import com.androks.contactstest.data.source.local.entries.EmailEntry;
import com.androks.contactstest.data.source.local.entries.PhoneNumberEntry;

/**
 * Created by androks on 15.07.17.
 */

public class ContactsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Contacts.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_CONTACTS_TABLE =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry._ID + TEXT_TYPE + " PRIMARY KEY NOT NULL," +
                    ContactEntry._OWNER + TEXT_TYPE + " NOT NULL " + COMMA_SEP +
                    ContactEntry._NAME + TEXT_TYPE + COMMA_SEP +
                    ContactEntry._SURNAME + TEXT_TYPE + COMMA_SEP +
                    ContactEntry._CREATED_AT + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_EMAILS_TABLE =
            "CREATE TABLE " + EmailEntry.TABLE_NAME + " (" +
                    EmailEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    EmailEntry._CONTACT_ID + TEXT_TYPE + " NOT NULL," +
                    EmailEntry._EMAIL + TEXT_TYPE + COMMA_SEP +
                    EmailEntry._LABEL + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY " + "(" + EmailEntry._CONTACT_ID + ") " +
                    "REFERENCES " + ContactEntry.TABLE_NAME + "(" + ContactEntry._ID + ") " +
                    "ON DELETE CASCADE " + "ON UPDATE CASCADE" +
                    " )";

    private static final String SQL_CREATE_PHONE_NUMBERS_TABLE =
            "CREATE TABLE " + PhoneNumberEntry.TABLE_NAME + " (" +
                    PhoneNumberEntry._ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT," +
                    PhoneNumberEntry._CONTACT_ID + TEXT_TYPE + " NOT NULL," +
                    PhoneNumberEntry._PHONE + TEXT_TYPE + COMMA_SEP +
                    PhoneNumberEntry._LABEL + TEXT_TYPE + COMMA_SEP +
                    "FOREIGN KEY " + "(" + PhoneNumberEntry._CONTACT_ID + ") " +
                    "REFERENCES " + ContactEntry.TABLE_NAME + "(" + ContactEntry._ID + ")" +
                    "ON DELETE CASCADE " + "ON UPDATE CASCADE" +
                    " )";

    public ContactsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
        db.execSQL(SQL_CREATE_EMAILS_TABLE);
        db.execSQL(SQL_CREATE_PHONE_NUMBERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Not required yet
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }
}
