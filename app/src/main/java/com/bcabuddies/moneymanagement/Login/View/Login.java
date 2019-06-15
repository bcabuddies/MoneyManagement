package com.bcabuddies.moneymanagement.Login.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bcabuddies.moneymanagement.Login.Presenter.LoginPresenterImp;
import com.bcabuddies.moneymanagement.Login.Presenter.loginPresenter;
import com.bcabuddies.moneymanagement.PostRegistration;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements LoginView {

    private static final String TAG ="Logn.java" ;
    Button googleLoginBtn;

    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1;
    private loginPresenter loginPresenter;
    private FirebaseAuth auth;
    private String fNname, profUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth=FirebaseAuth.getInstance();
        googleLoginBtn = findViewById(R.id.welcome_googlebtn);
        loginPresenter = new LoginPresenterImp(auth);
        loginPresenter.attachView(this);

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("751456404768-4d2026772jh67jk1286mardp0c9dl6kc.apps.googleusercontent.com")
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        googleLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleLogin();
            }
        });

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
                fNname = account.getDisplayName();
                //profUrl = account.getPhotoUrl().toString();
                profUrl = account.getPhotoUrl().toString();
                //Remove thumbnail url and replace the original part of the Url with the new part
                profUrl = profUrl.substring(0, profUrl.length() - 15) + "s400-c/photo.jpg";
                Log.e("googleRet", "name: " + fNname);
                Log.e("googleRet", "pofile: " + profUrl);
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
        Utils.showMessage(this, "Google Login Success!");
        Bundle data = new Bundle();
        Log.e(TAG, "thirdPartyLoginSuccess: name and profile " + fNname + " " + profUrl);
        data.putString("name", fNname);
        data.putString("profile", profUrl);
        Utils.setIntentExtra(this, PostRegistration.class, "data", data);
    }

    @Override
    public void loginError(String error) {
        Utils.showMessage(this, "Login error " + error);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
