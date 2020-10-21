package com.example.blueberryapp;

public class Reply {

    private String writing;
    private String userName;
    private String time;
    private String DocuName;
    private String userEmail;


    public Reply() {
    }


    public Reply(String writing, String userName, String time, String docuName, String userEmail) {
        this.writing = writing;
        this.userName = userName;
        this.time = time;
        DocuName = docuName;
        this.userEmail = userEmail;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
