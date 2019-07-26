package com.bcabuddies.moneymanagement.AddUser.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bcabuddies.moneymanagement.AddUser.Presenter.AddUserPresenter;
import com.bcabuddies.moneymanagement.AddUser.Presenter.AddUserPresenterImpl;
import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddUser extends AppCompatActivity implements AddUserView {

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
    ConstraintLayout addUserDateCard;
    @BindView(R.id.aadhar_approved_tv)
    TextView aadharApprovedTv;
    @BindView(R.id.add_user_aadharCard)
    ConstraintLayout addUserAadharCard;
    @BindView(R.id.address_approved_tv)
    TextView addressApprovedTv;
    @BindView(R.id.add_user_addressCard)
    ConstraintLayout addUserAddressCard;
    @BindView(R.id.reference_approved_tv)
    TextView referenceApprovedTv;
    @BindView(R.id.add_user_referenceCard)
    ConstraintLayout addUserReferenceCard;
    @BindView(R.id.relative_approved_tv)
    TextView relativeApprovedTv;
    @BindView(R.id.add_user_relativeCard)
    ConstraintLayout addUserRelativeCard;
    @BindView(R.id.add_user_prevBtn)
    Button addUserPrevBtn;
    final int aadhar_code = 101, address_code = 102, reference_code = 103, relative_code = 104;
    @BindView(R.id.add_user_topTV)
    TextView addUserTopTV;
    @BindView(R.id.add_user_aadhar_imageView)
    ImageView addUserAadharImageView;
    @BindView(R.id.add_user_address_imageView)
    ImageView addUserAddressImageView;
    @BindView(R.id.add_user_reference_imageView)
    ImageView addUserReferenceImageView;
    @BindView(R.id.add_user_phone_textLayout)
    TextInputLayout addUserPhoneTextLayout;
    @BindView(R.id.add_user_takeRadio)
    RadioButton addUserTakeRadio;
    @BindView(R.id.add_user_giveRadio)
    RadioButton addUserGiveRadio;
    @BindView(R.id.add_user_docsTV)
    TextView addUserDocsTV;

    private UsersParcelable usersParcelable;
    private AddUserPresenter presenter;
    private final String YES = "Yes";
    private final String TAG = "AddUser.java";
    @BindView(R.id.add_user_relative_imageView)
    ImageView addUserRelativeImageView;

    private String user_id;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);

        presenter = new AddUserPresenterImpl();
        presenter.attachView(this);
        usersParcelable = new UsersParcelable();

        presenter.setUserID();

        addUserGiveRadio.setChecked(true);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @OnClick({
            R.id.add_user_date_card,
            R.id.add_user_aadharCard,
            R.id.add_user_addressCard,
            R.id.add_user_referenceCard,
            R.id.add_user_relativeCard,
            R.id.add_user_prevBtn,
            R.id.add_user_takeRadio,
            R.id.add_user_giveRadio
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_user_date_card:
                presenter.showDatePicker();
                break;
            case R.id.add_user_aadharCard:
                Intent aadhar_intent = CropImage.activity()
                        .getIntent(getContext());
                startActivityForResult(aadhar_intent, aadhar_code);
                break;
            case R.id.add_user_addressCard:
                Intent address_intent = CropImage.activity()
                        .getIntent(getContext());
                startActivityForResult(address_intent, address_code);
                break;
            case R.id.add_user_referenceCard:
                Intent reference_intent = CropImage.activity()
                        .getIntent(getContext());
                startActivityForResult(reference_intent, reference_code);
                break;
            case R.id.add_user_relativeCard:
                Intent relative_intent = CropImage.activity()
                        .getIntent(getContext());
                startActivityForResult(relative_intent, relative_code);
                break;
            case R.id.add_user_prevBtn:
                //send values to Presenter to calculate and all
                getData();
                break;
            case R.id.add_user_takeRadio:
                takeClicked();
                break;
            case R.id.add_user_giveRadio:
                giveClicked();
                break;
        }
    }

    private void giveClicked() {
        addUserDocsTV.setVisibility(View.VISIBLE);
        addUserAadharCard.setVisibility(View.VISIBLE);
        addUserAddressCard.setVisibility(View.VISIBLE);
        addUserReferenceCard.setVisibility(View.VISIBLE);
        addUserRelativeCard.setVisibility(View.VISIBLE);
    }

    private void takeClicked() {
        addUserDocsTV.setVisibility(View.GONE);
        addUserAadharCard.setVisibility(View.GONE);
        addUserAddressCard.setVisibility(View.GONE);
        addUserReferenceCard.setVisibility(View.GONE);
        addUserRelativeCard.setVisibility(View.GONE);
    }

    private void getData() {
        //get data from editTexts
        removeErrors();
        Log.e(TAG, "getData: clicked ");
        String name = Objects.requireNonNull(addUserNameTextLayout.getEditText()).getText().toString();
        String age = Objects.requireNonNull(addUserAgeTextLayout.getEditText()).getText().toString();
        String amout = Objects.requireNonNull(addUserIntAmtTextLayout.getEditText()).getText().toString();
        String intRate = Objects.requireNonNull(addUserIntPerTextLayout.getEditText()).getText().toString();
        String date = dateShowTv.getText().toString();
        String phone = Objects.requireNonNull(addUserPhoneTextLayout.getEditText()).getText().toString();

        usersParcelable.setName(name);
        usersParcelable.setAge(age);
        usersParcelable.setAmount(amout);
        usersParcelable.setIntRate(intRate);
        usersParcelable.setDate(date);
        usersParcelable.setUserID(user_id);
        usersParcelable.setPhone(phone);

        aadharApprovedTv.setText(YES);
        addressApprovedTv.setText(YES);
        referenceApprovedTv.setText(YES);
        relativeApprovedTv.setText(YES);

        presenter.checkDetailsAndSubmit(usersParcelable);
    }

    private void changeTextAndColor(TextView textView) {
        textView.setText(YES);
        textView.setTextColor(getColor(R.color.green));
    }

    private void removeErrors() {
        //remove errors from TextFields
        addUserNameTextLayout.setError(null);
        addUserAgeTextLayout.setError(null);
        addUserIntAmtTextLayout.setError(null);
        addUserIntPerTextLayout.setError(null);
        addUserPhoneTextLayout.setError(null);
    }

    @Override
    public void TextFieldsError(String error, int code) {
        /*
            code 0 = Name error
            code 1 = Age error
            code 2 = Amount error
            code 3 = Interest error
            code 4 = Phone error
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
            case 4:
                addUserPhoneTextLayout.setError("Please Add Phone Number");
                Utils.showMessage(this, error);
                break;
        }
    }

    @Override
    public void showDate(String date) {
        dateShowTv.setText(date);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void userID(String id) {
        user_id = id;
        addUserTopTV.setText("Add user " + user_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri mainImageUri;
        if (requestCode == aadhar_code) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = Objects.requireNonNull(result).getUri();
                Glide.with(this).load(mainImageUri).into(addUserAadharImageView);
                if (mainImageUri != null) {
                    usersParcelable.setAadhar(mainImageUri.toString());
                }
                //type = "aadhar";
                aadharApprovedTv.setText(YES);
                changeTextAndColor(aadharApprovedTv);
                assert mainImageUri != null;
                Log.e(TAG, "onActivityResult aadhar: " + mainImageUri.toString());
            }
        } else if (requestCode == address_code) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = Objects.requireNonNull(result).getUri();
                Glide.with(this).load(mainImageUri).into(addUserAddressImageView);
                if (mainImageUri != null) {
                    usersParcelable.setAddress(mainImageUri.toString());
                }
                // type = "address";
                addressApprovedTv.setText(YES);
                changeTextAndColor(addressApprovedTv);
                assert mainImageUri != null;
                Log.e(TAG, "onActivityResult address: " + mainImageUri.toString());
            }
        } else if (requestCode == reference_code) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = Objects.requireNonNull(result).getUri();
                Glide.with(this).load(mainImageUri).into(addUserReferenceImageView);
                if (mainImageUri != null) {
                    usersParcelable.setReference(mainImageUri.toString());
                }
                //type = "reference";
                referenceApprovedTv.setText(YES);
                changeTextAndColor(referenceApprovedTv);
                assert mainImageUri != null;
                Log.e(TAG, "onActivityResult reference: " + mainImageUri.toString());
            }
        } else if (requestCode == relative_code) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = Objects.requireNonNull(result).getUri();
                Glide.with(this).load(mainImageUri).into(addUserRelativeImageView);
                if (mainImageUri != null) {
                    usersParcelable.setRelative(mainImageUri.toString());
                }
                //type = "relative";
                relativeApprovedTv.setText(YES);
                changeTextAndColor(relativeApprovedTv);
                assert mainImageUri != null;
                Log.e(TAG, "onActivityResult relative: " + mainImageUri.toString());
            }
        }

    }
}
