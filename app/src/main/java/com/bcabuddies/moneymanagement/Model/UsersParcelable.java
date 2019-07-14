package com.bcabuddies.moneymanagement.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UsersParcelable implements Parcelable {
    public static final Parcelable.Creator CREATOR = new
            Parcelable.Creator() {
                public UsersParcelable createFromParcel(Parcel in) {
                    return new UsersParcelable(in);
                }

                public UsersParcelable[] newArray(int size) {
                    return new UsersParcelable[size];
                }
            };
    private long id;
    private String name;
    private String age;
    private String amount;
    private String intRate;
    private String date;
    private String aadhar;
    private String address;
    private String reference;
    private String relative;

    public UsersParcelable() {
    }

    public UsersParcelable(long id, String name, String age, String amount, String intRate, String date, String aadhar, String address, String reference, String relative) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.amount = amount;
        this.intRate = intRate;
        this.date = date;
        this.aadhar = aadhar;
        this.address = address;
        this.reference = reference;
        this.relative = relative;
    }

    public UsersParcelable(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.age = in.readString();
        this.amount = in.readString();
        this.intRate = in.readString();
        this.date = in.readString();
        this.aadhar = in.readString();
        this.address = in.readString();
        this.reference = in.readString();
        this.relative = in.readString();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getIntRate() {
        return intRate;
    }

    public void setIntRate(String intRate) {
        this.intRate = intRate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getRelative() {
        return relative;
    }

    public void setRelative(String relative) {
        this.relative = relative;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.age);
        parcel.writeString(this.amount);
        parcel.writeString(this.intRate);
        parcel.writeString(this.date);
        parcel.writeString(this.aadhar);
        parcel.writeString(this.address);
        parcel.writeString(this.reference);
        parcel.writeString(this.relative);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id= " + id +
                ", name= " + name +
                ", age= " + age +
                ", amount= " + amount +
                ", intRate= " + intRate +
                ", date= " + date +
                ", aadhar= " + aadhar +
                ", address= " + address +
                ", reference= " + reference +
                ", relative= " + relative +
                "}";
    }
}
