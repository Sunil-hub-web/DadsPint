package com.example.dadspint;

public class PinCodeModel {

    String pin_id,pincode;

    public PinCodeModel(String pin_id, String pincode) {
        this.pin_id = pin_id;
        this.pincode = pincode;
    }

    public String getPin_id() {
        return pin_id;
    }

    public void setPin_id(String pin_id) {
        this.pin_id = pin_id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
