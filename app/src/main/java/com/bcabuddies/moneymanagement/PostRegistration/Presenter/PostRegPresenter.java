package com.bcabuddies.moneymanagement.PostRegistration.Presenter;

import android.os.Bundle;

import com.bcabuddies.moneymanagement.Base.BasePresenter;
import com.bcabuddies.moneymanagement.PostRegistration.View.PostRegView;

public interface PostRegPresenter extends BasePresenter<PostRegView> {
    void uploadData(Bundle bundle);

    void checkAdmin(String email);
}
