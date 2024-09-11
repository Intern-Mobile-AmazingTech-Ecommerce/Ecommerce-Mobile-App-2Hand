package com.example.ecommercemobileapp2hand.Models.Singleton;

import com.example.ecommercemobileapp2hand.Models.UserAccount;

public class UserAccountManager {
    public static UserAccountManager instance;
    private UserAccount userAccount;

    private UserAccountManager(){}

    public static synchronized UserAccountManager getInstance(){
        if(instance == null)
        {
            instance = new UserAccountManager();
        }
        return instance;
    }

    public UserAccount getCurrentUserAccount(){
        return userAccount;
    }

    public void setCurrentUserAccount(UserAccount curr){
        this.userAccount = curr;
    }


}
