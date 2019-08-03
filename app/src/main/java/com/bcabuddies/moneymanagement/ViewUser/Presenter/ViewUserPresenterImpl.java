package com.bcabuddies.moneymanagement.ViewUser.Presenter;

import android.util.Log;

import com.bcabuddies.moneymanagement.Model.TransactionModel;
import com.bcabuddies.moneymanagement.ViewUser.View.ViewUserView;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ViewUserPresenterImpl implements ViewUserPresenter {

    private ViewUserView view;

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
                        }
                    }
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                Log.e(TAG, "getTransaction: exception in firebase query " + e1.getMessage());
            }
        });
    }
}
