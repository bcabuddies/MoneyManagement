package com.bcabuddies.moneymanagement.PreviewUser.View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.Home.View.Home;
import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.PreviewUser.Adapter.GridAdapter;
import com.bcabuddies.moneymanagement.PreviewUser.Presenter.PreviewUserPresenter;
import com.bcabuddies.moneymanagement.PreviewUser.Presenter.PreviewUserPresenterImpl;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreviewUser extends AppCompatActivity implements PreviewUserView {

    private final String TAG = "PreviewUser.java";
    @BindView(R.id.preview_text_TV)
    TextView previewTextTV;
    @BindView(R.id.preview_submitBtn)
    Button previewSubmitBtn;
    @BindView(R.id.preview_gridView)
    GridView previewGridView;
    @BindView(R.id.preview_data_TV)
    TextView previewDataTV;
    @BindView(R.id.preview_progressBar)
    ProgressBar previewProgressBar;

    private PreviewUserPresenter previewUserPresenter;
    private ArrayList<String> list = new ArrayList<>();
    private UsersParcelable parcelable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        Log.e(TAG, "onCreate: preview class created ");

        Bundle data = getIntent().getExtras();
        assert data != null;
        parcelable = data.getParcelable("data");
        assert parcelable != null;
        Log.e(TAG, "onCreate: parcelable in PreviewUser.class " + parcelable.toString());

        previewUserPresenter = new PreviewUserPresenterImpl(parcelable);
        previewUserPresenter.attachView(this);
        previewUserPresenter.addText(previewTextTV, previewDataTV);

        if (parcelable.getType().equals("give")) {
            list.add(parcelable.getAadhar());
            list.add(parcelable.getAddress());
            list.add(parcelable.getReference());
            list.add(parcelable.getRelative());
        }

        previewGridView.setAdapter(new GridAdapter(list, this));
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick(R.id.preview_submitBtn)
    public void onViewClicked() {
        previewSubmitBtn.setEnabled(false);
        if (parcelable.getType().equals("give"))
            previewUserPresenter.submitData(list);
        else if (parcelable.getType().equals("take"))
            previewUserPresenter.submitTakeData();
    }

    @Override
    public void errorMsg(String msg) {
        Utils.showMessage(this, msg);
        previewSubmitBtn.setEnabled(true);
    }

    @Override
    public void showProgress(double progress) {
        previewProgressBar.setProgress((int) progress);
    }

    @Override
    public void everythingDone() {
        Utils.setIntentNoBackLog(this, Home.class);
        previewSubmitBtn.setEnabled(true);
    }
}
