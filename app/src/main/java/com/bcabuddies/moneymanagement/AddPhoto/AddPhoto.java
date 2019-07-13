package com.bcabuddies.moneymanagement.AddPhoto;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddPhoto extends AppCompatActivity {

    @BindView(R.id.add_photo_tv)
    TextView addPhotoTv;
    private String type;
    private Bundle bundle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        ButterKnife.bind(this);

        bundle = getIntent().getBundleExtra("data");
        type = bundle.getString("type");
        addPhotoTv.setText("Add " + type + " Photo");
    }
}
