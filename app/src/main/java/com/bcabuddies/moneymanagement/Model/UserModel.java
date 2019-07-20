package com.bcabuddies.moneymanagement.Model;

public class UserModel extends UserModelID {
    private String aadhar, address, age, date, amount, name, rate, reference, relative;

    public UserModel() {
    }

    public UserModel(String aadhar, String address, String age, String date, String amount, String name, String rate, String reference, String relative) {
        this.aadhar = aadhar;
        this.address = address;
        this.age = age;
        this.date = date;
        this.amount = amount;
        this.name = name;
        this.rate = rate;
        this.reference = reference;
        this.relative = relative;
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
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
}
