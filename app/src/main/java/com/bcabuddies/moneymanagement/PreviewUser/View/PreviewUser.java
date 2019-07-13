package com.bcabuddies.moneymanagement.PreviewUser.View;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.R;

public class PreviewUser extends AppCompatActivity implements PreviewUserView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
