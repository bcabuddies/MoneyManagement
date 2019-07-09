package com.bcabuddies.moneymanagement.PostRegistration.View;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.R;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class PostRegistration extends AppCompatActivity implements PostRegView {

    @BindView(R.id.post_nameLayout)
    TextInputLayout postNameLayout;
    @BindView(R.id.post_emailLayout)
    TextInputLayout postEmailLayout;
    @BindView(R.id.post_nextBtn)
    Button postNextBtn;
    @BindView(R.id.circleImageView)
    CircleImageView circleImageView;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_registration);
        ButterKnife.bind(this);

        bundle = getIntent().getBundleExtra("data");
        assert bundle != null;
        if (!bundle.isEmpty()) {
            Objects.requireNonNull(postNameLayout.getEditText()).setText(Objects.requireNonNull(bundle.get("name")).toString());
            Objects.requireNonNull(postEmailLayout.getEditText()).setText(Objects.requireNonNull(bundle.get("email")).toString());
            Glide.with(this).load(Objects.requireNonNull(bundle.get("profile")).toString())
                    .into(circleImageView);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick(R.id.post_nextBtn)
    public void onViewClicked() {
    }
}
