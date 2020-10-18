package com.example.blueberryapp;

import androidx.annotation.NonNull;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class A_payment_page extends AppCompatActivity {


    public static final String TAG = "ItemAdder";
    private Button BT_이용후기, BT_매거진, BT_질문답변, BT_장바구니추가, BT_바로구매;
    private TextView TV_로그아웃, TV_마이페이지, TV_장바구니, food_item_name, food_item_price;
    private ImageView IV_사진, IV_전화기;
    private String 실행 = "실행";
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;

    ArrayList<UserInfo> UserList;

//    private FirebaseDatabase database;
//    private DatabaseReference reference;
//    private StorageReference storageRef;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference UsersCRef = firestore.collection("Users");
    private CollectionReference BasketCRef = UsersCRef.document().collection("Basket");


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


        final String FoodName = intent.getExtras().getString("FoodName");
        final String FoodPrice = intent.getExtras().getString("FoodPrice");
        final String FoodImage = intent.getExtras().getString("FoodImage");
        final String FoodAmount = intent.getExtras().getString("FoodAmount");



        TextView food_item_name = findViewById(R.id.food_item_name);
        TextView food_item_price = findViewById(R.id.food_item_price);
        ImageView food_item_image = findViewById(R.id.food_item_image);


        food_item_name.setText(FoodName);
        food_item_price.setText(FoodPrice);
        Picasso.with(this).load(FoodImage).into(food_item_image);


        BT_장바구니추가.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                addItemToBasket(FoodName, FoodPrice, FoodImage, FoodAmount);
            }
        });

        BT_바로구매.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO : 카카오페이 결제창으로 이동.
                startActivity(new Intent(A_payment_page.this, kakaoPay.class));
//                HttpPostData();
            }
        });

        //                POST /v1/payment/ready HTTP/1.1
//                Host: kapi.kakao.com
//                Authorization: KakaoAK {APP_ADMIN_KEY}
//                Content-type: application/x-www-form-urlencoded;charset=utf-8



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
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_payment_page.this, A_basket_page.class);
                startActivity(intent);
                finish();
            }
        });
    }

//    private void HttpPostData() {
//        try {
//            //--------------------------
//            //   URL 설정하고 접속하기
//            //--------------------------
//            URL url = new URL("http://kapi.kakao.com/v1/payment/ready");       // URL 설정
//            HttpURLConnection http = (HttpURLConnection) url.openConnection();   // 접속
//            //--------------------------
//            //   전송 모드 설정 - 기본적인 설정이다
//            //--------------------------
//            http.setDefaultUseCaches(false);
//            http.setDoInput(true);                         // 서버에서 읽기 모드 지정
//            http.setDoOutput(true);                       // 서버로 쓰기 모드 지정
//            http.setRequestMethod("POST");         // 전송 방식은 POST
//
//            // 서버에게 웹에서 <Form>으로 값이 넘어온 것과 같은 방식으로 처리하라는 걸 알려준다
//            http.setRequestProperty("content-type", "application/x-www-form-urlencoded;charset=utf-8");
//            //--------------------------
//            //   서버로 값 전송
//            //--------------------------
//            StringBuffer buffer = new StringBuffer();
//            buffer.append("id").append("=").append(myId).append("&");                 // php 변수에 값 대입
//            buffer.append("pword").append("=").append(myPWord).append("&");   // php 변수 앞에 '$' 붙이지 않는다
//            buffer.append("title").append("=").append(myTitle).append("&");           // 변수 구분은 '&' 사용
//            buffer.append("subject").append("=").append(mySubject);
//
//            OutputStreamWriter outStream = new OutputStreamWriter(http.getOutputStream(), "EUC-KR");
//            PrintWriter writer = new PrintWriter(outStream);
//            writer.write(buffer.toString());
//            writer.flush();
//            //--------------------------
//            //   서버에서 전송받기
//            //--------------------------
//            InputStreamReader tmp = new InputStreamReader(http.getInputStream(), "EUC-KR");
//            BufferedReader reader = new BufferedReader(tmp);
//            StringBuilder builder = new StringBuilder();
//            String str;
//            while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
//                builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
//            }
//            myResult = builder.toString();                       // 전송결과를 전역 변수에 저장
//            ((TextView)(findViewById(R.id.text_result))).setText(myResult);
//            Toast.makeText(MainActivity.this, "전송 후 결과 받음", 0).show();
//        } catch (IOException e) {
//            //
//        } // try
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    private void addItemToBasket(final String foodName, final String foodPrice, final String foodImage, final String foodAmount) {
        RE_Food re_food = new RE_Food(foodName, foodPrice, foodImage, foodAmount);

        UsersCRef.document(MyApplication.회원Email).collection("Basket").document(foodName).set(re_food)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Intent intent = new Intent(A_payment_page.this, A_basket_page.class);

                        intent.putExtra("FoodName", foodName);
                        intent.putExtra("FoodPrice", foodPrice);
                        intent.putExtra("FoodImage", foodImage);

                        startActivity(intent);

                        Log.d(TAG, "Document has been saved!");
                        Toast.makeText(A_payment_page.this, "User saved!", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Document was not saved!", e);
                Toast.makeText(A_payment_page.this, "Error!", Toast.LENGTH_SHORT).show();
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
