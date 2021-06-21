package com.example.covid19;

public class CountryListData {

    private String countryName;
    private String population;
    private String no_of_cases;
    private String no_of_active_cases;
    private String recovered_cases;
    private String critical_cases;
    private String deaths;
    private String affected_countries;

    //constructor to handle world data
    public CountryListData(String countryName, String population, String no_of_cases, String no_of_active_cases, String critical_cases, String recovered_cases, String deaths, String affected_countries) {
        this.countryName = countryName;
        this.population = population;
        this.no_of_cases = no_of_cases;
        this.no_of_active_cases = no_of_active_cases;
        this.recovered_cases = recovered_cases;
        this.critical_cases = critical_cases;
        this.deaths = deaths;
        this.affected_countries = affected_countries;
    }

    //constructor to handle Countries data
    public CountryListData(String countryName, String population, String no_of_cases, String no_of_active_cases, String critical_cases, String recovered_cases, String deaths) {
        this.countryName = countryName;
        this.population = population;
        this.no_of_cases = no_of_cases;
        this.no_of_active_cases = no_of_active_cases;
        this.recovered_cases = recovered_cases;
        this.critical_cases = critical_cases;
        this.deaths = deaths;

    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getNo_of_cases() {
        return no_of_cases;
    }

    public void setNo_of_cases(String no_of_cases) {
        this.no_of_cases = no_of_cases;
    }

    public String getNo_of_active_cases() {
        return no_of_active_cases;
    }

    public void setNo_of_active_cases(String no_of_active_cases) {
        this.no_of_active_cases = no_of_active_cases;
    }

    public String getRecovered_cases() {
        return recovered_cases;
    }

    public void setRecovered_cases(String recovered_cases) {
        this.recovered_cases = recovered_cases;
    }

    public String getCritical_cases() {
        return critical_cases;
    }

    public void setCritical_cases(String critical_cases) {
        this.critical_cases = critical_cases;
    }

    public String getDeaths() {
        return deaths;
    }

    public void setDeaths(String deaths) {
        this.deaths = deaths;
    }

    public String getAffected_countries() {
        if (affected_countries != null)
            return null;
        return affected_countries;
    }

    public void setAffected_countries(String affected_countries) {
        this.affected_countries = affected_countries;
    }
}
