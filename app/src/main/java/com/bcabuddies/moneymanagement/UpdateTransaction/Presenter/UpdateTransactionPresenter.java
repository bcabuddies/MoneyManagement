package com.bcabuddies.moneymanagement.UpdateTransaction.Presenter;

import com.bcabuddies.moneymanagement.Base.BasePresenter;
import com.bcabuddies.moneymanagement.UpdateTransaction.View.UpdateTransactionView;

public interface UpdateTransactionPresenter extends BasePresenter<UpdateTransactionView> {
    void executeUpdate(String interest, String amount, String type);
}
