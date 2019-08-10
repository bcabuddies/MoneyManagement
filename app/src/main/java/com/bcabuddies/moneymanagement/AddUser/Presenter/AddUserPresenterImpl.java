package com.bcabuddies.moneymanagement.AddUser.Presenter;

import android.app.DatePickerDialog;
import android.util.Log;
import android.widget.Toast;

import com.bcabuddies.moneymanagement.AddUser.View.AddUserView;
import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.PreviewUser.View.PreviewUser;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddUserPresenterImpl implements AddUserPresenter {

    private final Calendar myCalendar = Calendar.getInstance();
    private final String TAG = "AddUserPresenter.java";
    private AddUserView view;

    @Override
    public void attachView(AddUserView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
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
        String myFormat = "dd/MM/yy"; //In which style you want the date to show
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
        if (parcelable.getPhone().isEmpty())
            view.TextFieldsError("Please Add Phone", 4);

        if (
                !parcelable.getName().isEmpty() &&
                        !parcelable.getAge().isEmpty() &&
                        !parcelable.getAmount().isEmpty() &&
                        !parcelable.getIntRate().isEmpty() &&
                        parcelable.getAddress() != null &&
                        parcelable.getAadhar() != null &&
                        !parcelable.getDate().isEmpty() &&
                        parcelable.getReference() != null &&
                        parcelable.getRelative() != null &&
                        parcelable.getPhone() != null
        ) {
            //all fields and images is filled
            Log.e(TAG, "checkDetailsAndSubmit: fields are complete " + parcelable.toString());
            Utils.setIntentParcel(view.getContext(), PreviewUser.class, "data", parcelable);
        } else {
            Log.e(TAG, "checkDetailsAndSubmit: error in some field ");
            Toast.makeText(view.getContext(), "Please add all Images", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void setUserID() {
        //set a userID for the user
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Customers").addSnapshotListener((queryDocumentSnapshots, e) -> {
            assert queryDocumentSnapshots != null;
            if (!queryDocumentSnapshots.isEmpty()) {
                String customerID = String.valueOf(queryDocumentSnapshots.size() + 1);
                Log.e(TAG, "onEvent: customers count" + customerID);
                view.userID(customerID);
            } else {
                String customerID = "1";
                Log.e(TAG, "onEvent: zero customers so this is 1st " + customerID);
                view.userID(customerID);
            }
        });
    }
}
