package com.bcabuddies.moneymanagement.UpdateTransaction.Presenter;

import android.util.Log;

import androidx.annotation.NonNull;

import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.PreviewUser.View.PreviewUserView;
import com.bcabuddies.moneymanagement.UpdateTransaction.View.UpdateTranactionView;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ServerTimestamp;

import java.util.HashMap;
import java.util.Objects;

public class UpdateTransactionPresenterImpl implements UpdateTranactionPresenter{

    private static final String TAG = "UpdateTransactionPresen";
    private UpdateTranactionView view;
    private HashMap<String, Object> userMap = new HashMap<>();
    private String customerID;
    private UsersParcelable parcelable;


    public UpdateTransactionPresenterImpl(UsersParcelable parcelable) {
        this.parcelable = parcelable;
    }

    @Override
    public void attachView(UpdateTranactionView view) {
        this.view=view;
    }

    @Override
    public void detachView() {
        view = null;
    }


    @Override
    public void executeUpdate(String interest, String amount) {

        customerID = parcelable.getUserID();
        userMap.put("userID", customerID);

        if (interest.equals("") || interest.isEmpty() || interest==null){
            userMap.put("isInt",false);
            userMap.put("interest","0");
        }
        else if (!(interest.equals("") || interest.isEmpty() || interest==null)){
            userMap.put("isInt",true);
            userMap.put("interest",interest);
        }
        if (amount.equals("") || amount.isEmpty() || amount==null){
            userMap.put("isAmt",false);
            userMap.put("amount","0");
        }
        else if(!(amount.equals("") || amount.isEmpty() || amount==null)){
            userMap.put("isAmt",true);
            userMap.put("amount",amount);
        }

        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Transactions").add(userMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    Log.e(TAG, ": customer user ID = " + customerID);
                } else {
                    Log.e(TAG, "error uploading data set" + Objects.requireNonNull(task.getException()).getMessage());
                    view.errorMsg("Some Error, Please try again!");
                }
            }
        });
    }
}
