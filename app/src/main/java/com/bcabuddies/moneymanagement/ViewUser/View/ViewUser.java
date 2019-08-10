package com.bcabuddies.moneymanagement.ViewUser.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.moneymanagement.Model.TransactionModel;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.UpdateTransaction.View.UpdateTransaction;
import com.bcabuddies.moneymanagement.ViewUser.Adapter.TransactionAdapter;
import com.bcabuddies.moneymanagement.ViewUser.Presenter.ViewUserPresenter;
import com.bcabuddies.moneymanagement.ViewUser.Presenter.ViewUserPresenterImpl;
import com.bcabuddies.moneymanagement.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewUser extends AppCompatActivity implements ViewUserView {

    private final String TAG = "ViewUser.java";
    @BindView(R.id.view_user_updateBtn)
    Button viewUserUpdateBtn;
    @BindView(R.id.view_user_completeBtn)
    Button viewUserCompleteBtn;
    @BindView(R.id.user_transaction_recyclerView)
    RecyclerView userTransactionRecyclerView;
    @BindView(R.id.home_item_nameTV)
    TextView homeItemNameTV;
    @BindView(R.id.home_item_dateTV)
    TextView homeItemDateTV;
    @BindView(R.id.home_item_amountLeftTV)
    TextView homeItemAmountLeftTV;
    @BindView(R.id.home_item_amountTV)
    TextView homeItemAmountTV;
    @BindView(R.id.home_item_rateTV)
    TextView homeItemRateTV;
    @BindView(R.id.home_item_userTV)
    TextView homeItemUserTV;

    private Bundle data;
    private ArrayList<TransactionModel> transactionList;
    private TransactionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        ButterKnife.bind(this);

        data = getIntent().getBundleExtra("data");
        assert data != null;
        String userID = data.getString("uid");

        ViewUserPresenter presenter = new ViewUserPresenterImpl();
        presenter.attachView(this);
        presenter.getTransaction(userID);

        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(transactionList);
        userTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userTransactionRecyclerView.setAdapter(adapter);

        assert data != null;
        Log.e(TAG, "onCreate: bundle data " + data.getString("uid"));

        setValues();
    }

    @SuppressLint("SetTextI18n")
    private void setValues() {
        /*
        data.putString("uid", uid);
        data.putString("result", String.valueOf(result));
        data.putString("nextDate", date);
        data.putString("name", name);
        data.putString("rate", intAmount);
        data.putString("total", amount);
        */
        String name = data.getString("name");
        String date = data.getString("nextDate");
        String amount = data.getString("total");
        String intAmt = data.getString("result");
        String rate = data.getString("rate");
        String uid = data.getString("uid");

        homeItemNameTV.setText(name);
        homeItemDateTV.setText(date);
        homeItemAmountLeftTV.setText(amount + getString(R.string.rupee_symbol));
        homeItemAmountTV.setText(intAmt + getString(R.string.rupee_symbol));
        homeItemUserTV.setText(uid);
        homeItemRateTV.setText(rate + "%");
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick({R.id.view_user_updateBtn, R.id.view_user_completeBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_user_updateBtn:
                Utils.setIntentExtra(this, UpdateTransaction.class, "data", data);
                break;
            case R.id.view_user_completeBtn:
                break;
        }
    }

    @Override
    public void getTransactions(TransactionModel transactions) {
        transactionList.add(transactions);
        adapter.notifyDataSetChanged();
    }
}
