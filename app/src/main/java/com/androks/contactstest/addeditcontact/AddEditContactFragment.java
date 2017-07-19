package com.androks.contactstest.addeditcontact;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
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

    //Store emailViews entered by user or created from existing email(in edit mode case)
    private List<EmailPhoneInputViewGroup> emailViews = new ArrayList<>();

    //Store phoneViews entered by user or created from existing phone(in edit mode case)
    private List<EmailPhoneInputViewGroup> phoneViews = new ArrayList<>();

    private Disposable lastEmailInputLayoutDisponsable;

    private Disposable lastPhoneInputLayoutDisponsable;

    private Unbinder unbinder;

    public AddEditContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_edit_contact, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        setHasOptionsMenu(true);
        setRetainInstance(true);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.apply:
                presenter.saveContact(
                        nameEt.getText().toString().trim(),
                        surnameEt.getText().toString().trim(),
                        emailViews,
                        phoneViews);
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.apply_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        nameInputLayout.setError(getString(R.string.field_required));
        showSnackBar(getString(R.string.fill_in_message));
    }

    @Override
    public void clearAllErrors() {
        nameInputLayout.setErrorEnabled(false);
        surnameInputLayout.setErrorEnabled(false);
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
        //Activity loaded in edited mode, so we must populate emails
        emailViews.clear();
        emailContainerLl.removeAllViews();
        for (Email email : emails) {
            LayoutInflater inflater =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_email_input, emailContainerLl, false);
            //Binding and populating our view
            EmailPhoneInputViewGroup vh = new EmailPhoneInputViewGroup(view);
            vh.populate(email);

            emailViews.add(vh);
            //Add view to view group
            emailContainerLl.addView(view);
        }
    }

    @Override
    public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
        //Activity loaded in edited mode, so we must populate emails
        phoneViews.clear();
        phoneContainerLl.removeAllViews();

        for (PhoneNumber phoneNumber : phoneNumbers) {
            LayoutInflater inflater =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_phone_input, phoneContainerLl, false);
            //Binding and populating our view
            EmailPhoneInputViewGroup vh = new EmailPhoneInputViewGroup(view);
            vh.populate(phoneNumber);

            phoneViews.add(vh);
            //Add view to view group
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
        View view = inflater.inflate(R.layout.item_email_input, emailContainerLl, false);
        //Bind view
        EmailPhoneInputViewGroup vh = new EmailPhoneInputViewGroup(view);

        //Remove listener from last email input field if it is not null
        if (lastEmailInputLayoutDisponsable != null)
            lastEmailInputLayoutDisponsable.dispose();

        //Add new listener to last email field
        lastEmailInputLayoutDisponsable = RxTextView.textChangeEvents(vh.data)
                .observeOn(AndroidSchedulers.mainThread())
                //Check if text field is empty
                .map(textViewTextChangeEvent -> textViewTextChangeEvent.text().toString().trim().isEmpty())
                .subscribe(isTextEmpty -> {
                    //Hide or show label field and add more btn
                    if (isTextEmpty) {
                        vh.labelInputLayout.setVisibility(View.GONE);
                        addEmailBtn.setEnabled(false);
                    } else {
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
        View view = inflater.inflate(R.layout.item_phone_input, phoneContainerLl, false);
        //Bind view
        EmailPhoneInputViewGroup vh = new EmailPhoneInputViewGroup(view);

        //Remove listener from last phone input field if it is not null
        if (lastPhoneInputLayoutDisponsable != null)
            lastPhoneInputLayoutDisponsable.dispose();

        //Add new listener to last phone field
        lastPhoneInputLayoutDisponsable = RxTextView.textChangeEvents(vh.data)
                .observeOn(AndroidSchedulers.mainThread())
                //Check if text field is empty
                .map(textViewTextChangeEvent -> textViewTextChangeEvent.text().toString().trim().isEmpty())
                .subscribe(isTextEmpty -> {
                    //Hide or show label field and add more btn
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
        Snackbar.make(getActivity().findViewById(R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
