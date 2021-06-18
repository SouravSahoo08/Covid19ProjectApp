package com.example.covid19;

import android.util.Log;

public class CountryListData {

    private String countryName;
    private String stats;

    public CountryListData(String countryName, String stats) {
        this.countryName = countryName;
        this.stats = stats;
        //Log.i("world",stats);
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }
}
