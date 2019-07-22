package com.bcabuddies.moneymanagement.PreviewUser.Presenter;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.PreviewUser.View.PreviewUserView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

import id.zelory.compressor.Compressor;

public class PreviewUserPresenterImpl implements PreviewUserPresenter {

    private final static String TAG = "PreviewPresenter";
    private PreviewUserView view;
    private UsersParcelable parcelable;
    private Bitmap thumb_Bitmap = null;
    private String downloadURL = null;
    private HashMap<String, Object> userMap = new HashMap<>();
    private String customerID;

    public PreviewUserPresenterImpl(UsersParcelable parcelable) {
        this.parcelable = parcelable;
    }

    @Override
    public void attachView(PreviewUserView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void addText(TextView previewTextTV, TextView previewDataTV) {
        previewTextTV.setText(
                "" +
                        "Name: " + "\n" +
                        "Age: " + "\n" +
                        "Amount: " + "\n" +
                        "Rate: " + "\n" +
                        "Phone: " + "\n" +
                        "Date: "
        );
        previewDataTV.setText(
                "" +
                        parcelable.getName() + "\n" +
                        parcelable.getAge() + "\n" +
                        parcelable.getAmount() + "\n" +
                        parcelable.getIntRate() + "%\n" +
                        parcelable.getPhone() + "\n" +
                        parcelable.getDate()
        );
    }

    private byte[] compressImage(Uri mainImageUri) {
        if (mainImageUri != null) {
            File thumb_filePathUri = new File(Objects.requireNonNull(mainImageUri.getPath()));
            try {
                thumb_Bitmap = new Compressor(view.getContext()).setQuality(50).compressToBitmap(thumb_filePathUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumb_Bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] thumb_byte = byteArrayOutputStream.toByteArray();            //upload this on firebase storage
            Log.e(TAG, "onActivityResult: thumb_byte" + Arrays.toString(thumb_byte));
            return thumb_byte;
        }
        return new byte[0];
    }


    @Override
    public void submitData(ArrayList<String> imageList) {
        /*
            imageList.get(0) = Aadhar
            imageList.get(1) = Address
            imageList.get(2) = Reference
            imageList.get(3) = Relative
        */
        byte[] aadhar = compressImage(Uri.parse(imageList.get(0)));
        byte[] address = compressImage(Uri.parse(imageList.get(1)));
        byte[] reference = compressImage(Uri.parse(imageList.get(2)));
        byte[] relative = compressImage(Uri.parse(imageList.get(3)));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        uploadImage(storageReference, aadhar, "aadhar");
        uploadImage(storageReference, address, "address");
        uploadImage(storageReference, reference, "reference");
        uploadImage(storageReference, relative, "relative");

        customerID = parcelable.getUserID();

        userMap.put("name", parcelable.getName());
        userMap.put("age", parcelable.getAge());
        userMap.put("amount", parcelable.getAmount());
        userMap.put("rate", parcelable.getIntRate());
        userMap.put("date", parcelable.getDate());
        userMap.put("phone", parcelable.getPhone());
        userMap.put("userID", customerID);

        Log.e(TAG, "submitData: userMap " + userMap);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        firestore.collection("Customers").document(String.valueOf(customerID))
                .set(userMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "submitData: customer user ID = " + customerID);
                        Log.e(TAG, "submitData: data uploaded on firestore ");
                        view.everythingDone();
                    } else {
                        Log.e(TAG, "submitData: error uploading data set" + Objects.requireNonNull(task.getException()).getMessage());
                        view.errorMsg("Some Error, Please try again!");
                    }
                });
    }

    private void uploadImage(StorageReference storageReference, byte[] image, String name) {
        customerID = parcelable.getUserID();
        StorageReference uploadFile = storageReference.child("Customers/" + customerID + "/" + name + ".jpg");
        uploadFile.putBytes(image)
                .addOnSuccessListener(taskSnapshot -> uploadFile.getDownloadUrl().addOnSuccessListener(uri -> {
                    downloadURL = uri.toString();
                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put(name, downloadURL);
                    Log.e(TAG, "uploadImage: CustomerID" + customerID);
                    updateFirebase(updateMap);
                }))
                .addOnFailureListener(runnable -> Log.e(TAG, "uploadImage: exception " + runnable.getMessage()))
                .addOnProgressListener(taskSnapshot -> view.showProgress((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount()));
    }

    private void updateFirebase(HashMap<String, Object> updateMap) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Customers").document(String.valueOf(customerID))
                .update(updateMap)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.e(TAG, "updateFirebase: done " + updateMap);
                        incrementUserID(String.valueOf(customerID), firestore);
                    } else {
                        Log.e(TAG, "submitData: error uploading data " + Objects.requireNonNull(task.getException()).getMessage());
                        view.errorMsg("Some Error, Please try again!");
                    }
                });

    }

    private void incrementUserID(String userID, FirebaseFirestore firestore) {
        //increment userID here
        HashMap<String, Object> map = new HashMap<>();
        map.put("userID", userID);
        firestore.collection("Admins").document("UserID")
                .set(map).addOnCompleteListener(task -> {
            if (task.isSuccessful())
                Log.e(TAG, "incrementUserID: userID updated ");
            else
                Log.e(TAG, "incrementUserID: exception " + Objects.requireNonNull(task.getException()).getMessage());
        });
    }
}
