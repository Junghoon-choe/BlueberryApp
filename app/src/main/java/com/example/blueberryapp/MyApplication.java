package com.example.blueberryapp;

import android.app.Application;
import android.widget.Toast;

public class MyApplication extends Application {

    public static String 회원ID;
    public static String 회원PW;
    public static String 회원Name;
    public static String 회원Email;
    public static String 회원PhoneNum;
    public static String 회원Address;
    // 회원 장바구니도 포함시킬것.

    // 로그인을 담당하는 Activity에서 이 변수에 값을 저장해준다.
    // MyApplication.회원ID = ID;
    // MyApplication.회원PW = PW;

    // 아이디와 닉네임을 어디에서나 사용할 수 있게, 다른 액티비티에서 아이디와 닉네임 값을 불러
    // 오고 싶다면 아랫걸 사용.
    // String userID = MyApplication.회원ID;
    // String userPW = MyApplication.회원PW;
    // String userName = MyApplication.회원Name;
    // String userEmail = MyApplication.회원Email;
    // String userPhoneNum = MyApplication.회원PhoneNum;
    // String userAddress = MyApplication.회원Address;

    // Application Class에서 만든 메서드를 호출하는 방법
    // MyApplication MTAPP = (MyApplication) getApplication();





    public void onCreate(){
        super.onCreate();
    }


}
