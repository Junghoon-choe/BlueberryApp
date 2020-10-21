package com.example.blueberryapp;

public class Reply {

    private String writing;
    private String userName;
    private String time;


    public Reply() {
    }

    public Reply(String writing, String userName, String time) {
        this.writing = writing;
        this.userName = userName;
        this.time = time;
    }

    public String getWriting() {
        return writing;
    }

    public void setWriting(String writing) {
        this.writing = writing;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
