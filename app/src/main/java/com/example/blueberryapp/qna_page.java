package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

public class qna_page extends AppCompatActivity {

    static final int REQUEST = 1;
    private ArrayList<RE_QnA> qnaList = new ArrayList<>();

    private Context mContext;
    private RE_QnAAdapter qnaAdapter;
    private RecyclerView mRecyclerView;
    private FirebaseDatabase database;
    private DatabaseReference reference;


    private Button  BT_이용후기,  BT_매거진, BT_질문답변, BT_글쓰기;
    private TextView TV_로그인, TV_회원가입, TV_장바구니;
    private ImageView IV_사진,IV_전화기;
    private String 실행 = "실행";
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna_page);



        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기 = findViewById(R.id.BT_이용후기);
        BT_매거진 = findViewById(R.id.BT_매거진);
        BT_질문답변 = findViewById(R.id.BT_질문답변);
        TV_회원가입 = findViewById(R.id.TV_회원가입);
        TV_장바구니 = findViewById(R.id.TV_장바구니);
        TV_로그인 = findViewById(R.id.TV_로그인);
        BT_글쓰기 = findViewById(R.id.BT_글쓰기);
        Log.v("qna_onCreate",실행);
        IV_전화기 = findViewById(R.id.IV_전화기);
        imageSwitcher = findViewById(R.id.QnA광고창);

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




        mRecyclerView = findViewById(R.id.Re_글쓰기);


        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);



        qnaAdapter = new RE_QnAAdapter(mContext,qnaList);
        mRecyclerView.setAdapter(qnaAdapter);

        DividerItemDecoration QnAdividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(QnAdividerItemDecoration);

        //Firebase
        reference = FirebaseDatabase.getInstance().getReference();

        // ArrayList
        qnaList = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

//        //클릭이벤트
//        qnaAdapter.setOnItemClickListener(new OnListItemClickListener() {
//            @Override
//            public void onItemClick(RE_QnAAdapter.ListViewHolder holder, View view, int position) {
//
//                new AlertDialog.Builder(qna_page.this)
//                        .setMessage("로그인해야 이용이 가능합니다.")
//                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getApplicationContext(), "확인 누름", Toast.LENGTH_LONG).show();
//                                Intent intent = new Intent(qna_page.this, Login_Page.class);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(getApplicationContext(), "취소 누름", Toast.LENGTH_LONG).show();
//
//                            }
//                        }).show(); // 메세지 다이얼 띄우는 코드
//
//            } // 어댑터에 리스너 설정하기.
//
//
//
//
//        });


        IV_전화기.setClickable(true);
        IV_전화기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:010-8885-3426"));
                startActivity(intent);
            }
        });
        BT_글쓰기.setClickable(true);
        BT_글쓰기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(qna_page.this)
                        .setMessage("로그인해야 이용이 가능합니다.")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "확인 누름", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(qna_page.this, Login_Page.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "취소 누름", Toast.LENGTH_LONG).show();

                            }
                        }).show(); // 메세지 다이얼 띄우는 코드

            }
        });

        IV_사진.setClickable(true);
        IV_사진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qna_page.this, main_page.class);
                startActivity(intent);
                finish();
            }
        });



        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qna_page.this, review_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qna_page.this, magazine_page.class);
                startActivity(intent);
                finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qna_page.this, qna_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_로그인.setClickable(true);
        TV_로그인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qna_page.this, Login_Page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_회원가입.setClickable(true);
        TV_회원가입.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qna_page.this, signup_page1.class);
                startActivity(intent);
                finish();
            }
        });
        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(qna_page.this, basket_page.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDataFromFirebase() {

        Query query = reference.child("QnA");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    // push some data from firebase to class what I made already
//                    RE_Food re_food = dataSnapshot.getValue(RE_Food.class);
                    //                    foodList.add(re_food);

                    // ready to push data what in the box to RecyclerView

                    RE_QnA re_qna = new RE_QnA();
                    re_qna.setQnATitle(Objects.requireNonNull(snapshot.child("qnATitle").getValue()).toString());
                    re_qna.setQnAWriting(Objects.requireNonNull(snapshot.child("qnAWriting").getValue()).toString());

                    qnaList.add(re_qna);
                }

                qnaAdapter = new RE_QnAAdapter(mContext, qnaList);
                //connect Adapter with RecyclerView
                mRecyclerView.setAdapter(qnaAdapter);
                qnaAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void ClearAll() {
        if (qnaList != null) {
            qnaList.clear();
            if (qnaAdapter != null) {
                qnaAdapter.notifyDataSetChanged();
            }
        }

        qnaList = new ArrayList<>();
    }

//    private void savaData(){ //이 메소드가 shardpreferences에 저장해준다.
//        SharedPreferences sharedPreferences = getSharedPreferences("회원정보",MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(qnaList);
//        editor.putString("ID",json);
//        editor.apply();
//    }
//
//    private void loadQnAData() { //읽어오는 메소드
//        SharedPreferences sharedPreferences = getSharedPreferences("질문", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("QnAList", null);
//        Type type = new TypeToken<ArrayList<RE_QnA>>() {
//        }.getType();
//        qnaList = gson.fromJson(json, type);
//
//        if (qnaList == null) {
//            qnaList = new ArrayList<>();
//        }
//    }

    @Override
    protected void onStart() {
        super.onStart();
        // Get Data Method
        getDataFromFirebase();
        Log.v("qna_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("qna_onResume", 실행);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("qna_onPause", 실행);


    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("qna_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("qna_onDestroy", 실행);
    }
}
