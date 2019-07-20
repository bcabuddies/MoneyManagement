package com.bcabuddies.moneymanagement.Home.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.moneymanagement.Model.UserModel;
import com.bcabuddies.moneymanagement.R;

import java.util.ArrayList;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    private static Context context;
    private ArrayList<UserModel> userList;

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

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = userList.get(position).getName();
        String date = userList.get(position).getDate();
        String amount = userList.get(position).getAmount() + context.getString(R.string.rupee_symbol);
        String intAmount = userList.get(position).getRate() + "%";

        holder.nameTV.setText(name);
        holder.dateTV.setText(date);
        holder.amountTV.setText(amount);
        holder.intAmountTV.setText(intAmount);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTV, dateTV, amountTV, intAmountTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.home_item_nameTV);
            dateTV = itemView.findViewById(R.id.home_item_dateTV);
            amountTV = itemView.findViewById(R.id.home_item_amountTV);
            intAmountTV = itemView.findViewById(R.id.home_item_rateTV);
        }
    }
}
