package com.bcabuddies.moneymanagement.Login.Presenter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bcabuddies.moneymanagement.Login.View.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.File;
import java.util.Objects;

import static com.bcabuddies.moneymanagement.utils.Utils.DOWNLOAD_PATH;
import static com.bcabuddies.moneymanagement.utils.Utils.FILE_NAME;
import static com.bcabuddies.moneymanagement.utils.Utils.VERSION;

public class LoginPresenterImp implements loginPresenter {
    private final String TAG = "LoginPresenter";
    private FirebaseAuth auth;
    private LoginView view;

    public LoginPresenterImp(FirebaseAuth auth) {
        this.auth = auth;
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.v("mGoogleSignIn", "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                try {
                    view.loginError(Objects.requireNonNull(task.getException()).getMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                view.googleLoginSuccess();
            }
        });
    }

    @Override
    public void update(FirebaseFirestore firestore, FirebaseStorage firebaseStorage) {
        Log.e(TAG, "checkUpdate: checking for update");
        try {
            firestore.collection("Admins").document("update").get()
                    .addOnCompleteListener(task -> {
                        if (Objects.requireNonNull(task.getResult()).exists()) {
                            String update = task.getResult().getString("update");
                            try {
                                assert update != null;
                                if (!update.equals(VERSION)) {
                                    //app needs update
                                    Log.e(TAG, "onComplete: update = " + update);
                                    //update available
                                    Log.e(TAG, "onComplete: update available ");
                                    if (isStoragePermissionGranted()) {
                                        Context context = view.getContext();
                                        File file = new File(context.getFilesDir(), FILE_NAME);
                                        Toast.makeText(context, "Please do not close the App, It is updating itself, Thank you", Toast.LENGTH_LONG).show();
                                        try {
                                            firebaseStorage.getReference().child(DOWNLOAD_PATH).getFile(file)
                                                    .addOnCompleteListener(task1 -> {
                                                        //file downloaded
                                                        Toast.makeText(context, "Update File download success", Toast.LENGTH_SHORT).show();
                                                        Log.e(TAG, "onComplete: file downloaded " + file.toString());
                                                        try {
                                                            Uri fileUri = Uri.fromFile(file);
                                                            if (Build.VERSION.SDK_INT >= 24) {
                                                                fileUri = FileProvider.getUriForFile(context, context.getPackageName(),
                                                                        file);
                                                            }
                                                            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
                                                            intent.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true);
                                                            intent.setDataAndType(fileUri, "application/vnd.android" + ".package-archive");
                                                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                                            context.startActivity(intent);
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                            Log.e(TAG, "checkUpdate: opening APK exception " + e.getMessage());
                                                        }
                                                    })
                                                    .addOnProgressListener(taskSnapshot -> {
                                                        //downloading
                                                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                                                        Log.e(TAG, "onProgress: progress " + ((int) progress));
                                                    })
                                                    .addOnFailureListener(e -> {
                                                        //failed
                                                        Log.e(TAG, "onFailure: Failed " + e.getMessage());
                                                    });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            Log.e(TAG, "onComplete: exception downloading file " + e.getMessage());
                                        }
                                    } else {
                                        isStoragePermissionGranted();
                                        Log.e(TAG, "onComplete: storage permission not granted ");
                                    }
                                } else {
                                    Log.e(TAG, "onComplete: app is fully updates ");
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(TAG, "checkUpdate: version exception " + e.getMessage());
                            }
                        } else {
                            Log.e(TAG, "onComplete: Error: " + Objects.requireNonNull(task.getException()).getMessage());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "checkUpdate: exception " + e.getMessage());
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (view.getContext().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(TAG, "Permission is granted");
                return true;
            } else {
                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions((Activity) view.getContext(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
            return true;
        }
    }

    @Override
    public void attachView(LoginView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        view = null;
    }
}
