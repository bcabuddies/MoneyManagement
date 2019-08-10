package com.bcabuddies.moneymanagement.UpdateTransaction.View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.UpdateTransaction.Presenter.UpdateTransactionPresenter;
import com.bcabuddies.moneymanagement.UpdateTransaction.Presenter.UpdateTransactionPresenterImpl;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateTransaction extends AppCompatActivity implements UpdateTransactionView {

    private static final String TAG = "UpdateTransaction";
    @BindView(R.id.updateTransaction_intLayout)
    TextInputLayout intLayout;
    @BindView(R.id.updateTransaction_amtLayout)
    TextInputLayout amtLayout;
    @BindView(R.id.updateTransaction_amtCheckBox)
    CheckBox amtCheckBox;
    @BindView(R.id.updateTransaction_intCheckBox)
    CheckBox intCheckBox;
    @BindView(R.id.updateTransaction_bothCheckBox)
    CheckBox bothCheckBox;
    @BindView(R.id.updateTransaction_btnGive)
    Button btnGive;
    @BindView(R.id.updateTransaction_btnTake)
    Button btnTake;
    private UpdateTransactionPresenter updateTransactionPresenter;
    private Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transaction);
        ButterKnife.bind(this);

        data = Objects.requireNonNull(getIntent().getExtras()).getBundle("data");
        assert data != null;
        Log.e(TAG, "onCreate: customer id in data " + data.getString("uid"));

        init();
    }

    private void init() {
        updateTransactionPresenter = new UpdateTransactionPresenterImpl(data);
        updateTransactionPresenter.attachView(this);
        intCheckBox.setChecked(true);
        amtLayout.setEnabled(true);
        Log.e(TAG, "init: total " + data.getString("result"));
        Objects.requireNonNull(intLayout.getEditText()).setText(data.getString("result"));
        Objects.requireNonNull(amtLayout.getEditText()).setText(data.getString("total"));
    }

    @Override
    public void errorMsg(String msg) {
        Utils.showMessage(this, msg);
    }

    @Override
    public void success() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @OnClick({R.id.updateTransaction_amtCheckBox, R.id.updateTransaction_intCheckBox, R.id.updateTransaction_bothCheckBox, R.id.updateTransaction_btnGive, R.id.updateTransaction_btnTake})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.updateTransaction_amtCheckBox:
                enableAmount();
                break;
            case R.id.updateTransaction_intCheckBox:
                break;
            case R.id.updateTransaction_bothCheckBox:
                bothChecked();
                break;
            case R.id.updateTransaction_btnGive:
                String interest = Objects.requireNonNull(intLayout.getEditText()).getText().toString();
                String amount = Objects.requireNonNull(amtLayout.getEditText()).getText().toString();
                updateTransactionPresenter.executeUpdate(interest, amount, "give");
                break;
            case R.id.updateTransaction_btnTake:
                interest = Objects.requireNonNull(intLayout.getEditText()).getText().toString();
                amount = Objects.requireNonNull(amtLayout.getEditText()).getText().toString();
                updateTransactionPresenter.executeUpdate(interest, amount, "take");
                break;
        }
    }

    private void bothChecked() {
        intCheckBox.setChecked(true);
        amtCheckBox.setChecked(true);

        bothCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                intCheckBox.setChecked(true);
                amtCheckBox.setChecked(true);
            } else {
                amtCheckBox.setChecked(false);
            }
        });
    }

    private void enableAmount() {
        amtLayout.setEnabled(true);
        amtCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                amtLayout.setEnabled(true);
            }
        });
    }
}
