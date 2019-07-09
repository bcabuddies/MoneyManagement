package com.bcabuddies.moneymanagement.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import com.bcabuddies.moneymanagement.Login.View.Login;
import com.bcabuddies.moneymanagement.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;

public class Utils {
    //this class is for taking all the general use items into 1 place like Toast and Intents
    //you can add more as you like

    public static void setIntent(Context context, Class destination) {
        Intent intent = new Intent(context, destination);
        context.startActivity(intent);
    }

    public static void setIntentNoBackLog(Context context, Class destination) {
        Intent intent = new Intent(context, destination);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void setIntentExtra(Context context, Class destination, String key, Bundle data) {
        Intent intent = new Intent(context, destination);
        intent.putExtra(key, data);
        context.startActivity(intent);
    }

    public static void setIntentParcel(Context context, Class destination, String key, Parcelable data) {
        Intent intent = new Intent(context, destination);
        intent.putExtra(key, data);
        context.startActivity(intent);
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

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
}