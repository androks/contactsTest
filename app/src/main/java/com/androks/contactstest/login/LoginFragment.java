package com.androks.contactstest.login;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androks.contactstest.R;
import com.androks.contactstest.contacts.ContactsActivity;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements LoginContract.View {

    @BindView(R.id.btn_sign_up) Button btnSingUp;

    private LoginContract.Presenter presenter;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode, resultCode, data);
    }


    @Override
    public void navigateToMainActivity() {
        Intent intent = new Intent(getContext(), ContactsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void showFirebaseAuthUI(int requestCode) {
        startActivityForResult(
                // Get an instance of AuthUI based on the default app
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build()
                                ))
                        .build(),
                requestCode);
    }

    @Override
    public void showLoginFailMessage(IdpResponse response) {
        // Sign in failed
        if (response == null) {
            // Contact pressed back button
            Snackbar.make(btnSingUp, "Sign up was cancelled", Snackbar.LENGTH_SHORT);
            return;
        }

        if (response.getErrorCode() == ErrorCodes.NO_NETWORK) {
            Snackbar.make(btnSingUp, "No network connection", Snackbar.LENGTH_SHORT);
            return;
        }

        if (response.getErrorCode() == ErrorCodes.UNKNOWN_ERROR)
            Snackbar.make(btnSingUp, "Unknown fail", Snackbar.LENGTH_SHORT);

    }

    @OnClick(R.id.btn_sign_up)
    public void signUpViaGoogle(){
        presenter.signUp();
    }
}
