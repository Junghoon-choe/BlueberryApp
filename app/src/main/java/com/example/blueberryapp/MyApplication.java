package com.example.blueberryapp;

import android.app.Application;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;

public class MyApplication extends Application {

    public static String 회원ID;
    public static String 회원PW;
    public static String 회원Name;
    public static String 회원Email;
    public static String 회원PhoneNum;
    public static String 회원Address;


    public static String 결제금액="";
    public static String ReplyDocuName;








    public void onCreate(){
        super.onCreate();
    }


}
