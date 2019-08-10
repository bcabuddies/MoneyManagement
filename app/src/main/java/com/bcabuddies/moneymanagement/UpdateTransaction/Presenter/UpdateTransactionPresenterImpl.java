package com.bcabuddies.moneymanagement.UpdateTransaction.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.moneymanagement.UpdateTransaction.View.UpdateTransactionView;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class UpdateTransactionPresenterImpl implements UpdateTransactionPresenter {

    private static final String TAG = "UpdateTransactionPresen";
    private UpdateTransactionView view;
    private HashMap<String, Object> userMap = new HashMap<>();
    private String customerID;
    private Bundle bundle;
    private String amount, interest;


    public UpdateTransactionPresenterImpl(Bundle bundle) {
        this.bundle = bundle;
    }

    @Override
    public void attachView(UpdateTransactionView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void executeUpdate(String intr, String amt, String type) {

        interest = intr;
        amount = amt;

        FirebaseAuth auth = FirebaseAuth.getInstance();

        customerID = bundle.getString("uid");
        Log.e(TAG, "executeUpdate: customer id " + customerID);
        userMap.put("userID", customerID);
        userMap.put("type", Utils.AESEncryptionString(type));
        userMap.put("admin", Utils.AESEncryptionString(Objects.requireNonNull(auth.getCurrentUser()).getEmail()));
        userMap.put("time", FieldValue.serverTimestamp());

        boolean b = interest.equals("") || interest.isEmpty();
        if (b) {
            userMap.put("isInt", false);
            userMap.put("interest", Utils.AESEncryptionString("0"));
            interest = "0";
        } else {
            userMap.put("isInt", true);
            userMap.put("interest", Utils.AESEncryptionString(interest));
            updateAmountOrInterest(interest, type, "intAmount");
        }
        boolean b1 = amount.equals("") || amount.isEmpty();
        if (b1) {
            userMap.put("isAmt", false);
            userMap.put("amount", Utils.AESEncryptionString("0"));
            amount = "0";
        } else {
            userMap.put("isAmt", true);
            userMap.put("amount", Utils.AESEncryptionString(amount));
            updateAmountOrInterest(amount, type, "amount");
        }

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Transactions").add(userMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e(TAG, ": customer user ID = " + customerID);
                Log.e(TAG, "executeUpdate: updated " + userMap);
                int total = Integer.parseInt(amount) + Integer.parseInt(interest);
                Utils.adjustCash(total + "", type);
                view.success();
            } else {
                Log.e(TAG, "error uploading data set" + Objects.requireNonNull(task.getException()).getMessage());
                view.errorMsg("Some Error, Please try again!");
            }
        });
    }

    private void updateAmountOrInterest(String amount, String type, String account) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Customers").document(customerID)
                .get().addOnCompleteListener(task -> {
            final String cash = Utils.AESDecryptionString(Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getString("amount")));

            Log.e(TAG, "adjustCash: cash " + cash);

            int result = 0;
            if (type.contains("give"))
                result = Integer.parseInt(cash) + Integer.parseInt(amount);
            if (type.contains("take"))
                result = Integer.parseInt(cash) - Integer.parseInt(amount);
            Log.e(TAG, "adjustCash: result " + result);

            HashMap<String, Object> updateMap = new HashMap<>();
            updateMap.put("amount", Utils.AESEncryptionString(String.valueOf(result)));
            firebaseFirestore.collection("Customers").document(customerID)
                    .update(updateMap).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful())
                    Log.e(TAG, "adjustCash: updated " + account + " " + updateMap.get(account));
            });
        });
    }
}
