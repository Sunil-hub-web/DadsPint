package com.example.dadspint;

public class SchoolModelClass {

    String school_name,school_id;

    public SchoolModelClass(String school_name, String school_id) {
        this.school_name = school_name;
        this.school_id = school_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }
}
