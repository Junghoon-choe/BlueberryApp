package com.example.blueberryapp;

public class UserHelper {

String Email,  Name, PhoneNum ;

    public UserHelper() {
    }

    public UserHelper(String email, String name, String phoneNum) {
        Email = email;
        Name = name;
        PhoneNum = phoneNum;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNum() {
        return PhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        PhoneNum = phoneNum;
    }
}
