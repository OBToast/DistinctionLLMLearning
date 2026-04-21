package com.example.distinctionlearningllm;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class UserAccount {
    //A user account, stores username and password
    @NonNull
    @PrimaryKey
    public String username;
    //Mmm... unenecrypted string password...

    public String password;
    public String interests;
    public String email;
    public UserAccount(String username, String password, String interests, String email)
    {
        this.username = username;
        this.password = password;
        this.interests = interests;
        this.email = email;
    }
}
