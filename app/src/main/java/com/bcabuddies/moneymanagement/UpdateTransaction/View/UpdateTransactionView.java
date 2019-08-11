package com.bcabuddies.moneymanagement.UpdateTransaction.View;

import com.bcabuddies.moneymanagement.Base.BaseView;

import java.util.ArrayList;

public interface UpdateTransactionView extends BaseView {
    void errorMsg(String msg);

    void success();

    void showData(String amount, String intAmt);

    void showUserName(ArrayList<String> userNameList);
}
