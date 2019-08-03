package com.bcabuddies.moneymanagement.Model;

import com.google.firebase.firestore.Exclude;

public class TransactionModelID {
    @Exclude
    public String TransactionModelID;

    public <T extends TransactionModelID> T withID(String id) {
        this.TransactionModelID = id;
        return (T) this;
    }
}
