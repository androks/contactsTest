package com.androks.contactstest.contactdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androks.contactstest.R;

public class ActivityContactDetail extends AppCompatActivity {

    public static final String EXTRA_CONTACT_ID = "CONTACT_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detail);
    }
}
