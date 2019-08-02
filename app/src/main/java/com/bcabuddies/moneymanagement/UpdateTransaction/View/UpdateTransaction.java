package com.bcabuddies.moneymanagement.UpdateTransaction.View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.UpdateTransaction.Presenter.UpdateTranactionPresenter;
import com.bcabuddies.moneymanagement.UpdateTransaction.Presenter.UpdateTransactionPresenterImpl;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateTransaction extends AppCompatActivity implements UpdateTranactionView {

    @BindView(R.id.updateTransaction_intlayout)
    TextInputLayout intlayout;
    @BindView(R.id.updateTransaction_amtLayout)
    TextInputLayout amtLayout;
    @BindView(R.id.updateTransaction_intCheckBox)
    CheckBox intCheckBox;
    @BindView(R.id.updateTransaction_amtCheckBox)
    CheckBox amtCheckBox;
    @BindView(R.id.updateTransaction_bothCheckBox)
    CheckBox bothCheckBox;
    @BindView(R.id.updateTransaction_btnSubmit)
    Button btnSubmit;
    private UpdateTranactionPresenter updateTranactionPresenter;
    private static final String TAG = "UpdateTransaction";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_transaction);
        ButterKnife.bind(this);

        Bundle data = getIntent().getExtras();
        assert data != null;
        Bundle bundle = data.getBundle("data");
        assert bundle != null;
        Log.e(TAG, "onCreate: customer id in bundle " + bundle.getString("uid"));

        updateTranactionPresenter = new UpdateTransactionPresenterImpl(bundle);
        updateTranactionPresenter.attachView(this);

    }

    @Override
    public void errorMsg(String msg) {
        Utils.showMessage(this, msg);
    }

    @Override
    public Context getContext() {
        return null;
    }

    @OnClick({R.id.updateTransaction_intCheckBox, R.id.updateTransaction_amtCheckBox, R.id.updateTransaction_bothCheckBox, R.id.updateTransaction_btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.updateTransaction_intCheckBox:
                break;
            case R.id.updateTransaction_amtCheckBox:
                break;
            case R.id.updateTransaction_bothCheckBox:
                break;
            case R.id.updateTransaction_btnSubmit:
                String interest = Objects.requireNonNull(intlayout.getEditText()).getText().toString();
                String amount = Objects.requireNonNull(amtLayout.getEditText()).getText().toString();
                updateTranactionPresenter.executeUpdate(interest, amount);
                break;
        }
    }
}
