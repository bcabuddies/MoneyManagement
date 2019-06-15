package com.bcabuddies.moneymanagement.Login.Presenter;

import androidx.annotation.NonNull;
import android.util.Log;

import com.bcabuddies.moneymanagement.Login.View.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPresenterImp implements loginPresenter {
    FirebaseAuth auth;
    private LoginView loginView;

    public LoginPresenterImp(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.v("mGoogleSignIn", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    try {
                        loginView.loginError(task.getException().getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    loginView.googleLoginSuccess();
                }
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
