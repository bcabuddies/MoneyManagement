package com.bcabuddies.moneymanagement.Login.Presenter;

import com.bcabuddies.moneymanagement.Base.BasePresenter;
import com.bcabuddies.moneymanagement.Login.View.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public interface loginPresenter extends BasePresenter<LoginView> {
    void firebaseAuthWithGoogle(GoogleSignInAccount account);

    void checkLogin();
}
