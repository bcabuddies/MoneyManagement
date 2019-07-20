package com.bcabuddies.moneymanagement.AddUser.View;

import com.bcabuddies.moneymanagement.Base.BaseView;

public interface AddUserView extends BaseView {
    void TextFieldsError(String error, int code);

    void showDate(String date);

    void userID(String id);
}
