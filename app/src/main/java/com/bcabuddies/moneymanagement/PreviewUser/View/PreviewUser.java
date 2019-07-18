package com.bcabuddies.moneymanagement.PreviewUser.View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.R;

public class PreviewUser extends AppCompatActivity implements PreviewUserView {

    private final String TAG = "PreviewUser.java";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        Log.e(TAG, "onCreate: preview class created ");

        Bundle data = getIntent().getExtras();
        assert data != null;
        UsersParcelable parcelable = data.getParcelable("data");
        assert parcelable != null;
        Log.e(TAG, "onCreate: parcelable in PreviewUser.class " + parcelable.toString());
    }

    @Override
    public Context getContext() {
        return this;
    }
}
