package com.bcabuddies.moneymanagement.AddUser.Presenter;

import com.bcabuddies.moneymanagement.AddUser.View.AddUserView;
import com.bcabuddies.moneymanagement.Base.BasePresenter;

public interface AddUserPresenter extends BasePresenter<AddUserView> {
    void getUserData();

    void addPhoto(String type);

    void showDatePicker();

    void checkDetailsAndSubmit();
}
