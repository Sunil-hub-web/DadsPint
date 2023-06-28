package com.example.dadspint;

public class StateModelClass {

    String state_id,state_name,Country_id;

    public StateModelClass(String state_id, String state_name, String country_id) {
        this.state_id = state_id;
        this.state_name = state_name;
        Country_id = country_id;
    }

    public String getState_id() {
        return state_id;
    }

    public void setState_id(String state_id) {
        this.state_id = state_id;
    }

    public String getState_name() {
        return state_name;
    }

    public void setState_name(String state_name) {
        this.state_name = state_name;
    }

    public String getCountry_id() {
        return Country_id;
    }

    public void setCountry_id(String country_id) {
        Country_id = country_id;
    }
}
