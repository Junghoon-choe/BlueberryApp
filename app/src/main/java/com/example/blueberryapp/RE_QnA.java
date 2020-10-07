package com.example.blueberryapp;

public class RE_QnA {

    private String userID;
    private String QnATitle;
    private String QnAWriting;

    public RE_QnA() {
    }


    public RE_QnA(String userID, String qnATitle, String qnAWriting) {
        this.userID = userID;
        QnATitle = qnATitle;
        QnAWriting = qnAWriting;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getQnATitle() {
        return QnATitle;
    }

    public void setQnATitle(String qnATitle) {
        QnATitle = qnATitle;
    }

    public String getQnAWriting() {
        return QnAWriting;
    }

    public void setQnAWriting(String qnAWriting) {
        QnAWriting = qnAWriting;
    }
}
