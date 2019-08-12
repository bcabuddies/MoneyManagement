package com.bcabuddies.moneymanagement.Accounts.Presenter;

import com.bcabuddies.moneymanagement.Accounts.View.AccountsView;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AccountsPresenterImpl implements AccountsPresenter {

    private AccountsView view;

    @Override
    public void attachView(AccountsView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getCash() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Admins").document("Accounts").get()
                .addOnCompleteListener(task -> {
                    String cash = Utils.AESDecryptionString(
                            Objects.requireNonNull(
                                    Objects.requireNonNull(
                                            task.getResult()).getString("cash")));
                    view.setCash("Cash = "
                            + cash + view.getContext().getResources().getString(R.string.rupee_symbol));
                });
    }
}
