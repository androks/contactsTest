package com.androks.contactstest.contactdetail;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.androks.contactstest.R;
import com.androks.contactstest.addeditcontact.AddEditContactActivity;
import com.androks.contactstest.addeditcontact.AddEditContactFragment;
import com.androks.contactstest.data.entity.Email;
import com.androks.contactstest.data.entity.PhoneNumber;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactDetailsFragment extends Fragment implements ContactDetailContract.View {

    public static final String ARGUMENT_CONTACT_ID = "ARGUMENT_CONTACT_ID";

    public static final int REQUEST_EDIT_CONTACT = 188;

    @BindView(R.id.ll_phone_container) LinearLayout phoneContainerLl;
    @BindView(R.id.ll_email_container) LinearLayout emailContainerLl;

    private ContactDetailContract.Presenter presenter;

    private Unbinder unbinder;

    public ContactDetailsFragment() {
        // Required empty public constructor
    }

    public static ContactDetailsFragment newInstance(String contactId){
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT_CONTACT_ID, contactId);
        ContactDetailsFragment fragment = new ContactDetailsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_contact_details, container, false);
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
    public void setPresenter(ContactDetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showFullName(String name, String surname){
        ((AppCompatActivity)getActivity()).getSupportActionBar()
                .setTitle(new StringBuilder(name).append(" ").append(surname));
    }

    @Override
    public void showPhones(List<PhoneNumber> phoneNumbers) {
        phoneContainerLl.removeAllViews();
        for (PhoneNumber phoneNumber : phoneNumbers) {
            LayoutInflater inflater =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_email_phone, phoneContainerLl, false);
            EmailPhoneInputView vh = new EmailPhoneInputView(view);
            vh.populate(phoneNumber);
            phoneContainerLl.addView(view);
        }
    }

    @Override
    public void showEmails(List<Email> emails) {
        emailContainerLl.removeAllViews();
        for (Email email : emails) {
            LayoutInflater inflater =
                    (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_email_phone, emailContainerLl, false);
            EmailPhoneInputView vh = new EmailPhoneInputView(view);
            vh.populate(email);
            emailContainerLl.addView(view);
        }
    }

    @Override
    public void showEditContact(String contactId) {
        Intent intent = new Intent(getContext(), AddEditContactActivity.class);
        intent.putExtra(AddEditContactFragment.ARGUMENT_EDIT_CONTACT_ID, contactId);
        startActivityForResult(intent, REQUEST_EDIT_CONTACT);
    }

    @Override
    public void showContactDeleted() {
        getActivity().setResult(ContactDetailActivity.RESULT_DELETED);
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.onActivityResult(requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void showSendMailUi(String email) {
        Intent gmail = new Intent(Intent.ACTION_VIEW);
        gmail.putExtra(Intent.ACTION_SENDTO, Uri.parse("mailto:" + email));
        startActivity(Intent.createChooser(gmail, getString(R.string.choose_email_client)));
    }

    @Override
    public void showCallUi(String phone) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phone));
        startActivity(callIntent);
    }

    @Override
    public void showUserSavedMessage() {
        showSnackBar(getString(R.string.user_saved));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit:
                presenter.editContact();
                return true;
            case R.id.delete:
                presenter.deleteContact();
                return true;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contact_details_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    private void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
