package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class A_basket_page extends AppCompatActivity implements RE_FoodAdapter_Basket.OnItemClickListener {


    public static final String GET_NAME = "get Name";
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;




    private Button BT_이용후기,BT_매거진,BT_질문답변;
    private TextView TV_로그아웃,TV_마이페이지,TV_장바구니;
    private ImageView IV_사진,IV_전화기;
    private String 실행 = "실행";

    //장바구니 구현
    private RecyclerView recyclerView;
    private RE_FoodAdapter_Basket re_foodAdapter_basket;

    private ArrayList<RE_Food> FoodList;
    private ArrayList<UserInfo> UserList;


    private FirebaseDatabase database;
    private DatabaseReference reference;
    private StorageReference storageRef;

    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private CollectionReference UsersCRef = firestore.collection("Users");
    private CollectionReference foodBasketCRef = UsersCRef.document(MyApplication.회원Email).collection("Basket");



    //장바구니 연산 구현
//    private EditText ET_itemCounter;
//    private Button BT_countUp, BT_countDown, BT_deleteItem;
    private TextView TV_totalAmount;
    private LinearLayout BT_payment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_basket_page);

        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기= findViewById(R.id.BT_이용후기);
        BT_매거진= findViewById(R.id.BT_매거진);
        BT_질문답변= findViewById(R.id.BT_질문답변);
        TV_마이페이지= findViewById(R.id.TV_마이페이지);
        TV_장바구니= findViewById(R.id.TV_장바구니);
        TV_로그아웃= findViewById(R.id.TV_로그아웃);
        Log.v("A_basket_onCreate",실행);
        IV_전화기 = findViewById(R.id.IV_전화기);
        imageSwitcher = findViewById(R.id.A장바구니광고창);
        recyclerView = findViewById(R.id.RE_장바구니);

        TV_totalAmount = findViewById(R.id.TV_totalCost);
        BT_payment = findViewById(R.id.BT_payment);


        String Cost = String.valueOf(MyApplication.결제금액);

        TV_totalAmount.setText(Cost);


        BT_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(A_basket_page.this,"결제 클릭",Toast.LENGTH_SHORT).show();
            }
        });

        // ArrayList
        FoodList = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

        // Get Data Method
        getDataFromFireStore();




        //TODO : Intent로 받은 내용을 리사이클러뷰로 구현하기.
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
                Intent intent = new Intent(A_basket_page.this, A_main_page.class);
                startActivity(intent);
                finish();
            }
        });



        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_basket_page.this, A_review_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_basket_page.this, A_magazine_page.class);
                startActivity(intent);
                finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_basket_page.this, A_qna_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_로그아웃.setClickable(true);
        TV_로그아웃.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_basket_page.this, main_page.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                finish();
            }
        });
        TV_마이페이지.setClickable(true);
        TV_마이페이지.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_basket_page.this, A_my_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_basket_page.this, A_basket_page.class);
                startActivity(intent);
                finish();
            }
        });

        //장바구니 구현
        recyclerView = findViewById(R.id.RE_장바구니);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1); // 갯수
        recyclerView.setLayoutManager(layoutManager);


        //TODO : 왜 매니저 창에는 뜨는데 Main에는 데이터값이 안뜨는 지 이유를 알아보고 정리하기.  (메인과 로드했을때의 메인에 해당 값 띄우기. + 게시판 만들기.)

        DividerItemDecoration FooddividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(FooddividerItemDecoration);

        //Firebase
        reference = FirebaseDatabase.getInstance().getReference();

        storageRef = FirebaseStorage.getInstance().getReference("foodImages");

        // ArrayList
        UserList = new ArrayList<>();
        getDataFromFireStore();

    }


    private void getDataFromFireStore() {

        re_foodAdapter_basket = new RE_FoodAdapter_Basket(A_basket_page.this, FoodList);
        //connect Adapter with RecyclerView
        recyclerView.setAdapter(re_foodAdapter_basket);

        //클릭 리스너 구현
        re_foodAdapter_basket.setOnBasketItemClickListener(A_basket_page.this);

        foodBasketCRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                FoodList.clear();


                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RE_Food re_food = documentSnapshot.toObject(RE_Food.class);


                    FoodList.add(re_food);
                    re_foodAdapter_basket.notifyDataSetChanged();
                }

            }
        });
    }

    private void ClearAll() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("A_basket_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("A_basket_onResume", 실행);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("A_basket_onPause", 실행);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("A_basket_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("A_basket_onDestroy", 실행);
    }

    @Override
    public void onItemClick(int position) {
        startActivity(new Intent(A_basket_page.this,A_basket_page.class));
        finish();
    }
}
