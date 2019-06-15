package com.bcabuddies.moneymanagement.Login.View;

import com.bcabuddies.moneymanagement.Base.BaseView;

public interface LoginView extends BaseView {
    void googleLoginSuccess();
    void loginError(String error);
}
