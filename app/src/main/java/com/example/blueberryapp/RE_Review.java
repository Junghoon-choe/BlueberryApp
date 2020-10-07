package com.example.blueberryapp;

public class RE_Review {

//    private String userName = MyApplication.회원Name;
    private String ReViewTitle;
    private String ReViewWriting;

    public RE_Review() {
    }

    public RE_Review(String reViewTitle, String reViewWriting) {
        ReViewTitle = reViewTitle;
        ReViewWriting = reViewWriting;
    }


    public String getReViewTitle() {
        return ReViewTitle;
    }

    public void setReViewTitle(String reViewTitle) {
        ReViewTitle = reViewTitle;
    }

    public String getReViewWriting() {
        return ReViewWriting;
    }

    public void setReViewWriting(String reViewWriting) {
        ReViewWriting = reViewWriting;
    }
}
