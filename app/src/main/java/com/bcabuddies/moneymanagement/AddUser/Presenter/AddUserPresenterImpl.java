package com.bcabuddies.moneymanagement.AddUser.Presenter;

import android.app.DatePickerDialog;
import android.util.Log;

import com.bcabuddies.moneymanagement.AddUser.View.AddUserView;
import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.PreviewUser.View.PreviewUser;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

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

        if (
                !parcelable.getName().isEmpty() &&
                        !parcelable.getName().isEmpty() &&
                        !parcelable.getName().isEmpty() &&
                        !parcelable.getName().isEmpty() &&
                        !parcelable.getName().isEmpty() &&
                        !parcelable.getName().isEmpty() &&
                        !parcelable.getName().isEmpty() &&
                        !parcelable.getName().isEmpty() &&
                        !parcelable.getName().isEmpty()
        ) {
            //all fields and images is filled
            Log.e(TAG, "checkDetailsAndSubmit: fields are complete " + parcelable.toString());
            Utils.setIntentParcel(view.getContext(), PreviewUser.class, "data", parcelable);
        } else {
            Log.e(TAG, "checkDetailsAndSubmit: error in some field ");
        }
    }

    @Override
    public void setUserID() {
        //set a userID for the user
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Admins")
                .document("UserID").get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                String userID = task.getResult().getString("usedID");
                Log.e(TAG, "setUserID: userID's from firebase " + userID);
                int i = 0;
                assert userID != null;
                while (userID.contains(String.valueOf(i))) {
                    i++;
                }
                if (!userID.contains(String.valueOf(i))) {
                    String newUserID = String.valueOf(i);
                    Log.e(TAG, "setUserID: new user ID = " + newUserID);
                    view.userID(newUserID);
                }
            }
        });
    }
}
