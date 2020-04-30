package com.geekbrains.theweatherapp.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(indices = {@Index(value = {CityEntity.CITY_NAME})}, tableName = "cities")
public class CityEntity {
    public final static String ID = "id";
    public final static String CITY_NAME = "name";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public long mId;

    @ColumnInfo(name = CITY_NAME)
    private String mCityName;

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }
}
