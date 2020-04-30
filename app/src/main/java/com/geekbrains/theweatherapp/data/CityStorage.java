package com.geekbrains.theweatherapp.data;

import java.util.LinkedList;

public class CityStorage {

    static private CityStorage mInstance;
    static private LinkedList<City> mCities;

    private CityStorage() {
        mCities = new LinkedList<>();
    }

    public static CityStorage getInstance() {
        if (mInstance == null) {
            mInstance = new CityStorage();
        }
        return mInstance;
    }

    public City addCity(String cityName) {
        City city = new City(cityName);
        mCities.addFirst(city);
        return city;
    }

    public void addCity(City city) {
        mCities.addFirst(city);
    }

    public City findCity(String cityName) {
        City foundCity;
        for (int i = 0; i < mCities.size(); i++) {
            foundCity = mCities.get(i);
            if (foundCity.getCityName().equals(cityName)) {
                return foundCity;
            }
        }
        return null;
    }

    public void clearList() {
        mCities.clear();
    }

    public City getCity(final int index) {
        return mCities.get(index);
    }

    public int getStorageSize() {
        return mCities.size();
    }
}
