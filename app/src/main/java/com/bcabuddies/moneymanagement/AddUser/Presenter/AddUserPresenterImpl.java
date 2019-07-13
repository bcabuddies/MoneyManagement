package com.bcabuddies.moneymanagement.AddUser.Presenter;

import android.os.Bundle;

import com.bcabuddies.moneymanagement.AddPhoto.AddPhoto;
import com.bcabuddies.moneymanagement.AddUser.View.AddUserView;
import com.bcabuddies.moneymanagement.utils.Utils;

public class AddUserPresenterImpl implements AddUserPresenter {

    private AddUserView view;

    @Override
    public void attachView(AddUserView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getUserData() {

    }

    @Override
    public void addPhoto(String type) {
        Bundle data = new Bundle();
        data.putString("type", type);
        Utils.setIntentExtra(view.getContext(), AddPhoto.class, "data", data);
    }

    @Override
    public void showDatePicker() {

    }

    @Override
    public void checkDetailsAndSubmit() {
        /*
            code 0 = Name error
            code 1 = Age error
            code 2 = Amount error
            code 3 = Interest error
        */
    }
}
