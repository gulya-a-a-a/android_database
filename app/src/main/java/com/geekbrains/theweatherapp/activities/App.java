package com.geekbrains.theweatherapp.activities;

import android.app.Application;

import androidx.room.Room;

import com.geekbrains.theweatherapp.dao.CityDao;
import com.geekbrains.theweatherapp.database.CityDatabase;

public class App extends Application {

    private static App mInstance;
    private CityDatabase mDb;

    public static App getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;

        mDb = Room.databaseBuilder(
                getApplicationContext(),
                CityDatabase.class,
                "city_database")
                .allowMainThreadQueries()
                .build();
    }

    public CityDatabase getDb() {
        return mDb;
    }

    public CityDao getCityDao() {
        return mDb.getCityDao();
    }

}
