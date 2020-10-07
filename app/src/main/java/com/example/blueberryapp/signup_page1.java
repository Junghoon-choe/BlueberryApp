package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

public class signup_page1 extends AppCompatActivity {


    private Button  BT_이용후기,  BT_매거진, BT_질문답변,BT_다음;
    private TextView TV_로그인, TV_회원가입, TV_장바구니;
    private ImageView IV_사진,IV_전화기;
    private String 실행="실행";
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;

    // 다음 진행 버튼
    // 체크박스 체크여부
    public int TERMS_AGREE_1 = 0; // 체크 안됬을시 0, 체크 됬을 경우 1
    public int TERMS_AGREE_2 = 0;
    public int TERMS_AGREE_3 = 0;

    AppCompatCheckBox check1; // 첫번째 동의
    AppCompatCheckBox check2; // 두번째
    AppCompatCheckBox check3; // 세번째





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page1);

        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기 = findViewById(R.id.BT_이용후기);
        BT_매거진 = findViewById(R.id.BT_매거진);
        BT_질문답변 = findViewById(R.id.BT_질문답변);
        BT_다음 = findViewById(R.id.BT_다음);
        TV_회원가입 = findViewById(R.id.TV_회원가입);
        TV_장바구니 = findViewById(R.id.TV_장바구니);
        TV_로그인 = findViewById(R.id.TV_로그인);
        check1 = findViewById(R.id.check1);
        check2 = findViewById(R.id.check2);
        check3 = findViewById(R.id.check3);
        imageSwitcher = findViewById(R.id.회원가입1광고창);

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

        Log.v("Signup_onCreate",실행);
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
                Intent intent = new Intent(signup_page1.this, main_page.class);
                startActivity(intent);
                finish();
            }
        });


        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page1.this, review_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page1.this, magazine_page.class);
                startActivity(intent);
                finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page1.this, qna_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_로그인.setClickable(true);
        TV_로그인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page1.this, Login_Page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_회원가입.setClickable(true);
        TV_회원가입.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page1.this, signup_page1.class);
                startActivity(intent);
                finish();
            }
        });
        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(signup_page1.this, basket_page.class);
                startActivity(intent);
                finish();
            }
        });

        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    TERMS_AGREE_1=1;
                } else{
                    TERMS_AGREE_1=0;
                }
            }
        });

        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    TERMS_AGREE_2=1;
                } else {
                    TERMS_AGREE_2=0;
                }
            }
        });

        check3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check1.setChecked(true);
                    check2.setChecked(true);
                    TERMS_AGREE_3 = 1;
                } else {
                    check1.setChecked(false);
                    check2.setChecked(false);
                    TERMS_AGREE_3 = 0;
                }
            }
        });

        BT_다음.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                if(TERMS_AGREE_3 != 1){
                    if(TERMS_AGREE_2 == 1){
                        if(TERMS_AGREE_1 == 1){
                            startActivity(new Intent(signup_page1.this,signup_page2.class));

                        } else {
                            Toast.makeText(getApplicationContext(),"약관을 체크해주세요.",Toast.LENGTH_LONG).show();
                            return;
                        }
                    } else {
                        Toast.makeText(getApplicationContext(),"약관을 체크해주세요.",Toast.LENGTH_LONG).show();
                        return;
                    }
                } else {
                    Intent intent = new Intent(signup_page1.this,signup_page2.class);
                    startActivity(intent);
                }


            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Signup_onStart",실행);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Signup_onResume",실행);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.v("Signup_onPause",실행);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Signup_onStop",실행);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Signup_onDestroy",실행);
    }


}
