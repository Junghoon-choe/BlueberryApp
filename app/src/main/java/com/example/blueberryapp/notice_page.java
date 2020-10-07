package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class notice_page extends AppCompatActivity {


    private Button BT_이용후기,BT_매거진,BT_질문답변;
    private TextView TV_로그인,TV_회원가입,TV_장바구니;
    private ImageView IV_사진,IV_전화기;
    private String 실행 = "실행";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_page);

        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기= findViewById(R.id.BT_이용후기);
        BT_매거진= findViewById(R.id.BT_매거진);
        BT_질문답변= findViewById(R.id.BT_질문답변);
        TV_회원가입= findViewById(R.id.TV_회원가입);
        TV_장바구니= findViewById(R.id.TV_장바구니);
        TV_로그인= findViewById(R.id.TV_로그인);
        Log.v("notice_onCreate",실행);
        IV_전화기 = findViewById(R.id.IV_전화기);
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
                Intent intent = new Intent(notice_page.this, main_page.class);
                startActivity(intent);finish();
            }
        });



        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notice_page.this, review_page.class);
                startActivity(intent);finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notice_page.this, magazine_page.class);
                startActivity(intent);finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notice_page.this, qna_page.class);
                startActivity(intent);finish();
            }
        });
        TV_로그인.setClickable(true);
        TV_로그인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notice_page.this, Login_Page.class);
                startActivity(intent);finish();
            }
        });
        TV_회원가입.setClickable(true);
        TV_회원가입.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notice_page.this, signup_page1.class);
                startActivity(intent);finish();
            }
        });
        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(notice_page.this, basket_page.class);
                startActivity(intent);finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.v("notice_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("notice_onResume", 실행);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("notice_onPause", 실행);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("notice_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("notice_onDestroy", 실행);
    }
}
