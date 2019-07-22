package com.bcabuddies.moneymanagement.ViewUser.View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.ViewUser.Presenter.ViewUserPresenter;
import com.bcabuddies.moneymanagement.ViewUser.Presenter.ViewUserPresenterImpl;

public class ViewUser extends AppCompatActivity implements ViewUserView {

    private final String TAG = "ViewUser.java";
    private ViewUserPresenter presenter;
    private Bundle data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user);

        presenter = new ViewUserPresenterImpl();
        presenter.attachView(this);

        data = getIntent().getBundleExtra("data");

        Log.e(TAG, "onCreate: bundle data " + data.getString("uid"));
    }

    @Override
    public Context getContext() {
        return this;
    }
}
