package com.geekbrains.theweatherapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.geekbrains.theweatherapp.activities.App;
import com.geekbrains.theweatherapp.activities.MainActivity;
import com.geekbrains.theweatherapp.data.*;
import com.geekbrains.theweatherapp.model.CitiesRepo;
import com.geekbrains.theweatherapp.model.CityEntity;
import com.geekbrains.theweatherapp.service.ForecastResponse;
import com.geekbrains.theweatherapp.service.OpenWeather;
import com.geekbrains.theweatherapp.service.Parcel;
import com.geekbrains.theweatherapp.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.geekbrains.theweatherapp.service.Parcel.PARCEL_TAG;

public class FragmentList extends Fragment {
    private Parcel mCurrentCityData;

    private TextInputEditText mCityNameTV;
    private LinearLayout mCityListlayout;

    private CityStorage mCc;

    private OpenWeather mOpenWeather;
    private CitiesRepo mCitiesRepo;
    private MainActivity mMainActivity;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_city_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        mCc = CityStorage.getInstance();

        configControls(view);
        initRetrofit();
        initCityList(view);
    }

    private void initRetrofit() {
        String BASE_API_URL = "https://api.openweathermap.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mOpenWeather = retrofit.create(OpenWeather.class);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mCitiesRepo = ((MainActivity) getActivity()).getCitiesRepo();

        if (savedInstanceState != null) {
            mCurrentCityData = (Parcel) savedInstanceState.getSerializable(PARCEL_TAG);
        } else {
            City city = new City(getResources().getStringArray(R.array.cities)[0]);
            mCurrentCityData = new Parcel(0, city);
        }
    }

    private void getWeather(final String cityName) {
        String API_KEY = "f912bb6609c3957b0ed1ba6ffcc4c5d6";
        mOpenWeather.loadForecast(cityName, API_KEY, "metric").enqueue(new Callback<ForecastResponse>() {
            @Override
            public void onResponse(Call<ForecastResponse> call, Response<ForecastResponse> response) {
                if (response.body() != null) {
                    forecastResponseHandle(response.body());
                }
            }

            @Override
            public void onFailure(Call<ForecastResponse> call, Throwable t) {
                Log.d("Request Error", "request failure");
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.clear_list:
                CityStorage.getInstance().clearList();
                mCityListlayout.removeAllViews();
                return true;
            case R.id.sort_list:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void forecastResponseHandle(ForecastResponse response) {
        try {
            City cityFromResponse = response.getCity();
            City newCity = mCc.findCity(cityFromResponse.getCityName());
            if (newCity == null) {
                mCc.addCity(cityFromResponse);
                newCity = cityFromResponse;
                addCityToBase(newCity);
                addCityTextView(newCity, mCityListlayout, 0);
            }
            newCity.setWeathers(parseTheForecast(response));


            mCurrentCityData = new Parcel(mCc.getStorageSize() - 1, newCity);
            showTheWeather(mCurrentCityData);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    private void addCityToBase(City newCity) {
        mCitiesRepo.addCity(newCity);
    }

    private List<Weather> parseTheForecast(ForecastResponse response) {
        List<Weather> weathers;
        if ((weathers = response.getWeathers()) == null) {
            return null;
        }
        List<Weather> resWeathers = new LinkedList<>();
        for (int i = 0; i < weathers.size(); i += 8) {
            Weather w = weathers.get(i);
            w.setDrawableID(w.getAdditionalWeatherData().getId());
            resWeathers.add(w);
        }
        return resWeathers;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(PARCEL_TAG, mCurrentCityData);
        super.onSaveInstanceState(outState);
    }

    private void configControls(View view) {
        mCityNameTV = view.findViewById(R.id.city_name_TI);
        mCityListlayout = view.findViewById(R.id.city_list);

        mCityNameTV.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    String newCityName = Objects.requireNonNull(mCityNameTV.getText()).toString();
                    if (!newCityName.isEmpty()) {
                        requestSnack(v, newCityName).show();
                    }
                }
                return false;
            }
        });
    }

    private Snackbar requestSnack(View v, final String cityName) {
        return Snackbar.make(v, cityName, Snackbar.LENGTH_LONG).
                setAction(R.string.go, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getWeather(cityName);
                    }
                });
    }

    private void addCityTextView(@NonNull final City city, LinearLayout layout, final int index) {
        TextView tv = new TextView(getContext());
        tv.setText(city.getCityName());
        tv.setTextSize(25);
        layout.addView(tv, 0);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSnack(v, city.getCityName()).show();
            }
        });
    }

    private void initCityList(View view) {
        List<CityEntity> savedCities = mCitiesRepo.getCities();
        for (int i = 0; i < savedCities.size(); i++) {
            City cityData = new City(savedCities.get(i).getCityName());
            addCityTextView(cityData, mCityListlayout, i);
        }
    }

    private void showTheWeather(Parcel parcel) {
        FragmentWeather weather = FragmentWeather.create(parcel);
        getParentFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, weather)
                .addToBackStack("Weather")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }
}
