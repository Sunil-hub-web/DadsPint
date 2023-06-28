package co.in.dadspint;

public class AddressModel {

    String address_id,user_id,first_name,last_name,address_type,city_name,sclname,email,number,
            state,pincode,address1,adress2;

    public AddressModel(String address_id, String user_id, String first_name, String last_name, String address_type,
                        String city_name, String sclname, String email, String number, String state, String pincode,
                        String address1, String adress2) {

        this.address_id = address_id;
        this.user_id = user_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.address_type = address_type;
        this.city_name = city_name;
        this.sclname = sclname;
        this.email = email;
        this.number = number;
        this.state = state;
        this.pincode = pincode;
        this.address1 = address1;
        this.adress2 = adress2;
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

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getSclname() {
        return sclname;
    }

    public void setSclname(String sclname) {
        this.sclname = sclname;
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
}
