package com.androks.contactstest.login;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.ResultCodes;
import com.google.firebase.auth.FirebaseAuth;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by androks on 15.07.17.
 */

public class LoginPresenter implements LoginContract.Presenter {

    private static final int RC_SIGN_IN = 124;

    @NonNull
    private final LoginContract.View loginView;


    @NonNull
    private CompositeDisposable subscriptions;

    public LoginPresenter(@NonNull LoginContract.View loginView) {
        this.loginView = loginView;

        subscriptions = new CompositeDisposable();
        loginView.setPresenter(this);
    }

    @Override
    public void subscribe() {
        checkIfSignedUp();
    }

    @Override
    public void unsubscribe() {
        subscriptions.clear();
    }

    @Override
    public void result(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN && resultCode == ResultCodes.OK)
            loginView.navigateToMainActivity();
        else
            loginView.showLoginFailMessage(IdpResponse.fromResultIntent(data));
    }

    @Override
    public void checkIfSignedUp() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null)
            loginView.navigateToMainActivity();
    }

    @Override
    public void signUp() {
        loginView.showFirebaseAuthUI(RC_SIGN_IN);
    }
}
