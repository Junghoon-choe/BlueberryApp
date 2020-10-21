package com.example.blueberryapp;

import okhttp3.Interceptor;


public class qna_review_item {
    private String UserImageUrl;
    private String UserName;
    private String Title;
    private String Writing;
    private Integer CountComment;
    private Integer CountGood;


    public qna_review_item() {
    }

    public qna_review_item(String userImageUrl, String userName, String title, String writing, Integer countComment, Integer countGood) {
        UserImageUrl = userImageUrl;
        UserName = userName;
        Title = title;
        Writing = writing;
        CountComment = countComment;
        CountGood = countGood;
    }

    public String getUserImageUrl() {
        return UserImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        UserImageUrl = userImageUrl;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getWriting() {
        return Writing;
    }

    public void setWriting(String writing) {
        Writing = writing;
    }

    public Integer getCountComment() {
        return CountComment;
    }

    public void setCountComment(Integer countComment) {
        CountComment = countComment;
    }

    public Integer getCountGood() {
        return CountGood;
    }

    public void setCountGood(Integer countGood) {
        CountGood = countGood;
    }
}
