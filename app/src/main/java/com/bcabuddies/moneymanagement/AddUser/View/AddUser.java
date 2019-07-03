package com.bcabuddies.moneymanagement.AddUser.View;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.R;

public class AddUser extends AppCompatActivity implements AddUserView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
