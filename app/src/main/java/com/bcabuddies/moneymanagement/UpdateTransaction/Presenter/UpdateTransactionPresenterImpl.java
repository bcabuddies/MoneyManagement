package com.bcabuddies.moneymanagement.UpdateTransaction.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.moneymanagement.UpdateTransaction.View.UpdateTranactionView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class UpdateTransactionPresenterImpl implements UpdateTranactionPresenter {

    private static final String TAG = "UpdateTransactionPresen";
    private UpdateTranactionView view;
    private HashMap<String, Object> userMap = new HashMap<>();
    private String customerID;
    private Bundle bundle;


    public UpdateTransactionPresenterImpl(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void attachView(UpdateTranactionView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }


    @Override
    public void executeUpdate(String interest, String amount) {

        customerID = bundle.getString("uid");
        Log.e(TAG, "executeUpdate: customer id " + customerID);
        userMap.put("userID", customerID);

        boolean b = interest.equals("") || interest.isEmpty();
        if (b) {
            userMap.put("isInt", false);
            userMap.put("interest", "0");
        } else {
            userMap.put("isInt", true);
            userMap.put("interest", interest);
        }
        boolean b1 = amount.equals("") || amount.isEmpty();
        if (b1) {
            userMap.put("isAmt", false);
            userMap.put("amount", "0");
        } else {
            userMap.put("isAmt", true);
            userMap.put("amount", amount);
        }

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Transactions").add(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e(TAG, ": customer user ID = " + customerID);
            } else {
                Log.e(TAG, "error uploading data set" + Objects.requireNonNull(task.getException()).getMessage());
                view.errorMsg("Some Error, Please try again!");
            }
        });
    }
}
