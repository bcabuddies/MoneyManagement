package com.bcabuddies.moneymanagement.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.bcabuddies.moneymanagement.EncryptionKey.EncryptionKey;
import com.bcabuddies.moneymanagement.Login.View.Login;
import com.bcabuddies.moneymanagement.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class Utils {
    //this class is for taking all the general use items into 1 place like Toast and Intents
    //you can add more as you like
    public static final String DOWNLOAD_PATH = "update/money.apk";
    public final static String VERSION = "0.1";
    public final static String FILE_NAME = "money.apk";
    private static final String ENCRYPTION_TECH = "AES";
    private static final String TAG = "Utils";

    //for encryption
    @SuppressLint("GetInstance")
    public static String AESEncryptionString(String stringData) {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ENCRYPTION_TECH);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }

        SecretKeySpec secretKeySpec = new SecretKeySpec(EncryptionKey.ENCRYPTION_KEY, ENCRYPTION_TECH);
        byte[] stringToByte = stringData.getBytes();
        byte[] encryptionByte = new byte[stringToByte.length];

        try {
            assert cipher != null;
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptionByte = cipher.doFinal(stringToByte);

        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        String returnData;
        returnData = new String(encryptionByte, StandardCharsets.ISO_8859_1);
        return returnData;
    }

    //DecryptString
    @SuppressLint("GetInstance")
    public static String AESDecryptionString(String stringData) {
        Cipher decipher = null;
        byte[] encryptedString = stringData.getBytes(StandardCharsets.ISO_8859_1);
        String returnData = stringData;
        SecretKeySpec secretKeySpec = new SecretKeySpec(EncryptionKey.ENCRYPTION_KEY, ENCRYPTION_TECH);
        try {
            decipher = Cipher.getInstance(ENCRYPTION_TECH);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
        byte[] decryption;
        try {
            assert decipher != null;
            decipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryption = decipher.doFinal(encryptedString);
            returnData = new String(decryption);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return returnData;
    }

    //simple Intent from a class to another
    public static void setIntent(Context context, Class destination) {
        Intent intent = new Intent(context, destination);
        context.startActivity(intent);
    }

    //intent with no backLogs
    public static void setIntentNoBackLog(Context context, Class destination) {
        Intent intent = new Intent(context, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    //intent with Bundle data
    public static void setIntentExtra(Context context, Class destination, String key, Bundle data) {
        Intent intent = new Intent(context, destination);
        intent.putExtra(key, data);
        context.startActivity(intent);
    }

    //intent with Parcel Data
    public static void setIntentParcel(Context context, Class destination, String key, Parcelable data) {
        Intent intent = new Intent(context, destination);
        intent.putExtra(key, data);
        context.startActivity(intent);
    }

    //this will show Toast
    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    //google signOut
    public static void googleSignOut(Context context) {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.google_requestIdToken))
                .requestEmail()
                .build();
        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(context, gso);
        mGoogleSignInClient.signOut()
                .addOnCompleteListener((Activity) context, task -> {
                    Utils.showMessage(context, "Log out");
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    auth.signOut();
                    Utils.setIntentNoBackLog(context, Login.class);
                });
    }

    public static void adjustCash(String amount, String type) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Admins").document("Accounts").get()
                .addOnCompleteListener(task -> {
                    final String cash = AESDecryptionString(task.getResult().getString("cash"));

                    Log.e(TAG, "adjustCash: cash " + cash);

                    int result = 0;
                    if (type.contains("give"))
                        result = Integer.parseInt(cash) - Integer.parseInt(amount);
                    if (type.contains("take"))
                        result = Integer.parseInt(cash) + Integer.parseInt(amount);
                    Log.e(TAG, "adjustCash: result " + result);

                    HashMap<String, Object> updateMap = new HashMap<>();
                    updateMap.put("cash", AESEncryptionString(String.valueOf(result)));
                    firestore.collection("Admins").document("Accounts")
                            .update(updateMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful())
                            Log.e(TAG, "adjustCash: cash updated " + updateMap.get("cash"));
                    });
                });
    }
}