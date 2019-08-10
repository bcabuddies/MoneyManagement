package com.bcabuddies.moneymanagement.ViewUser.View;

import com.bcabuddies.moneymanagement.Base.BaseView;
import com.bcabuddies.moneymanagement.Model.TransactionModel;

public interface ViewUserView extends BaseView {

    void getTransactions(TransactionModel transactions);

    void userCompleted();
}
