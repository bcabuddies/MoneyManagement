package com.bcabuddies.moneymanagement.ViewUser.Presenter;

import com.bcabuddies.moneymanagement.ViewUser.View.ViewUserView;

public class ViewUserPresenterImpl implements ViewUserPresenter {

    private ViewUserView view;

    @Override
    public void attachView(ViewUserView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
