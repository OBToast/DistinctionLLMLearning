package com.example.distinctionlearningllm;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//The database that stores user accounts
@Database(entities = {UserAccount.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract AccountDao accountDao();
}

