package com.bcabuddies.moneymanagement.Home.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.moneymanagement.Model.UserModel;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.ViewUser.View.ViewUser;
import com.bcabuddies.moneymanagement.utils.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private ArrayList<UserModel> userList;
    private Context context;

    public HomeAdapter(ArrayList<UserModel> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override

    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_row_item, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = userList.get(position).getName();
        String date = userList.get(position).getDate();
        String amount = userList.get(position).getAmount();
        String intAmount = userList.get(position).getRate();
        String uid = userList.get(position).UserModelID;

        /*
            String dt = "2008-01-01";  // Start date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar c = Calendar.getInstance();
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, 1);  // number of days to add
            dt = sdf.format(c.getTime());  // dt is now the new date
        */

        //to calculate next Date
        String TAG = "HomeAdapter";
        try {
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
            Calendar c = Calendar.getInstance();
            c.setTime(Objects.requireNonNull(sdf.parse(date)));
            c.add(Calendar.MONTH, 1);
            date = sdf.format(c.getTime());
            Log.e(TAG, "onBindViewHolder: new date = " + date);
            holder.dateTV.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e(TAG, "onBindViewHolder: exception in date " + e.getMessage());
        }

        //to calculate next amount
        // P * R * T
        // P = remaining amount
        // R = rate
        // T = time in months
        double p = Double.parseDouble(amount);
        double r = Double.parseDouble(intAmount);
        double t = 0.083; //for show taking 1/12 calculating only for 1 month
        int result = (int) (p * r * t);
        Log.e(TAG, "onBindViewHolder: p " + p + " r " + r + " t " + t + " res " + result);

        holder.nameTV.setText(name);
        holder.amountLeftTV.setText(amount + context.getString(R.string.rupee_symbol));
        holder.amountTV.setText(result + context.getString(R.string.rupee_symbol));
        holder.intAmountTV.setText(intAmount + "%");
        holder.userIdTV.setText(uid);

        Bundle data = new Bundle();
        data.putString("uid", uid);
        data.putString("result", String.valueOf(result));
        data.putString("nextDate", date);

        holder.userCard.setOnClickListener(view -> Utils.setIntentExtra(context, ViewUser.class, "data", data));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, dateTV, amountTV, intAmountTV, amountLeftTV, userIdTV;
        private CardView userCard;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.home_item_nameTV);
            dateTV = itemView.findViewById(R.id.home_item_dateTV);
            amountTV = itemView.findViewById(R.id.home_item_amountTV);
            intAmountTV = itemView.findViewById(R.id.home_item_rateTV);
            amountLeftTV = itemView.findViewById(R.id.home_item_amountLeftTV);
            userIdTV = itemView.findViewById(R.id.home_item_userTV);
            userCard = itemView.findViewById(R.id.home_item_cardView);
        }
    }
}
