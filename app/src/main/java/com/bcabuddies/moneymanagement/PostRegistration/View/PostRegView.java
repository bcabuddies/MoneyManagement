package com.bcabuddies.moneymanagement.PostRegistration.View;

import com.bcabuddies.moneymanagement.Base.BaseView;

public interface PostRegView extends BaseView {
    void errorMsg(String error);

    void uploadSuccess();

    void userIsNotAdmin();

    void userIsAdmin();
}
