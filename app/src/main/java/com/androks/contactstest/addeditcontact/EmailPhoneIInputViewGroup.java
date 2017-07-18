package com.androks.contactstest.addeditcontact;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.androks.contactstest.R;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by androks on 17.07.17.
 */

public class EmailPhoneIInputViewGroup {

    @BindView(R.id.et_data) EditText data;
    @BindView(R.id.et_label) EditText label;
    @BindView(R.id.input_layout_label) TextInputLayout labelInputLayout;
    @BindView(R.id.input_layout_data) TextInputLayout dataInputLayout;

    public EmailPhoneIInputViewGroup(View view){
        ButterKnife.bind(this, view);
    }

    public void populate(Email email){
        data.setText(email.getEmail());
        label.setText(email.getLabel());
    }

    public void populate(PhoneNumber phone){
        data.setText(phone.getPhone());
        label.setText(phone.getLabel());
    }
}
