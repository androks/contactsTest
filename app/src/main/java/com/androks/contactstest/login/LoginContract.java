package com.androks.contactstest.login;

import android.content.Intent;

import com.androks.contactstest.BasePresenter;
import com.androks.contactstest.BaseView;
import com.firebase.ui.auth.IdpResponse;

/**
 * Created by androks on 15.07.17.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter>{
        void navigateToMainActivity();
        void showFirebaseAuthUI(int requestCode);
        void showLoginFailMessage(IdpResponse response);
    }

    interface Presenter extends BasePresenter {

        void result(int requestCode, int resultCode, Intent data);

        void checkIfSignedUp();

        void signUp();
    }

}
