package com.bcabuddies.moneymanagement.ViewUser.Presenter;

import android.util.Log;

import com.bcabuddies.moneymanagement.Model.TransactionModel;
import com.bcabuddies.moneymanagement.ViewUser.View.ViewUserView;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.HashMap;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ViewUserPresenterImpl implements ViewUserPresenter {

    private ViewUserView view;
    private String customerID = "";

    @Override
    public void attachView(ViewUserView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getTransaction(String userID) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Log.e("ViewUserPresenter", "getTransaction: userID " + userID);
        Query query = firestore.collection("Transactions")
                .orderBy("time", Query.Direction.ASCENDING)
                .whereEqualTo("userID", userID);

        query.addSnapshotListener((queryDocumentSnapshots, e) -> {
            assert queryDocumentSnapshots != null;
            try {
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String transactionID = doc.getDocument().getId();
                            final TransactionModel transactions =
                                    doc.getDocument().toObject(TransactionModel.class)
                                            .withID(transactionID);
                            view.getTransactions(transactions);
                            Log.e(TAG, "getTransaction: transaction int amt " + transactions.getInterest());
                            Log.e(TAG, "getTransaction: transaction time " + transactions.getTime());
                            customerID = transactions.getUserID();
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                Log.e(TAG, "getTransaction: exception in firebase query " + e1.getMessage());
            }
        });
    }

    @Override
    public void completeFeature(String amount) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("completed", Utils.AESEncryptionString("yes"));
        firestore.collection("Customers").document(customerID)
                .update(updateMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Utils.adjustCash(amount, "take");
                view.userCompleted();
            }
        });
    }

    @Override
    public void updateIntAmount(String intAmt, String uid) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        HashMap<String, Object> updateMap = new HashMap<>();
        updateMap.put("intAmount", Utils.AESEncryptionString(intAmt));
        firestore.collection("Customers").document(uid)
                .update(updateMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.e(TAG, "updateIntAmount: intAmount updated " + intAmt);
            } else {
                Log.e(TAG, "updateIntAmount: exception in intAmount update " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }
}
