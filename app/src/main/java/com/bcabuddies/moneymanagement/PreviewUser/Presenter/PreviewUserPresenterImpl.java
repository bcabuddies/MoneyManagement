package com.bcabuddies.moneymanagement.PreviewUser.Presenter;

import com.bcabuddies.moneymanagement.PreviewUser.View.PreviewUserView;

public class PreviewUserPresenterImpl implements PreviewUserPresenter {

    PreviewUserView view;
    @Override
    public void attachView(PreviewUserView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
view=null;
    }
}
