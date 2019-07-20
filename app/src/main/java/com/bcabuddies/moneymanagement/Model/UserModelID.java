package com.bcabuddies.moneymanagement.Model;

import com.google.firebase.firestore.Exclude;

public class UserModelID {
    @Exclude
    public String UserModelID;

    public <T extends UserModelID> T withID(String id) {
        this.UserModelID = id;
        return (T) this;
    }
}
