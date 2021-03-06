package com.bcabuddies.moneymanagement.AddUser.Presenter;

import com.bcabuddies.moneymanagement.AddUser.View.AddUserView;
import com.bcabuddies.moneymanagement.Base.BasePresenter;
import com.bcabuddies.moneymanagement.Model.UsersParcelable;

public interface AddUserPresenter extends BasePresenter<AddUserView> {

    void showDatePicker();

    void checkDetailsAndSubmit(UsersParcelable parcelable);

    void setUserID();

    void getCash();
}
