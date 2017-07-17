package com.androks.contactstest.addeditcontact;

import android.view.View;
import android.widget.EditText;

import com.androks.contactstest.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by androks on 17.07.17.
 */

public class MultiImputedViewsViewHolder {

    @BindView(R.id.et_name) EditText name;
    @BindView(R.id.et_surname) EditText surname;
    @BindView(R.id.et_data) EditText data;
    @BindView(R.id.et_label) EditText label;

    private int id;

    public MultiImputedViewsViewHolder(View view){
        ButterKnife.bind(this, view);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
