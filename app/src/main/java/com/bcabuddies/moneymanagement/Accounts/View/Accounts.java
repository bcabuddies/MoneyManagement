package com.bcabuddies.moneymanagement.Accounts.View;

import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bcabuddies.moneymanagement.Accounts.CashFrag.CashFragment;
import com.bcabuddies.moneymanagement.Accounts.Presenter.AccountsPresenter;
import com.bcabuddies.moneymanagement.Accounts.Presenter.AccountsPresenterImpl;
import com.bcabuddies.moneymanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Accounts extends AppCompatActivity implements AccountsView {

    @BindView(R.id.accounts_cashTV)
    TextView accountsCashTV;
    @BindView(R.id.accounts_cashCardView)
    CardView cashCardView;

    private AccountsPresenter presenter;
    private CashFragment cashFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        ButterKnife.bind(this);

        presenter = new AccountsPresenterImpl();
        presenter.attachView(this);
        presenter.getCash();

        initFragment();
    }

    private void initFragment() {
        cashFragment = new CashFragment();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setCash(String cash) {
        accountsCashTV.setText(cash);
    }

    @OnClick(R.id.accounts_cashCardView)
    public void onViewClicked() {
        setDefaultFragment(cashFragment);
    }

    // This method is used to set the default fragment that will be shown.
    private void setDefaultFragment(Fragment defaultFragment) {
        this.replaceFragment(defaultFragment);
    }

    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment) {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.accounts_frameLayout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
}
