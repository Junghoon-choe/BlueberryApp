package com.example.blueberryapp;

import com.google.gson.annotations.SerializedName;
import com.google.type.DateTime;

public class kakaoGet {

    @SerializedName("body")
    private String text;

    //GET
    private String tid;

    private String next_redirect_app_url;

    private String next_redirect_mobile_url;

    private String next_redirect_pc_url;

    private String android_app_scheme;

    private String ios_app_scheme;

    private DateTime created_at;

    public kakaoGet(String text,
                    String tid,
                    String next_redirect_app_url,
                    String next_redirect_mobile_url,
                    String next_redirect_pc_url,
                    String android_app_scheme,
                    String ios_app_scheme,
                    DateTime created_at) {
        this.text = text;
        this.tid = tid;
        this.next_redirect_app_url = next_redirect_app_url;
        this.next_redirect_mobile_url = next_redirect_mobile_url;
        this.next_redirect_pc_url = next_redirect_pc_url;
        this.android_app_scheme = android_app_scheme;
        this.ios_app_scheme = ios_app_scheme;
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public String getTid() {
        return tid;
    }

    public String getNext_redirect_app_url() {
        return next_redirect_app_url;
    }

    public String getNext_redirect_mobile_url() {
        return next_redirect_mobile_url;
    }

    public String getNext_redirect_pc_url() {
        return next_redirect_pc_url;
    }

    public String getAndroid_app_scheme() {
        return android_app_scheme;
    }

    public String getIos_app_scheme() {
        return ios_app_scheme;
    }

    public DateTime getCreated_at() {
        return created_at;
    }
}
