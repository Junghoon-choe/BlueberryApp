package com.example.blueberryapp;

public class ReviewInfo {

    String 회원아이디, 후기제목, 후기글;



    public ReviewInfo(String 아이디, String 제목, String 글) {
        회원아이디 = 아이디;
        후기제목 = 제목;
        후기글 = 글;

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

    public String toString() {
        return "Review [회원아이디=" + 회원아이디 + ", 후기제목=" + 후기제목 + ", 후기글=" + 후기글 + "]";
    }

}
