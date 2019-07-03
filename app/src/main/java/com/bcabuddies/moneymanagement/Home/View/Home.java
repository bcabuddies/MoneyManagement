package com.bcabuddies.moneymanagement.Home.View;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.moneymanagement.AddUser.View.AddUser;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Home extends AppCompatActivity implements HomeView {

    @BindView(R.id.home_add_user_btn)
    Button homeAddUserBtn;
    @BindView(R.id.home_recyclerView)
    RecyclerView homeRecyclerView;
    @BindView(R.id.home_cardView)
    CardView homeCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.home_add_user_btn)
    public void onViewClicked() {
        Utils.setIntent(this, AddUser.class);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
