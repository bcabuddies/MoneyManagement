package com.bcabuddies.moneymanagement.Accounts.Presenter;

import com.bcabuddies.moneymanagement.Accounts.View.AccountsView;
import com.bcabuddies.moneymanagement.Base.BasePresenter;

public interface AccountsPresenter extends BasePresenter<AccountsView> {
    void getCash();
}
