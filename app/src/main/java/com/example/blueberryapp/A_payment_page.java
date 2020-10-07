package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.squareup.picasso.Picasso;

public class A_payment_page extends AppCompatActivity {


    private Button BT_이용후기, BT_매거진, BT_질문답변, BT_장바구니추가, BT_바로구매;
    private TextView TV_로그아웃, TV_마이페이지, TV_장바구니, food_item_name, food_item_price;
    private ImageView IV_사진, IV_전화기;
    private String 실행 = "실행";
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_payment_page);


        BT_바로구매 = findViewById(R.id.BT_바로구매);
        BT_장바구니추가 = findViewById(R.id.BT_장바구니추가);
        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기 = findViewById(R.id.BT_이용후기);
        BT_매거진 = findViewById(R.id.BT_매거진);
        BT_질문답변 = findViewById(R.id.BT_질문답변);
        TV_마이페이지 = findViewById(R.id.TV_마이페이지);
        TV_장바구니 = findViewById(R.id.TV_장바구니);
        TV_로그아웃 = findViewById(R.id.TV_로그아웃);
        Log.v("A_payment_onCreate", 실행);
        IV_전화기 = findViewById(R.id.IV_전화기);
        imageSwitcher = findViewById(R.id.A결재창광고창);

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



        Intent intent = getIntent();


        String FoodName = intent.getExtras().getString("FoodName");
        String FoodPrice = intent.getExtras().getString("FoodPrice");
        String FoodImage = intent.getExtras().getString("FoodImage");



        TextView food_item_name = findViewById(R.id.food_item_name);
        TextView food_item_price = findViewById(R.id.food_item_price);
        ImageView food_item_image = findViewById(R.id.food_item_image);

        String str1 = FoodName;
        String str2 = FoodPrice;

        food_item_name.setText(str1);
        food_item_price.setText(str2);
        Picasso.with(this).load(FoodImage).into(food_item_image);



        BT_장바구니추가.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_payment_page.this, A_basket_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_바로구매.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
                Intent intent = new Intent(A_payment_page.this, A_main_page.class);
                startActivity(intent);
                finish();
            }
        });


        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_payment_page.this, A_review_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_payment_page.this, A_magazine_page.class);
                startActivity(intent);
                finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_payment_page.this, A_qna_page.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                finish();
            }
        });
        TV_로그아웃.setClickable(true);
        TV_로그아웃.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_payment_page.this, main_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_마이페이지.setClickable(true);
        TV_마이페이지.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_payment_page.this, A_my_page.class);
                startActivity(intent);
                finish();
            }
        });


        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {


            Intent intent = getIntent();

            String FoodName = intent.getExtras().getString("FoodName");
            String FoodPrice = intent.getExtras().getString("FoodPrice");
            String FoodImage = intent.getExtras().getString("FoodImage");

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_payment_page.this, A_basket_page.class);

                intent.putExtra("FoodName", FoodName);
                intent.putExtra("FoodPrice", FoodPrice);
                intent.putExtra("FoodImage", FoodImage);

                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("A_payment_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("A_payment_onResume", 실행);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("A_payment_onPause", 실행);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("A_payment_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("A_payment_onDestroy", 실행);
    }
}
