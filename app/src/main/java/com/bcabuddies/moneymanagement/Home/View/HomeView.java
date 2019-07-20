package com.bcabuddies.moneymanagement.Home.View;

import com.bcabuddies.moneymanagement.Base.BaseView;
import com.bcabuddies.moneymanagement.Model.UserModel;

public interface HomeView extends BaseView {
    void setUserList(UserModel user);
}
