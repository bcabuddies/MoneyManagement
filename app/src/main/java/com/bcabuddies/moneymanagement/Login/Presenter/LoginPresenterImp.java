package com.bcabuddies.moneymanagement.Login.Presenter;

import android.util.Log;

import com.bcabuddies.moneymanagement.Login.View.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginPresenterImp implements loginPresenter {
    private FirebaseAuth auth;
    private LoginView loginView;

    public LoginPresenterImp(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.v("mGoogleSignIn", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                try {
                    loginView.loginError(Objects.requireNonNull(task.getException()).getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                loginView.googleLoginSuccess();
            }
        });
    }

    @Override
    public void attachView(LoginView view) {
        loginView = view;
    }

    @Override
    public void detachView() {
        loginView = null;
    }
}
