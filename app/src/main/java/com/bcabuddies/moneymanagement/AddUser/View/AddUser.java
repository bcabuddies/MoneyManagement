package com.bcabuddies.moneymanagement.AddUser.View;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bcabuddies.moneymanagement.AddUser.Presenter.AddUserPresenter;
import com.bcabuddies.moneymanagement.AddUser.Presenter.AddUserPresenterImpl;
import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.PreviewUser.View.PreviewUser;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.google.android.material.textfield.TextInputLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddUser extends AppCompatActivity implements AddUserView {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.add_user_name_textLayout)
    TextInputLayout addUserNameTextLayout;
    @BindView(R.id.add_user_age_textLayout)
    TextInputLayout addUserAgeTextLayout;
    @BindView(R.id.add_user_intAmt_textLayout)
    TextInputLayout addUserIntAmtTextLayout;
    @BindView(R.id.add_user_intPer_textLayout)
    TextInputLayout addUserIntPerTextLayout;
    @BindView(R.id.date_show_tv)
    TextView dateShowTv;
    @BindView(R.id.add_user_date_card)
    CardView addUserDateCard;
    @BindView(R.id.aadhar_approved_tv)
    TextView aadharApprovedTv;
    @BindView(R.id.add_user_aadharCard)
    CardView addUserAadharCard;
    @BindView(R.id.address_approved_tv)
    TextView addressApprovedTv;
    @BindView(R.id.add_user_addressCard)
    CardView addUserAddressCard;
    @BindView(R.id.reference_approved_tv)
    TextView referenceApprovedTv;
    @BindView(R.id.add_user_referenceCard)
    CardView addUserReferenceCard;
    @BindView(R.id.relative_approved_tv)
    TextView relativeApprovedTv;
    @BindView(R.id.add_user_relativeCard)
    CardView addUserRelativeCard;
    @BindView(R.id.add_user_prevBtn)
    Button addUserPrevBtn;

    private UsersParcelable usersParcelable;
    private AddUserPresenter presenter;
    private String name, age, amout, intRate, date, aadhar, address, reference, relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);

        presenter = new AddUserPresenterImpl();
        presenter.attachView(this);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick({R.id.add_user_date_card, R.id.add_user_aadharCard, R.id.add_user_addressCard, R.id.add_user_referenceCard, R.id.add_user_relativeCard, R.id.add_user_prevBtn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_user_date_card:
                presenter.showDatePicker();
                break;
            case R.id.add_user_aadharCard:
                presenter.addPhoto("Aadhar");
                break;
            case R.id.add_user_addressCard:
                presenter.addPhoto("Address");
                break;
            case R.id.add_user_referenceCard:
                presenter.addPhoto("Reference");
                break;
            case R.id.add_user_relativeCard:
                presenter.addPhoto("Relative");
                break;
            case R.id.add_user_prevBtn:
                //send values to Presenter to calculate and all
                getData();
                break;
        }
    }

    private void getData() {
        name = addUserNameTextLayout.getEditText().getText().toString();
        age = addUserAgeTextLayout.getEditText().getText().toString();

    }

    @Override
    public void TextFieldsError(String error, int code) {
        /*
            code 0 = Name error
            code 1 = Age error
            code 2 = Amount error
            code 3 = Interest error
        */
        switch (code) {
            case 0:
                addUserNameTextLayout.setError("Please Fill Name");
                Utils.showMessage(this, error);
                break;
            case 1:
                addUserAgeTextLayout.setError("Please Fill Age");
                Utils.showMessage(this, error);
                break;
            case 2:
                addUserIntAmtTextLayout.setError("Please Fill Interest Amount");
                Utils.showMessage(this, error);
                break;
            case 3:
                addUserIntPerTextLayout.setError("Please Fill Interest %");
                Utils.showMessage(this, error);
                break;
        }
    }

    @Override
    public void pictureError(String error) {
        Utils.showMessage(this, error);
    }

    @Override
    public void dataForParcel(UsersParcelable usersParcelable) {
        this.usersParcelable = usersParcelable;
    }

    @Override
    public void everythingFine() {
        Utils.setIntentParcel(this, PreviewUser.class, "User", usersParcelable);
    }

    @Override
    public void showDate(String date) {
        dateShowTv.setText(date);
    }
}
