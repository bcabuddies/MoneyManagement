package com.bcabuddies.moneymanagement.PostRegistration.Presenter;

import android.os.Bundle;
import android.util.Log;

import com.bcabuddies.moneymanagement.PostRegistration.View.PostRegView;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

public class PostRegPresenterImpl implements PostRegPresenter {

    private final String TAG = "PostRegImpl.java";
    private PostRegView view;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser user;

    public PostRegPresenterImpl(FirebaseFirestore firebaseFirestore, FirebaseUser user) {
        this.firebaseFirestore = firebaseFirestore;
        this.user = user;
    }

    @Override
    public void attachView(PostRegView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @Override
    public void uploadData(Bundle bundle) {
        //upload data to Firestore
        String name = bundle.getString("name");
        String email = bundle.getString("email");
        String profile = bundle.getString("profile");

        if (!name.isEmpty() || name.equals(null)) {
            HashMap<String, Object> maps = new HashMap<>();
            maps.put("name", Utils.AESEncryptionString(name));
            maps.put("email", Utils.AESEncryptionString(email));
            maps.put("profile", Utils.AESEncryptionString(profile));

            assert user != null;
            Log.e(TAG, "uploadData: inside PostRegImpl uploadData current user = " + user.getUid());

            firebaseFirestore.collection("Users").document(user.getUid())
                    .set(maps).addOnCompleteListener(task -> {
                if (task.isComplete()) {
                    view.uploadSuccess();
                } else {
                    String error;
                    error = Objects.requireNonNull(task.getException()).getMessage();
                    view.errorMsg(error);
                }
            });
        } else {
            view.errorMsg("Please Fill name Field");
        }
    }

    @Override
    public void checkAdmin(String email) {
        //check if email is admin or not
        firebaseFirestore.collection("Admins").document("AdminEmails")
                .get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                String adminEmails = task.getResult().getString("email");
                assert adminEmails != null;
                if (adminEmails.contains(email)) {
                    view.userIsAdmin();
                } else {
                    view.userIsNotAdmin();
                }
            }
        });
    }
}
