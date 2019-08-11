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

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;

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
    @BindView(R.id.updateTransaction_userIDLayout)
    TextInputLayout updateTransactionUserIDLayout;

    private UpdateTransactionPresenter updateTransactionPresenter;
    private SpinnerDialog spinnerDialog;
    private ArrayList<String> userNameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transaction);
        ButterKnife.bind(this);

        try {
            Bundle data = getIntent().getExtras().getBundle("data");

            if (data != null) {
                Log.e(TAG, "onCreate: customer id in data " + data.getString("uid"));
                Objects.requireNonNull(updateTransactionUserIDLayout.getEditText())
                        .setText(data.getString("name"));
                updateTransactionUserIDLayout.setEnabled(false);
                initWithBundle(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onCreate: exception bundle is null " + e.getMessage());
        }

        initWithoutBundle();
    }

    private void initWithoutBundle() {
        updateTransactionPresenter = new UpdateTransactionPresenterImpl();
        updateTransactionPresenter.attachView(this);
        updateTransactionPresenter.addUserName();
        intCheckBox.setChecked(true);
        amtLayout.setEnabled(true);

        Log.e(TAG, "initWithoutBundle: inside initWithout bundle ");

        spinnerDialog = new SpinnerDialog(UpdateTransaction.this,
                userNameList,
                "Select or Search User",
                "Close");

        spinnerDialog.bindOnSpinerListener((item, position) -> {
            String[] parts = item.split(" id:");
            String name = parts[0].trim(); // name
            String id = parts[1].trim(); // id
            Objects.requireNonNull(updateTransactionUserIDLayout.getEditText()).setText(name);
            Log.e(TAG, "initWithoutBundle: uid from Utils " + id);
            updateTransactionPresenter.getUserDetails(id);
        });
    }

    private void initWithBundle(Bundle data) {
        updateTransactionPresenter = new UpdateTransactionPresenterImpl(data);
        updateTransactionPresenter.attachView(this);
        intCheckBox.setChecked(true);
        amtLayout.setEnabled(true);
        Log.e(TAG, "init: total " + data.getString("result"));
        Objects.requireNonNull(intLayout.getEditText()).setText(data.getString("result"));
        Objects.requireNonNull(amtLayout.getEditText()).setText("0");
        intLayout.setHelperText("Interest Amount = " + data.getString("result"));
        amtLayout.setHelperText("Remaining Amount = " + data.getString("total"));
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
    public void showData(String amount, String intAmt) {
        Objects.requireNonNull(intLayout.getEditText()).setText(intAmt);
        Objects.requireNonNull(amtLayout.getEditText()).setText("0");
        intLayout.setHelperText("Interest Amount = " + intAmt);
        amtLayout.setHelperText("Remaining Amount = " + amount);
    }

    @Override
    public void showUserName(ArrayList<String> userNameList) {
        this.userNameList = userNameList;
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick({
            R.id.updateTransaction_amtCheckBox,
            R.id.updateTransaction_userIDLayout,
            R.id.updateTransaction_intCheckBox,
            R.id.updateTransaction_bothCheckBox,
            R.id.updateTransaction_userIDTV,
            R.id.updateTransaction_btnGive,
            R.id.updateTransaction_btnTake})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.updateTransaction_amtCheckBox:
                enableAmount();
                break;
            case R.id.updateTransaction_userIDLayout:
                spinnerDialog.showSpinerDialog();
                Log.e(TAG, "onViewClicked: editText clicked ");
                break;
            case R.id.updateTransaction_userIDTV:
                spinnerDialog.showSpinerDialog();
                Log.e(TAG, "onViewClicked: editText clicked ");
                break;
            case R.id.updateTransaction_intCheckBox:
                break;
            case R.id.updateTransaction_bothCheckBox:
                bothChecked();
                break;
            case R.id.updateTransaction_btnGive:
                update("give");
                break;
            case R.id.updateTransaction_btnTake:
                update("take");
                break;
        }
    }

    private void update(String type) {
        if (type.contains("give")) {
            String interest = Objects.requireNonNull(intLayout.getEditText()).getText().toString();
            String amount = Objects.requireNonNull(amtLayout.getEditText()).getText().toString();
            if (amount == null)
                amount = "0";
            if (interest == null)
                interest = "0";
            updateTransactionPresenter.executeUpdate(interest, amount, "give");
        } else if (type.contains("take")) {
            String interest = intLayout.getEditText().getText().toString();
            String amount = amtLayout.getEditText().getText().toString();
            if (amount == null)
                amount = "0";
            if (interest == null)
                interest = "0";
            updateTransactionPresenter.executeUpdate(interest, amount, "take");
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
