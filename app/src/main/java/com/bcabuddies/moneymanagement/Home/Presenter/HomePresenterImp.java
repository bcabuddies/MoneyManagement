package com.bcabuddies.moneymanagement.Home.Presenter;

import android.app.Activity;

import com.bcabuddies.moneymanagement.Home.View.HomeView;
import com.bcabuddies.moneymanagement.Model.UserModel;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomePresenterImp implements HomePresenter {

    private HomeView view;

    @Override
    public void attachView(HomeView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void getUser() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("Customers")
                .orderBy("userID", Query.Direction.ASCENDING);
        query.addSnapshotListener((Activity) view.getContext(), (queryDocumentSnapshots, e) -> {
            assert queryDocumentSnapshots != null;
            if (!queryDocumentSnapshots.isEmpty()) {
                for (final DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String user_id = doc.getDocument().getId();
                        final UserModel user = doc.getDocument().toObject(UserModel.class).withID(user_id);
                        view.setUserList(user);
                    }
                }
            }
        });
    }
}
