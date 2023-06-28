package com.example.schooluniformapp;

public class CityModelClass {

    String city_id,city_name,state_id,country_id;

    public CityModelClass(String city_id, String city_name, String state_id, String country_id) {
        this.city_id = city_id;
        this.city_name = city_name;
        this.state_id = state_id;
        this.country_id = country_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getCountry_id() {
        return country_id;
    }

    public void setCountry_id(String country_id) {
        this.country_id = country_id;
    }
}
