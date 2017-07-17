package com.androks.contactstest.addeditcontact;

import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;

import com.androks.contactstest.R;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by androks on 17.07.17.
 */

public class MultiImputedViewsViewHolder {

    @BindView(R.id.et_data) EditText data;
    @BindView(R.id.et_label) EditText label;
    @BindView(R.id.input_layout_label) TextInputLayout labelInputLayout;
    @BindView(R.id.input_layout_data) TextInputLayout dataInputLayout;

    private int id;

    public MultiImputedViewsViewHolder(View view){
        ButterKnife.bind(this, view);
    }

    public int getId() {
        return id;
    }

    public void populate(Email email){
        data.setText(email.getEmail());
        label.setText(email.getLabel());
        id = UUID.randomUUID().variant();
    }

    public void populate(PhoneNumber phone){
        data.setText(phone.getPhone());
        label.setText(phone.getLabel());
        id = UUID.randomUUID().variant();
    }
}
