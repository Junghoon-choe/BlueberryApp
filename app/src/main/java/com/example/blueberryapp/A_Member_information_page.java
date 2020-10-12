package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.io.InputStream;

public class A_Member_information_page extends AppCompatActivity {


    private TextView UserName,UserEmail,UserPhoneNum;
//    private TextView UserAddress;
    private Button BT_이용후기,BT_매거진,BT_질문답변,BT_수정버튼;
    private TextView TV_로그아웃,TV_마이페이지,TV_장바구니;
    private ImageView IV_사진,IV_전화기;
    private String 실행 = "실행";

    private String userID = MyApplication.회원ID;
    private String userPW = MyApplication.회원PW;
    private String userName = MyApplication.회원Name;
    private String userEmail = MyApplication.회원Email;
    private String userPhoneNum = MyApplication.회원PhoneNum;
    private String userAddress = MyApplication.회원Address;
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_member_information_page);








        BT_수정버튼= findViewById(R.id.BT_수정버튼);
        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기= findViewById(R.id.BT_이용후기);
        BT_매거진= findViewById(R.id.BT_매거진);
        BT_질문답변= findViewById(R.id.BT_질문답변);
        TV_마이페이지= findViewById(R.id.TV_마이페이지);
        TV_장바구니= findViewById(R.id.TV_장바구니);
        TV_로그아웃= findViewById(R.id.TV_로그아웃);
        Log.v("A_Member_information_page_onCreate",실행);
        IV_전화기 = findViewById(R.id.IV_전화기);

        imageSwitcher = findViewById(R.id.A회원정보광고창);


        UserName = findViewById(R.id.UserName);
        UserEmail = findViewById(R.id.UserEmail);
        UserPhoneNum = findViewById(R.id.UserPhoneNum);
//        UserAddress = findViewById(R.id.UserAddress);


        UserName.setText(userName);
        UserEmail.setText(userEmail);
        UserPhoneNum.setText(userPhoneNum);


        //Thread
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(getApplicationContext());
                view.setBackgroundColor(Color.WHITE);
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                view.setLayoutParams(
                        new ImageSwitcher.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                        )
                );
                return view;
            }
        });
        imageSwitcher.setInAnimation(this, android.R.anim.fade_out);
        imageSwitcher.setInAnimation(this, android.R.anim.fade_in);
        class ImageThread extends java.lang.Thread {
            int duration = 2000;
            final int images[] = {
                    R.drawable.a1,
                    R.drawable.a2,
            };

            int cur = 0;

            public void run() {
                aBoolean = true;
                while (aBoolean) {
                    synchronized (this) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                imageSwitcher.setImageResource(images[cur]);
                            }
                        });
                    }
                    cur++;

                    if (cur == images.length) {
                        cur = 0;
                    }
                    try {
                        Thread.sleep(duration);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }


        imageSwitcher.setVisibility(View.VISIBLE);
        thread = new ImageThread();
        thread.start();

        IV_전화기.setClickable(true);
        IV_전화기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-8885-3426"));
                startActivity(intent);
            }
        });
        IV_사진.setClickable(true);
        IV_사진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_Member_information_page.this, A_main_page.class);
                startActivity(intent);
                finish();
            }
        });



        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_Member_information_page.this, A_review_page.class);
                startActivity(intent);finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_Member_information_page.this, A_magazine_page.class);
                startActivity(intent);finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_Member_information_page.this, A_qna_page.class);
                startActivity(intent);finish();
            }
        });
        TV_로그아웃.setClickable(true);
        TV_로그아웃.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_Member_information_page.this, main_page.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);finish();
            }
        });
        TV_마이페이지.setClickable(true);
        TV_마이페이지.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_Member_information_page.this, A_my_page.class);
                startActivity(intent);finish();
            }
        });
        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_Member_information_page.this, A_basket_page.class);
                startActivity(intent);finish();
            }
        });

        BT_수정버튼.setClickable(true);
        BT_수정버튼.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_Member_information_page.this, A_EditPassword.class);
                startActivity(intent);

            }
        });

    }










    /*private String getJsonString(){  //Json파일을 읽어와 파일 내용을 String 변수에 담아 return하는 getJsonString()함수 이다.
        String json = "";

        try {
            InputStream is = getAssets().open("User.json");
            int fileSize = is.available();

            byte[] buffer = new byte[fileSize];
            is.read(buffer);
            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException e){
            e.printStackTrace();
        }
        return json;
    }*/


    @Override
    protected void onStart() {
        super.onStart();
        Log.v("A_Member_information_page_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("A_Member_information_page_onResume", 실행);



//        UserAddress.setText(userAddress);










        // String userID = MyApplication.회원ID;
        // String userPW = MyApplication.회원PW;
        // String userName = MyApplication.회원Name;
        // String userEmail = MyApplication.회원Email;
        // String userPhoneNum = MyApplication.회원PhoneNum;
        // String userAddress = MyApplication.회원Address;




        /*SharedPreferences User = getSharedPreferences("UserFile",MODE_PRIVATE);

        String 회원이름 = User.getString("회원이름","");
        String 회원이메일 = User.getString("회원이메일","");
        String 회원번호 = User.getString("회원번호","");
        String 회원주소 = User.getString("회원주소","");

        UserName.setText(회원이름);
        UserEmail.setText(회원이메일);
        UserPhoneNum.setText(회원번호);
        UserAddress.setText(회원주소);*/






        /*
        이름저장소 = findViewById(R.id.이름저장소);
        SharedPreferences testsf = getSharedPreferences("sFile",MODE_PRIVATE);
        String text = testsf.getString("text","");
        이름저장소.setText(text);
        Intent intent = new Intent();
        intent.getStringExtra("sFile");*/


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("A_Member_information_page_onPause", 실행);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("A_Member_information_page_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("A_Member_information_page_onDestroy", 실행);
    }




}
