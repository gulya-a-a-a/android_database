package com.geekbrains.theweatherapp.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.geekbrains.theweatherapp.data.City;

@Entity(indices = {@Index(value = {CityEntity.CITY_NAME})}, tableName = "cities")
public class CityEntity {
    private final static String ID = "id";
    final static String CITY_NAME = "name";
    private final static String CITY_ENTITY_DATE = "date";
    private final static String CITY_ENTITY_TEMP = "temp";

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = ID)
    public long mId;

    @ColumnInfo(name = CITY_NAME)
    private String mCityName;

    @ColumnInfo(name = CITY_ENTITY_DATE)
    private long mEntityDate;

    @ColumnInfo(name = CITY_ENTITY_TEMP)
    private float mTemp;

    public String getCityName() {
        return mCityName;
    }

    public void setCityName(String cityName) {
        mCityName = cityName;
    }

    public long getEntityDate() {
        return mEntityDate;
    }

    public void setEntityDate(long entityDate) {
        mEntityDate = entityDate;
    }

    public float getTemp() {
        return mTemp;
    }

    public void setTemp(float temp) {
        mTemp = temp;
    }

    static CityEntity cityToEntity(City city) {
        CityEntity entity = new CityEntity();
        entity.setCityName(city.getCityName());
        if (city.getWeathers() != null) {
            entity.setTemp(city.getWeathers().get(0).getTemp());
            entity.setEntityDate(city.getWeathers().get(0).getTimestamp() * 1000);  //convert to milliseconds
        }
        return entity;
    }
}
