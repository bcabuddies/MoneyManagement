package com.bcabuddies.moneymanagement.UpdateTransaction.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.moneymanagement.Model.UserModel;
import com.bcabuddies.moneymanagement.UpdateTransaction.View.UpdateTransactionView;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class UpdateTransactionPresenterImpl implements UpdateTransactionPresenter {

    private static final String TAG = "UpdateTransactionPresen";
    private UpdateTransactionView view;
    private HashMap<String, Object> userMap = new HashMap<>();
    private String customerID;
    private Bundle bundle;
    private String amount, interest;
    private ArrayList<String> userNameList = new ArrayList<>();

    public UpdateTransactionPresenterImpl() {
    }

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

        try {
            customerID = bundle.getString("uid");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "executeUpdate: exception in bundle ");
        }
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
                if (amount == null || amount.equals("")) {
                    amount = "0";
                }
                Utils.adjustCash(Integer.parseInt(amount) + "", type);
                view.success();
            } else {
                Log.e(TAG, "error uploading data set" + Objects.requireNonNull(task.getException()).getMessage());
                view.errorMsg("Some Error, Please try again!");
            }
        });
    }

    @Override
    public void getUserDetails(String userID) {
        customerID = userID;
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Customers").document(userID)
                .get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                String rate = Utils.AESDecryptionString(Objects.requireNonNull(task.getResult().getString("rate")));
                String amount = Utils.AESDecryptionString(Objects.requireNonNull(task.getResult().getString("amount")));
                String intAmt = Utils.calculateInt(amount, rate);

                view.showData(amount, intAmt);
            }
        });
    }

    @Override
    public void addUserName() {
        //add user name to searchable list
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Customers")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    try {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                                if (doc.getType() == DocumentChange.Type.ADDED) {
                                    String customerId = doc.getDocument().getId();
                                    final UserModel user =
                                            doc.getDocument().toObject(UserModel.class)
                                                    .withID(customerId);
                                    String userName = Utils.AESDecryptionString(user.getName());
                                    userNameList.add(userName + " id:" + customerId);
                                }
                            }
                        }
                    } catch (Exception e1) {
                        e1.printStackTrace();
                        Log.e(TAG, "addUserName: exception adding user name and id to list ");
                    }
                });
        view.showUserName(userNameList);
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
