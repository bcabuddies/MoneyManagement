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
        String name = Utils.AESDecryptionString(userList.get(position).getName());
        String date = Utils.AESDecryptionString(userList.get(position).getDate());
        String amount = Utils.AESDecryptionString(userList.get(position).getAmount());
        String rate = Utils.AESDecryptionString(userList.get(position).getRate());
        String uid = userList.get(position).UserModelID;
        String type = Utils.AESDecryptionString(userList.get(position).getType());

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

        String result = Utils.calculateInt(amount, rate);

        holder.nameTV.setText(name);
        holder.amountLeftTV.setText(amount + context.getString(R.string.rupee_symbol));
        holder.amountTV.setText(result + context.getString(R.string.rupee_symbol));
        holder.intAmountTV.setText(rate + "%");
        holder.userIdTV.setText(uid);

        if (type.contains("give")) {
            holder.userCard.setCardBackgroundColor(context.getColor(R.color.light_green));
        } else if (type.contains("take")) {
            holder.userCard.setCardBackgroundColor(context.getColor(R.color.light_red));
        }

        Bundle data = new Bundle();
        data.putString("uid", uid);
        data.putString("result", result);
        data.putString("nextDate", date);
        data.putString("name", name);
        data.putString("rate", rate);
        data.putString("total", amount);

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
