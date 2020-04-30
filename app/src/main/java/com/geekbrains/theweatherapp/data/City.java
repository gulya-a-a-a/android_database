package com.geekbrains.theweatherapp.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class City implements Serializable {
    @SerializedName("name")
    @Expose
    private String mCityName;

    private List<Weather> mWeathers;

    public City(String cityName) {
        mCityName = cityName;
        mWeathers = new ArrayList<>();
    }

    public String getCityName() {
        return mCityName;
    }

    public List<Weather> getWeathers() {
        return mWeathers;
    }

    public void setWeathers(List<Weather> weathers) {
        mWeathers = weathers;
    }
}

