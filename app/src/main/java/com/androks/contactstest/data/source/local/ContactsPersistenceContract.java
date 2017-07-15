package com.androks.contactstest.data.source.local;

/**
 * Created by androks on 15.07.17.
 */

public interface ContactsPersistenceContract {
    String TABLE_NAME = "contact";
    String _ID = "id";
    String _OWNER = "owner";
    String _NAME = "name";
    String _SURNAME = "surname";
    String _EMAIL = "email";
    String _ADDITIONAL_EMAIL = "add_email";
    String _PHONE = "phone";
    String _SECOND_PHONE = "second_phone";
    String _THIRD_PHONE = "third_phone";
}
