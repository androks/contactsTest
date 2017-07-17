package com.androks.contactstest.addeditcontact;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.androks.contactstest.R;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditContactFragment extends Fragment implements AddEditContactContract.View {

    public static final String ARGUMENT_EDIT_CONTACT_ID = "EDIT_CONTACT_ID";

    @BindView(R.id.input_layout_name) TextInputLayout nameInputLayout;
    @BindView(R.id.input_layout_surname) TextInputLayout surnameInputLayout;
    @BindView(R.id.et_name) EditText nameEt;
    @BindView(R.id.et_surname) EditText surnameEt;
    @BindView(R.id.ll_email_container) LinearLayout emailContainerLl;
    @BindView(R.id.ll_phone_container) LinearLayout phoneContainerLl;
    @BindView(R.id.btn_add_phone) View addPhoneBtn;
    @BindView(R.id.btn_add_email) View addEmailBtn;

    private AddEditContactContract.Presenter presenter;

    private List<MultiImputedViewsViewHolder> emailViews = new ArrayList<>();

    private List<MultiImputedViewsViewHolder> phoneViews = new ArrayList<>();

    private Disposable lastEmailInputLayoutDisponsable;

    private Disposable lastPhoneInputLayoutDisponsable;

    public AddEditContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_edit_contact, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getActivity().findViewById(R.id.fab_confirm_contact)
                .setOnClickListener(__ -> {
                    presenter.saveContact(
                            nameEt.getText().toString(),
                            surnameEt.getText().toString(),
                            emailViews,
                            phoneViews);
                });
    }

    public AddEditContactFragment newInstance() {
        return new AddEditContactFragment();
    }

    @Override
    public void setPresenter(AddEditContactContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showEmptyNameError() {
        nameInputLayout.setError(getActivity().getString(R.string.field_required));
    }

    @Override
    public void showEmptySurnameError() {
        surnameInputLayout.setError(getString(R.string.field_required));
    }

    @Override
    public void showNoEmailError() {
        showSnackBar(getString(R.string.no_emails_error));
    }

    @Override
    public void showNoPhoneError() {
        showSnackBar(getString(R.string.no_phones_error));
    }

    @Override
    public void showContactsList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setName(String name) {
        nameEt.setText(name);
    }

    @Override
    public void setSurname(String surname) {
        surnameEt.setText(surname);
    }

    @Override
    public void setEmails(List<Email> emails) {
        emailContainerLl.removeAllViews();
        for (Email email : emails) {
            LayoutInflater inflater =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_email_phone_input, emailContainerLl, false);
            MultiImputedViewsViewHolder vh = new MultiImputedViewsViewHolder(view);
            vh.populate(email);
            vh.dataInputLayout.setHint(getString(R.string.email_hint));
            emailViews.add(vh);
            emailContainerLl.addView(view);
        }
    }

    @Override
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        phoneContainerLl.removeAllViews();
        for (PhoneNumber phoneNumber : phoneNumbers) {
            LayoutInflater inflater =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_email_phone_input, phoneContainerLl, false);
            MultiImputedViewsViewHolder vh = new MultiImputedViewsViewHolder(view);
            vh.populate(phoneNumber);
            vh.dataInputLayout.setHint(getString(R.string.phone_hint));
            phoneViews.add(vh);
            phoneContainerLl.addView(view);
        }
    }

    @Override
    public void showUnknownError() {
        showSnackBar(getString(R.string.unknown_error));
    }

    @Override
    public void showNewEmailInputLayout() {
        LayoutInflater inflater =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_email_phone_input, emailContainerLl, false);
        MultiImputedViewsViewHolder vh = new MultiImputedViewsViewHolder(view);
        vh.dataInputLayout.setHint(getString(R.string.email_hint));
        if (lastEmailInputLayoutDisponsable != null)
            lastEmailInputLayoutDisponsable.dispose();
        lastEmailInputLayoutDisponsable = RxTextView.textChangeEvents(vh.data)
                .sample(TimeUnit.SECONDS.toSeconds(1), TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(textViewTextChangeEvent -> textViewTextChangeEvent.text().toString().isEmpty())
                .subscribe(isTextEmpty -> {
                    if (isTextEmpty) {
                        vh.labelInputLayout.setVisibility(View.GONE);
                        addEmailBtn.setEnabled(false);
                    }
                    else {
                        vh.labelInputLayout.setVisibility(View.VISIBLE);
                        addEmailBtn.setEnabled(true);
                    }
                });
        emailViews.add(vh);
        emailContainerLl.addView(view);
    }

    @Override
    public void showNewPhoneInputLayout() {
        LayoutInflater inflater =
                (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_email_phone_input, phoneContainerLl, false);
        MultiImputedViewsViewHolder vh = new MultiImputedViewsViewHolder(view);
        vh.dataInputLayout.setHint(getString(R.string.phone_hint));
        if (lastPhoneInputLayoutDisponsable != null)
            lastPhoneInputLayoutDisponsable.dispose();
        lastPhoneInputLayoutDisponsable = RxTextView.textChangeEvents(vh.data)
                .sample(TimeUnit.SECONDS.toSeconds(1), TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(textViewTextChangeEvent -> textViewTextChangeEvent.text().toString().isEmpty())
                .subscribe(isTextEmpty -> {
                    if (isTextEmpty) {
                        vh.labelInputLayout.setVisibility(View.GONE);
                        addPhoneBtn.setEnabled(false);
                    } else {
                        vh.labelInputLayout.setVisibility(View.VISIBLE);
                        addPhoneBtn.setEnabled(true);
                    }
                });
        phoneViews.add(vh);
        phoneContainerLl.addView(view);
    }

    @OnClick(R.id.btn_add_email)
    void onAddEmailClick() {
        presenter.addNewEmailInputLayout();
    }

    @OnClick(R.id.btn_add_phone)
    void onAddPhoneClick() {
        presenter.addNewPhoneInputLayout();
    }

    private void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }
}
