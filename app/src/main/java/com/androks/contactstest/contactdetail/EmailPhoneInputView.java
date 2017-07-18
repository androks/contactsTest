package com.androks.contactstest.contactdetail;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.androks.contactstest.R;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by androks on 17.07.17.
 */

public class EmailPhoneInputView {

    @BindView(R.id.tv_data) TextView data;
    @BindView(R.id.tv_label) TextView label;

    public EmailPhoneInputView(View view){
        ButterKnife.bind(this, view);
    }

    public void populate(Email email){
        data.setText(email.getEmail());
        label.setText(email.getLabel());
        if(TextUtils.isEmpty(email.getLabel()))
            label.setVisibility(View.GONE);
    }

    public void populate(PhoneNumber phone){
        data.setText(phone.getPhone());
        label.setText(phone.getLabel());
        if(TextUtils.isEmpty(phone.getLabel()))
            label.setVisibility(View.GONE);
    }
}
