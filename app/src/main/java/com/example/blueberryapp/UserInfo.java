package com.example.blueberryapp;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfo {

    String 회원아이디, 회원비밀번호, 회원이름, 회원이메일, 회원번호, 회원주소;

    public UserInfo(String 아이디, String 비밀번호, String 이름, String 이메일, String 번호, String 주소) {
        회원아이디 = 아이디;
        회원비밀번호 = 비밀번호;
        회원이름 = 이름;
        회원이메일 = 이메일;
        회원번호 = 번호;
        회원주소 = 주소;
    }

    public String get회원아이디() {
        return 회원아이디;
    }

    public void set회원아이디(String 회원아이디) {
        this.회원아이디 = 회원아이디;
    }

    public String get회원비밀번호() {
        return 회원비밀번호;
    }

    public void set회원비밀번호(String 회원비밀번호) {
        this.회원비밀번호 = 회원비밀번호;
    }

    public String get회원이름(String name) {
        return 회원이름;
    }

    public void set회원이름(String 회원이름) {
        this.회원이름 = 회원이름;
    }

    public String get회원이메일(String email) {
        return 회원이메일;
    }

    public void set회원이메일(String 회원이메일) {
        this.회원이메일 = 회원이메일;
    }

    public String get회원번호(String phoneNum) {
        return 회원번호;
    }

    public void set회원번호(String 회원번호) {
        this.회원번호 = 회원번호;
    }

    public String get회원주소(String address) {
        return 회원주소;
    }

    public void set회원주소(String 회원주소) {
        this.회원주소 = 회원주소;
    }

    public String toString() {
        return "Member [회원아이디=" + 회원아이디 + ", 회원비밀번호=" + 회원비밀번호 + ", 회원이름=" + 회원이름 + ", 회원이메일=" + 회원이메일 + ", 회원번호=" + 회원번호 + ",회원주소=" + 회원주소 + "]";
    }


}


