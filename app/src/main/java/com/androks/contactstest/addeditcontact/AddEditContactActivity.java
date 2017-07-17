package com.androks.contactstest.addeditcontact;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androks.contactstest.R;

public class AddEditContactActivity extends AppCompatActivity {

    public static final int REQUEST_ADD_CONTACT = 54;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_contact);
    }
}
