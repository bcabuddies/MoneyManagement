package com.bcabuddies.moneymanagement.ViewUser.Presenter;

import com.bcabuddies.moneymanagement.Base.BasePresenter;
import com.bcabuddies.moneymanagement.ViewUser.View.ViewUserView;

public interface ViewUserPresenter extends BasePresenter<ViewUserView> {

    void getTransaction(String userID);

    void completeFeature(String amount);

    void updateIntAmount(String intAmt, String uid);

    void getUser(String userID);
}
