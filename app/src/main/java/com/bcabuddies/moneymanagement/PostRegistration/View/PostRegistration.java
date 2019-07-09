package com.bcabuddies.moneymanagement.PostRegistration.View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.Home.View.Home;
import com.bcabuddies.moneymanagement.PostRegistration.Presenter.PostRegPresenter;
import com.bcabuddies.moneymanagement.PostRegistration.Presenter.PostRegPresenterImpl;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

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
    @BindView(R.id.post_profileView)
    CircleImageView postProfileView;

    private PostRegPresenter postRegPresenter;
    private Bundle bundle;
    private final String TAG = "PostReg.java";

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
                    .into(postProfileView);
        }

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();

        postRegPresenter = new PostRegPresenterImpl(firebaseFirestore, user);
        postRegPresenter.attachView(this);

        //check if user is admin
        postRegPresenter.checkAdmin(Objects.requireNonNull(bundle.get("email")).toString());
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick(R.id.post_nextBtn)
    public void onViewClicked() {
        //add user data to Database
        postRegPresenter.uploadData(bundle);
    }

    @Override
    public void errorMsg(String error) {
        //some error
        Utils.showMessage(this, error);
        Log.e(TAG, "errorMsg: error " + error);
    }

    @Override
    public void uploadSuccess() {
        Log.e(TAG, "uploadSuccess: success ");
        Utils.setIntentNoBackLog(this, Home.class);
    }

    @Override
    public void userIsNotAdmin() {
        //user is not admin
        Log.e(TAG, "userIsNotAdmin: user is not admin ");
    }

    @Override
    public void userIsAdmin() {
        //user is admin
        Log.e(TAG, "userIsAdmin: user is admin");
    }
}
