package com.bcabuddies.moneymanagement.ViewUser.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.moneymanagement.Model.TransactionModel;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private static final String TAG = "TransactionAdapter";
    private ArrayList<TransactionModel> transactionList;
    private Context context;

    public TransactionAdapter(ArrayList<TransactionModel> transactionList) {
        this.transactionList = transactionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resource = R.layout.transaction_item;
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String amount, intAmount, type;
        int totalAmount;
        Date time;

        amount = Utils.AESDecryptionString(transactionList.get(position).getAmount());
        type = Utils.AESDecryptionString(transactionList.get(position).getType());
        time = transactionList.get(position).getTime();
        intAmount = Utils.AESDecryptionString(transactionList.get(position).getInterest());
        totalAmount = Integer.parseInt(amount + intAmount);

        Log.e(TAG, "onBindViewHolder: " + totalAmount);

        //setting amount and type
        holder.amountTV.setText(totalAmount + "" + context.getResources().getString(R.string.rupee_symbol));
        holder.typeTV.setText(type);

        //setting time in format dd/MM/yy
        try {
            String pattern = "dd/MM/yy \nhh:mm:a";
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat(pattern);
            Date d = new Date(time.getTime());
            holder.dateTV.setText(dateFormat.format(d));
            Log.e(TAG, "onBindViewHolder: date " + dateFormat.format(d));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onBindViewHolder: exception " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView dateTV, amountTV, typeTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTV = itemView.findViewById(R.id.transaction_item_dateTV);
            amountTV = itemView.findViewById(R.id.transaction_item_amountTV);
            typeTV = itemView.findViewById(R.id.transaction_item_typeTV);
        }
    }
}
