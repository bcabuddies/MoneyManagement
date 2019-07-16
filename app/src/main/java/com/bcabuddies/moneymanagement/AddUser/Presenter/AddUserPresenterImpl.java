package com.bcabuddies.moneymanagement.AddUser.Presenter;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.moneymanagement.AddPhoto.AddPhoto;
import com.bcabuddies.moneymanagement.AddUser.View.AddUserView;
import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddUserPresenterImpl implements AddUserPresenter {

    private AddUserView view;
    private final Calendar myCalendar = Calendar.getInstance();
    private final String TAG = "AddUserPresenter.java";

    @Override
    public void attachView(AddUserView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getUserData() {

    }

    @Override
    public void addPhoto(String type) {
        Bundle data = new Bundle();
        data.putString("type", type);
        Utils.setIntentExtra(view.getContext(), AddPhoto.class, "data", data);
    }

    @Override
    public void showDatePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        new DatePickerDialog(view.getContext(), date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        String date = sdf.format(myCalendar.getTime());
        view.showDate(date);
    }

    @Override
    public void checkDetailsAndSubmit(UsersParcelable parcelable) {
        /*
            code 0 = Name error
            code 1 = Age error
            code 2 = Amount error
            code 3 = Interest error
        */
        Log.e(TAG, "checkDetailsAndSubmit: userParcel in Presenter " + parcelable.toString());

        if (parcelable.getName().isEmpty())
            view.TextFieldsError("Please Fill Name", 0);
        if (parcelable.getAge().isEmpty())
            view.TextFieldsError("Please Fill Age", 1);
        if (parcelable.getAmount().isEmpty())
            view.TextFieldsError("Please Fill Amount", 2);
        if (parcelable.getIntRate().isEmpty())
            view.TextFieldsError("Please Fill Interest", 3);

    }

    @Override
    public void imagePost(byte[] thumb_byte) {

    }
}
