package com.example.blueberryapp;

public class Reply extends RE_REVIEW_test {

    private String writing;
    private String userName;
    private String time;
    private String DocuName;


    public Reply() {
    }

    public Reply(String writing, String userName, String time, String docuName) {
        this.writing = writing;
        this.userName = userName;
        this.time = time;
        DocuName = docuName;
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

    public String getDocuName() {
        return DocuName;
    }

    public void setDocuName(String docuName) {
        DocuName = docuName;
    }
}
