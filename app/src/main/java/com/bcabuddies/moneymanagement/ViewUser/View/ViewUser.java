package com.bcabuddies.moneymanagement.ViewUser.View;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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
    @BindView(R.id.view_user_nameTV)
    TextView viewUserNameTV;
    @BindView(R.id.view_user_dateTV)
    TextView viewUserDateTV;
    @BindView(R.id.view_user_amountLeftTV)
    TextView viewUserAmountLeftTV;
    @BindView(R.id.view_user_amountTV)
    TextView viewUserAmountTV;
    @BindView(R.id.view_user_rateTV)
    TextView viewUserRateTV;
    @BindView(R.id.view_user_userTV)
    TextView viewUserUserTV;

    private Bundle data;
    private ArrayList<TransactionModel> transactionList;
    private TransactionAdapter adapter;
    private ViewUserPresenter presenter;
    private String name, amount, userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        ButterKnife.bind(this);

        data = getIntent().getBundleExtra("data");
        assert data != null;
        userID = data.getString("uid");

        presenter = new ViewUserPresenterImpl();
        presenter.attachView(this);
        presenter.getUser(userID);
        presenter.getTransaction(userID);

        transactionList = new ArrayList<>();
        adapter = new TransactionAdapter(transactionList);
        userTransactionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        userTransactionRecyclerView.setAdapter(adapter);

        assert data != null;
        Log.e(TAG, "onCreate: bundle data " + data.getString("uid"));
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
        presenter.getUser(userID);
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
                completeUser();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private void completeUser() {
        String message = "This will complete and close <b>" + name + "</b> account and will add remaining <b>" + amount + getResources().getString(R.string.rupee_symbol) + "</b> to cash account";
        final AlertDialog alertDialog;
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("Caution!!")
                .setMessage(Html.fromHtml(message, Html.FROM_HTML_MODE_COMPACT))
                .setPositiveButton("Complete", (dialogInterface, i) -> presenter.completeFeature(amount))
                .setNegativeButton("No", (dialogInterface, i) ->
                        Log.e(TAG, "No clicked")).show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.green));
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getColor(R.color.black));
    }

    @Override
    public void getTransactions(TransactionModel transactions) {
        transactionList.add(transactions);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void userCompleted() {
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showUserData(Bundle b) {
        name = data.getString("name");
        String date = data.getString("nextDate");
        amount = b.getString("amount");
        String rate = b.getString("rate");
        String uid = data.getString("uid");
        String intAmt = Utils.calculateInt(amount, rate);

        viewUserNameTV.setText(name);
        viewUserDateTV.setText(date);
        viewUserAmountLeftTV.setText(amount + getString(R.string.rupee_symbol));
        viewUserAmountTV.setText(intAmt + getString(R.string.rupee_symbol));
        viewUserUserTV.setText(uid);
        viewUserRateTV.setText(rate + "%");

        presenter.updateIntAmount(intAmt, uid);
    }
}
