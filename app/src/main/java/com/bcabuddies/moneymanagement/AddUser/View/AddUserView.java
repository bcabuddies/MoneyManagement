package com.bcabuddies.moneymanagement.AddUser.View;

import com.bcabuddies.moneymanagement.Base.BaseView;
import com.bcabuddies.moneymanagement.Model.UsersParcelable;

public interface AddUserView extends BaseView {
    void TextFieldsError(String error);

    void pictureError(String error);

    void dataForParcel(UsersParcelable usersParcelable);

    void everythingFine();
}
