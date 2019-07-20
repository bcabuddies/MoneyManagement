package com.bcabuddies.moneymanagement.Home.View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bcabuddies.moneymanagement.AddUser.View.AddUser;
import com.bcabuddies.moneymanagement.Home.Adapter.HomeAdapter;
import com.bcabuddies.moneymanagement.Home.Presenter.HomePresenter;
import com.bcabuddies.moneymanagement.Home.Presenter.HomePresenterImp;
import com.bcabuddies.moneymanagement.Model.UserModel;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class Home extends AppCompatActivity implements HomeView {

    @BindView(R.id.home_add_user_btn)
    Button homeAddUserBtn;
    @BindView(R.id.home_recyclerView)
    RecyclerView homeRecyclerView;
    @BindView(R.id.home_cardView)
    CardView homeCardView;
    private final String TAG = "Home.java";
    @BindView(R.id.home_profileView)
    CircleImageView homeProfileView;
    @BindView(R.id.home_nameView)
    TextView homeNameView;
    @BindView(R.id.home_logOut_btn)
    Button homeLogOutBtn;

    private FirebaseUser user;
    private FirebaseFirestore firestore;
    private ArrayList<UserModel> userList;
    private HomeAdapter adapter;
    private HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        init();
        showNameAndProfile();
    }

    private void showNameAndProfile() {
        firestore.collection("Users").document(user.getUid())
                .get().addOnCompleteListener(task -> {
            if (Objects.requireNonNull(task.getResult()).exists()) {
                String name = task.getResult().getString("name");
                String profile = task.getResult().getString("profile");

                homeNameView.setText(name);
                try {
                    Glide.with(this).load(profile).into(homeProfileView);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(TAG, "showNameAndProfile: error loading profile " + e.getMessage());
                }
            }
        });
    }

    private void init() {
        presenter = new HomePresenterImp();
        presenter.attachView(this);

        user = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();

        userList = new ArrayList<>();
        presenter.getUser();

        adapter = new HomeAdapter(userList);
        homeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        homeRecyclerView.setAdapter(adapter);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick({R.id.home_add_user_btn, R.id.home_logOut_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.home_add_user_btn:
                Utils.setIntent(this, AddUser.class);
                break;
            case R.id.home_logOut_btn:
                Utils.googleSignOut(this);
                break;
        }
    }

    @Override
    public void setUserList(UserModel user) {
        userList.add(user);
        adapter.notifyDataSetChanged();
    }
}
