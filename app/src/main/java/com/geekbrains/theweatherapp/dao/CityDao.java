package com.geekbrains.theweatherapp.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.geekbrains.theweatherapp.model.CityEntity;

import java.util.List;

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertCity(CityEntity city);

    @Update
    void updateCity(CityEntity city);

    @Delete
    void deleteCity(CityEntity city);

    @Query("DELETE FROM CityEntity WHERE id = :id")
    void deleteCityById(long id);

    @Query("SELECT * FROM CityEntity WHERE id = :id")
    CityEntity getCityById(long id);

    @Query("SELECT * FROM CityEntity")
    List<CityEntity> getAllCities();

    @Query("SELECT COUNT() FROM CityEntity")
    long getCountCities();

}
