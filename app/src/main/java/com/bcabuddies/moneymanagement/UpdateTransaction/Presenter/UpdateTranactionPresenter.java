package com.bcabuddies.moneymanagement.UpdateTransaction.Presenter;

import com.bcabuddies.moneymanagement.Base.BasePresenter;
import com.bcabuddies.moneymanagement.UpdateTransaction.View.UpdateTranactionView;

public interface UpdateTranactionPresenter extends BasePresenter<UpdateTranactionView> {
    void executeUpdate(String interest, String amount);
}
