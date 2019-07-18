package com.bcabuddies.moneymanagement.AddUser.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.bcabuddies.moneymanagement.AddUser.Presenter.AddUserPresenter;
import com.bcabuddies.moneymanagement.AddUser.Presenter.AddUserPresenterImpl;
import com.bcabuddies.moneymanagement.Model.UsersParcelable;
import com.bcabuddies.moneymanagement.PreviewUser.View.PreviewUser;
import com.bcabuddies.moneymanagement.R;
import com.bcabuddies.moneymanagement.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import id.zelory.compressor.Compressor;

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
    final int aadhar_code = 101, address_code = 102, reference_code = 103, relative_code = 104;
    @BindView(R.id.add_user_topTV)
    TextView addUserTopTV;
    @BindView(R.id.add_user_aadhar_imageView)
    ImageView addUserAadharImageView;
    @BindView(R.id.add_user_address_imageView)
    ImageView addUserAddressImageView;
    @BindView(R.id.add_user_reference_imageView)
    ImageView addUserReferenceImageView;

    private UsersParcelable usersParcelable;
    private AddUserPresenter presenter;
    private final String YES = "Yes";
    private final String TAG = "AddUser.java";
    @BindView(R.id.add_user_relative_imageView)
    ImageView addUserRelativeImageView;
    private Bitmap thumb_Bitmap = null;
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
        }
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

        usersParcelable.setName(name);
        usersParcelable.setAge(age);
        usersParcelable.setAmount(amout);
        usersParcelable.setIntRate(intRate);
        usersParcelable.setDate(date);
        usersParcelable.setUserID(user_id);

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

    @SuppressLint("SetTextI18n")
    @Override
    public void userID(String id) {
        user_id = id;
        addUserTopTV.setText("Add user " + user_id);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Uri mainImageUri = null;
        String type = null;
        if (requestCode == aadhar_code) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = Objects.requireNonNull(result).getUri();
                type = "aadhar";
                Glide.with(this).load(mainImageUri).into(addUserAadharImageView);
                aadharApprovedTv.setText(YES);
                changeTextAndColor(aadharApprovedTv);
                Log.e(TAG, "onActivityResult aadhar: " + mainImageUri.toString());
            }
        } else if (requestCode == address_code) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = Objects.requireNonNull(result).getUri();
                type = "address";
                Glide.with(this).load(mainImageUri).into(addUserAddressImageView);
                addressApprovedTv.setText(YES);
                changeTextAndColor(addressApprovedTv);
                Log.e(TAG, "onActivityResult address: " + mainImageUri.toString());
            }
        } else if (requestCode == reference_code) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = Objects.requireNonNull(result).getUri();
                type = "reference";
                Glide.with(this).load(mainImageUri).into(addUserReferenceImageView);
                referenceApprovedTv.setText(YES);
                changeTextAndColor(referenceApprovedTv);
                Log.e(TAG, "onActivityResult reference: " + mainImageUri.toString());
            }
        } else if (requestCode == relative_code) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = Objects.requireNonNull(result).getUri();
                type = "relative";
                Glide.with(this).load(mainImageUri).into(addUserRelativeImageView);
                relativeApprovedTv.setText(YES);
                changeTextAndColor(relativeApprovedTv);
                Log.e(TAG, "onActivityResult relative: " + mainImageUri.toString());
            }
        }
        if (mainImageUri != null) {
            File thumb_filePathUri = new File(Objects.requireNonNull(mainImageUri.getPath()));
            try {
                thumb_Bitmap = new Compressor(this).setQuality(50).compressToBitmap(thumb_filePathUri);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            thumb_Bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] thumb_byte = byteArrayOutputStream.toByteArray();            //upload this on firebase storage
            Log.e(TAG, "onActivityResult: thumb_byte" + Arrays.toString(thumb_byte));
            switch (type) {
                case "aadhar":
                    usersParcelable.setAadhar(Arrays.toString(thumb_byte));
                    break;
                case "address":
                    usersParcelable.setAddress(Arrays.toString(thumb_byte));
                    break;
                case "reference":
                    usersParcelable.setReference(Arrays.toString(thumb_byte));
                    break;
                case "relative":
                    usersParcelable.setRelative(Arrays.toString(thumb_byte));
                    break;
            }
        } else {
            Utils.showMessage(this, "Please select an image");
        }
    }
}
