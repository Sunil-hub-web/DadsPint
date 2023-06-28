package co.in.dadspint;

public class ViewAddressModel {

    String address_id,user_id,first_name,last_name,address_type,school_id,school_name,state,pincode,email,number,address1,
            adress2,city_id,city_name,shipping_price;

    public ViewAddressModel(String address_id, String user_id, String first_name, String last_name, String address_type,
                            String school_id, String school_name, String state, String pincode, String email, String number,
                            String address1, String adress2, String city_id, String city_name, String shipping_price) {
        this.address_id = address_id;
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address_type = address_type;
        this.school_id = school_id;
        this.school_name = school_name;
        this.state = state;
        this.pincode = pincode;
        this.email = email;
        this.number = number;
        this.address1 = address1;
        this.adress2 = adress2;
        this.city_id = city_id;
        this.city_name = city_name;
        this.shipping_price = shipping_price;
    }

    public String getAddress_id() {
        return address_id;
    }

    public void setAddress_id(String address_id) {
        this.address_id = address_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getAddress_type() {
        return address_type;
    }

    public void setAddress_type(String address_type) {
        this.address_type = address_type;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAdress2() {
        return adress2;
    }

    public void setAdress2(String adress2) {
        this.adress2 = adress2;
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

    public String getShipping_price() {
        return shipping_price;
    }

    public void setShipping_price(String shipping_price) {
        this.shipping_price = shipping_price;
    }
}
