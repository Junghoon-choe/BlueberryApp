package com.example.blueberryapp;

public class UserHelper {

String Email, PW, Name, PhoneNum ;

    public UserHelper() {
    }

    public UserHelper(String Email, String PW, String Name, String PhoneNum) {

        this.PW = PW;
        this.Name = Name;
        this.Email = Email;
        this.PhoneNum = PhoneNum;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPW() {
        return PW;
    }

    public void setPW(String PW) {
        this.PW = PW;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }
}
