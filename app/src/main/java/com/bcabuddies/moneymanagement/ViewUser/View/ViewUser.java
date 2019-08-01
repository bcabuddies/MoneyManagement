package com.bcabuddies.moneymanagement.ViewUser.View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.UpdateTransaction.View.UpdateTransaction;
import com.bcabuddies.moneymanagement.ViewUser.Presenter.ViewUserPresenter;
import com.bcabuddies.moneymanagement.ViewUser.Presenter.ViewUserPresenterImpl;
import com.bcabuddies.moneymanagement.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ViewUser extends AppCompatActivity implements ViewUserView {

    private final String TAG = "ViewUser.java";
    @BindView(R.id.view_user_updateBtn)
    Button viewUserUpdateBtn;
    @BindView(R.id.view_user_completeBtn)
    Button viewUserCompleteBtn;
    private ViewUserPresenter presenter;
    private Bundle data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);
        ButterKnife.bind(this);

        presenter = new ViewUserPresenterImpl();
        presenter.attachView(this);

        data = getIntent().getBundleExtra("data");

        Log.e(TAG, "onCreate: bundle data " + data.getString("uid"));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick({R.id.view_user_updateBtn, R.id.view_user_completeBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.view_user_updateBtn:
                Utils.setIntentExtra(this, UpdateTransaction.class,"data",data);
                break;
            case R.id.view_user_completeBtn:
                break;
        }
    }
}
