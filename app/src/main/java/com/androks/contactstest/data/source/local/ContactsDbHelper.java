package com.androks.contactstest.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by androks on 15.07.17.
 */

public class ContactsDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "Contacts.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_CONTACTS_TABLE =
            "CREATE TABLE " + ContactEntry.TABLE_NAME + " (" +
                    ContactEntry._ID + TEXT_TYPE + " PRIMARY KEY," +
                    ContactEntry._OWNER + TEXT_TYPE + COMMA_SEP +
                    ContactEntry._NAME + TEXT_TYPE + COMMA_SEP +
                    ContactEntry._SURNAME + TEXT_TYPE + COMMA_SEP +
                    ContactEntry._EMAIL + TEXT_TYPE + COMMA_SEP +
                    ContactEntry._ADDITIONAL_EMAIL + TEXT_TYPE + COMMA_SEP +
                    ContactEntry._PHONE + TEXT_TYPE + COMMA_SEP +
                    ContactEntry._SECOND_PHONE + TEXT_TYPE + COMMA_SEP +
                    ContactEntry._THIRD_PHONE + TEXT_TYPE +
                    " )";


    public ContactsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Not required yet
    }
}
