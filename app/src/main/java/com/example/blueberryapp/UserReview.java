package com.example.blueberryapp;

public class UserReview {

    String 회원아이디, 후기제목, 후기글 ;

    public UserReview(String 회원아이디, String 후기제목, String 후기글) {
        this.회원아이디 = 회원아이디;
        this.후기제목 = 후기제목;
        this.후기글 = 후기글;
    }

    public String get회원아이디() {
        return 회원아이디;
    }

    public void set회원아이디(String 회원아이디) {
        this.회원아이디 = 회원아이디;
    }

    public String get후기제목() {
        return 후기제목;
    }

    public void set후기제목(String 후기제목) {
        this.후기제목 = 후기제목;
    }

    public String get후기글() {
        return 후기글;
    }

    public void set후기글(String 후기글) {
        this.후기글 = 후기글;
    }
}
