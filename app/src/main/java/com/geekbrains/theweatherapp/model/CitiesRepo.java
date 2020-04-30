package com.geekbrains.theweatherapp.model;

import android.os.AsyncTask;

import com.geekbrains.theweatherapp.dao.CityDao;
import com.geekbrains.theweatherapp.data.City;

import java.util.List;

public class CitiesRepo {
    private final CityDao mCitiesDao;

    private List<CityEntity> mEntities;

    public CitiesRepo(CityDao dao) {
        mCitiesDao = dao;
    }

    private void loadEntities() {
        mEntities = mCitiesDao.getAllCities();
    }

    public List<CityEntity> getCities() {
        if (mEntities == null)
            loadEntities();
        return mEntities;
    }

    public long getCountOfCities() {
        return mCitiesDao.getCountCities();
    }

    public void addCity(CityEntity city) {
        long id = mCitiesDao.insertCity(city);
        loadEntities();
    }

    public void addCity(City city) {
        CityEntity newEntity = new CityEntity();
        newEntity.setCityName(city.getCityName());
        long id = mCitiesDao.insertCity(newEntity);
        loadEntities();
    }

    public void updateCity(CityEntity city) {
        mCitiesDao.updateCity(city);
        loadEntities();
    }

    public void removeCity(long id) {
        mCitiesDao.deleteCityById(id);
        loadEntities();
    }

    public CityEntity getCity(long id) {
        return mCitiesDao.getCityById(id);
    }
}
