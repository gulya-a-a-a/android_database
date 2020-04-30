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
        loadEntities();
    }

    private void loadEntities() {
        SelectAllTask sat = new SelectAllTask(mCitiesDao);
        sat.delegate = this;
        sat.execute();
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
        InsertTask it = new InsertTask(mCitiesDao);
        it.execute(city);
        loadEntities();
    }

    public void addCity(City city) {
        CityEntity newEntity = new CityEntity();
        newEntity.setCityName(city.getCityName());
        InsertTask it = new InsertTask(mCitiesDao);
        it.execute(newEntity);
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

    private void asyncFinished(List<CityEntity> cities) {
        mEntities = cities;
    }

    private static class SelectAllTask extends AsyncTask<Void, Void, List<CityEntity>> {

        private CitiesRepo delegate = null;

        CityDao mCityDao;

        SelectAllTask(CityDao dao) {
            mCityDao = dao;
        }

        @Override
        protected List<CityEntity> doInBackground(Void... voids) {
            return mCityDao.getAllCities();
        }

        @Override
        protected void onPostExecute(List<CityEntity> cityEntities) {
            delegate.asyncFinished(cityEntities);
        }
    }

    private static class InsertTask extends AsyncTask<CityEntity, Void, Void> {
        CityDao mCityDao;

        InsertTask(CityDao dao) {
            mCityDao = dao;
        }

        @Override
        protected Void doInBackground(CityEntity... cityEntities) {
            mCityDao.insertCity(cityEntities[0]);
            return null;
        }
    }
}
