package com.bcabuddies.moneymanagement.PreviewUser.View;

import com.bcabuddies.moneymanagement.Base.BaseView;

public interface PreviewUserView extends BaseView {
    void errorMsg(String msg);

    void showProgress(double progress);

    void everythingDone();
}
