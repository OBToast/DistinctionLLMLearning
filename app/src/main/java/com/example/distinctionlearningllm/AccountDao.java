package com.example.distinctionlearningllm;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface AccountDao {
    @Insert
    void newAccount(UserAccount account);

    //Query to check if an account is already in the database
    @Query("SELECT 1 FROM UserAccount WHERE username = :username")
    boolean doesUsernameExist(String username);

    //Login query, check if the username and password are correct
    @Query("SELECT 1 FROM UserAccount WHERE username = :username AND password = :password")
    boolean attemptLogin(String username, String password);

}

