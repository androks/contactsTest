package com.androks.contactstest.contacts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.androks.contactstest.R;

public class ContactsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        //ContactsRepository.getInstance()
    }
}
