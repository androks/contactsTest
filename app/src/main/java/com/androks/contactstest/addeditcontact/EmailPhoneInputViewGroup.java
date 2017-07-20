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

public class EmailPhoneInputViewGroup {

    public View rootView;
    @BindView(R.id.et_data) EditText data;
    @BindView(R.id.et_label) EditText label;
    @BindView(R.id.input_layout_label) TextInputLayout labelInputLayout;
    @BindView(R.id.input_layout_data) TextInputLayout dataInputLayout;

    public EmailPhoneInputViewGroup(View view){
        ButterKnife.bind(this, view);
        rootView = view;
        rootView.setId(View.generateViewId());
        data.setId(View.generateViewId());
        label.setId(View.generateViewId());
        labelInputLayout.setId(View.generateViewId());
        dataInputLayout.setId(View.generateViewId());
    }

    public void populate(Email email){
        data.setText(email.getEmail());
        label.setText(email.getLabel());

    }

    public void populate(PhoneNumber phone){
        data.setText(phone.getPhone());
        label.setText(phone.getLabel());
    }

    public Email toEmail(){
        return Email.newBuilder()
                .label(label.getText().toString())
                .email(data.getText().toString()).build();
    }

    public PhoneNumber toPhone(){
        return PhoneNumber.newBuilder()
                .label(label.getText().toString())
                .phone(data.getText().toString()).build();
    }
}
