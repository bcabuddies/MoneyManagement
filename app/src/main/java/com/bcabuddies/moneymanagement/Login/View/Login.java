package com.bcabuddies.moneymanagement.Login.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.Home.View.Home;
import com.bcabuddies.moneymanagement.Login.Presenter.LoginPresenterImp;
import com.bcabuddies.moneymanagement.Login.Presenter.loginPresenter;
import com.bcabuddies.moneymanagement.PostRegistration.View.PostRegistration;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity implements LoginView {

    private static final String TAG = "Login.java";

    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private loginPresenter loginPresenter;
    private String fName, profUrl, email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        Button googleLoginBtn = findViewById(R.id.welcome_googlebtn);
        loginPresenter = new LoginPresenterImp(auth);
        loginPresenter.attachView(this);

        loginPresenter.checkLogin();

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.google_requestIdToken))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLoginBtn.setOnClickListener(v -> GoogleLogin());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                loginPresenter.firebaseAuthWithGoogle(account);
                assert account != null;
                fName = account.getDisplayName();
                email = account.getEmail();
                //profUrl = account.getPhotoUrl().toString();
                profUrl = Objects.requireNonNull(account.getPhotoUrl()).toString();
                //Remove thumbnail url and replace the original part of the Url with the new part
                profUrl = profUrl.substring(0, profUrl.length() - 15) + "s400-c/photo.jpg";
                Log.e("googleRet", "name: " + fName);
                Log.e("googleRet", "profile: " + profUrl);
                Log.e("mGoogleSignIn", "Google sign in try");

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.e("mGoogleSignIn", "Google sign in failed", e);
                // ...
            }
        }
    }

    private void GoogleLogin() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void googleLoginSuccess() {
        Bundle data = new Bundle();
        Log.e(TAG, "thirdPartyLoginSuccess: name and profile " + fName + " " + profUrl + " " + email);
        data.putString("name", fName);
        data.putString("profile", profUrl);
        data.putString("email", email);
        Utils.setIntentExtra(this, PostRegistration.class, "data", data);
    }

    @Override
    public void loginError(String error) {
        Utils.showMessage(this, error);
    }

    @Override
    public void userLogin() {
        //user already logged in
        Utils.setIntentNoBackLog(this, Home.class);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
