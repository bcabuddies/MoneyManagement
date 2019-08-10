package com.bcabuddies.moneymanagement.Accounts;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Accounts extends AppCompatActivity {

    @BindView(R.id.accounts_cashTV)
    TextView accountsCashTV;

    private String cash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        ButterKnife.bind(this);

        initCash();
    }

    @SuppressLint("SetTextI18n")
    private void initCash() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("Admins").document("Accounts").get()
                .addOnCompleteListener(task -> {
                    cash = Utils.AESDecryptionString(task.getResult().getString("cash"));
                    accountsCashTV.setText("Cash = " + cash + getResources().getString(R.string.rupee_symbol));
                });
    }
}
