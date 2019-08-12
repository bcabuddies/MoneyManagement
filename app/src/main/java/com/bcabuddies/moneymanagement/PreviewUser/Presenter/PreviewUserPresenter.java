package com.bcabuddies.moneymanagement.PreviewUser.Presenter;

import android.widget.TextView;

import com.bcabuddies.moneymanagement.Base.BasePresenter;
import com.bcabuddies.moneymanagement.PreviewUser.View.PreviewUserView;

import java.util.ArrayList;

public interface PreviewUserPresenter extends BasePresenter<PreviewUserView> {
    void addText(TextView previewTextTV, TextView previewDataTV);

    void submitData(ArrayList<String> imageList);

    void submitTakeData();
}
