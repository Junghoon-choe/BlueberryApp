package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
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

import static com.example.blueberryapp.R.layout.edit_box;
import static com.example.blueberryapp.R.layout.review_editbox;
import static com.example.blueberryapp.R.layout.review_list;

public class A_review_page extends AppCompatActivity {
    static final int REQUEST = 100;

    private static ArrayList<RE_Review> reviewList;
    private RE_ReviewAdapter reviewAdapter;
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;

    private RecyclerView mRecyclerView;

    private Context mContext;


    private EditText editTextTitle, editTextWriting;
    private Button BT_이용후기, BT_매거진, BT_질문답변, A_BT_글쓰기;
    private TextView TV_로그아웃, TV_마이페이지, TV_장바구니;
    private ImageView IV_사진, IV_전화기;
    private String 실행 = "실행";
    private String userID = MyApplication.회원ID;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_review_page);
//        getReViewData();

        A_BT_글쓰기 = findViewById(R.id.A_BT_글쓰기);
        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기 = findViewById(R.id.BT_이용후기);
        BT_매거진 = findViewById(R.id.BT_매거진);
        BT_질문답변 = findViewById(R.id.BT_질문답변);
        TV_마이페이지 = findViewById(R.id.TV_마이페이지);
        TV_장바구니 = findViewById(R.id.TV_장바구니);
        TV_로그아웃 = findViewById(R.id.TV_로그아웃);
        Log.v("A_review_onCreate", 실행);
        IV_전화기 = findViewById(R.id.IV_전화기);
        imageSwitcher = findViewById(R.id.A후기광고창);

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


        mRecyclerView = findViewById(R.id.Re_review);
        LinearLayoutManager ReviewLinearLayoutManager = new LinearLayoutManager(this);
        ReviewLinearLayoutManager.setReverseLayout(true);
        ReviewLinearLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(ReviewLinearLayoutManager);

        DividerItemDecoration ReviewdividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                ReviewLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(ReviewdividerItemDecoration);

        //Firebase
        reference = FirebaseDatabase.getInstance().getReference();

        // ArrayList
        reviewList = new ArrayList<>();

        //Clear ArrayList
        ClearAll();




        A_BT_글쓰기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(A_review_page.this, A_review_writing_page.class);
                startActivity(intent);


            }
        });

//        A_BT_글쓰기.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*Intent intent = new Intent(A_review_page.this, A_add_review.class);
//
//                startActivityForResult(intent,REQUEST);*/
//
//
//                //1. 글쓰기 버튼을 클릭하면
//
//                //2. 레이아웃 파일 edit_box를 불러와서 화면에 다이얼로그를 보여줍니다.
//                AlertDialog.Builder builder = new AlertDialog.Builder(A_review_page.this);
//                View view = LayoutInflater.from(A_review_page.this).inflate(review_editbox, null, false);
//                builder.setView(view);
//
//
//                //
//                // QnA생성자를 사용하여 어레이리스트에 삽입할 데이터를 만든다.
//                // RE_QnA data = new RE_QnA(count+"","제목"+count,"글 입력");
//
//                //                 mArrayList.add(0, qna); >> 뷰의 첫줄에 삽입
//                //                mArrayList.add(data); >> 뷰의 마지막줄에 삽입
//
//                //                  mAdapter.notifyDataSetChanged(); 변경된 데이터를 화면에 반영한다
//
//
//                final Button ButtonSubmit = view.findViewById(R.id.BT_edit);
//
//                final EditText editTextTitle = view.findViewById(R.id.review_edit_title);
//                final EditText editTextWriting = view.findViewById(R.id.review_edit_writing);
//
//
//                Log.v("실행", String.valueOf(ButtonSubmit));
//                ButtonSubmit.setText("데이터 추가");
//
//
//                final AlertDialog dialog = builder.create();
//                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//
//
//                        SharedPreferences 후기 = getSharedPreferences("후기정보", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = 후기.edit(); // 사용자가 입력한 내용을 저장할 데이터 이름 = 후기정보
//
//                        String strId = userID;
//
//                        String strTitle = editTextTitle.getText().toString();
//                        String strWriting = editTextWriting.getText().toString();
//
//                        RE_Review review = new RE_Review(strId, strTitle, strWriting);
//                        ReviewArrayList.add(review);// 첫줄에 삽입*//*
//
//                        //mArrayList.add(qna); >> 마지막 줄에 삽입
//                        ReviewAdapter.notifyDataSetChanged(); // 변경된 데이터를 화면에 반영
//
//                        Gson gson = new Gson();
//                        String userListResult = gson.toJson(review);
//                        editor.putString(strId, userListResult);
//                        editor.apply();
//
//                        //--------------
//
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//
//
//               /* count ++;
//                RE_QnA data = new RE_QnA(count+"","제목"+count,"글 입력");
//
//
//
//
//                mArrayList.add(data);
//                mAdapter.notifyDataSetChanged();*/
//
//            }
//        });

//        //클릭이벤트
//        reviewAdapter.setOnReviewItemClickListener(new OnReviewItemClickListener() {
//            @Override
//            public void onReviewItemClick(RE_ReviewAdapter.ReviewHolder reviewHolder, View view, int position) {
//                {
//                    RE_Review item = reviewAdapter.getItem(position);
//                    Toast.makeText(getApplicationContext(),
//                            "아이템 선택됨 : " + item.getReViewTitle(), Toast.LENGTH_SHORT).show();
//
//                    Intent intent = new Intent(getBaseContext(), A_review_page.class);
//
//
//                    intent.putExtra("ReViewTitle", item.getReViewTitle());
//                    intent.putExtra("ReViewWriting", item.getReViewWriting());
//
//                    startActivity(intent);
//
//
//                }
//            } // 어댑터에 리스너 설정하기.
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
        IV_사진.setClickable(true);
        IV_사진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_review_page.this, A_main_page.class);
                startActivity(intent);
                finish();
            }
        });


        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_review_page.this, A_review_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_review_page.this, A_magazine_page.class);
                startActivity(intent);
                finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_review_page.this, A_qna_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_로그아웃.setClickable(true);
        TV_로그아웃.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_review_page.this, main_page.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                finish();
            }
        });
        TV_마이페이지.setClickable(true);
        TV_마이페이지.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_review_page.this, A_my_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_review_page.this, A_basket_page.class);
                startActivity(intent);
                finish();
            }
        });
    }


    private void getDataFromFirebase() {

        Query query = reference.child("Review");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    // push some data from firebase to class what I made already
//                    RE_Food re_food = dataSnapshot.getValue(RE_Food.class);
                    //                    foodList.add(re_food);

                    // ready to push data what in the box to RecyclerView

                    RE_Review re_review = new RE_Review();


                    re_review.setReViewTitle(Objects.requireNonNull(snapshot.child("reViewTitle").getValue()).toString());
                    re_review.setReViewWriting(Objects.requireNonNull(snapshot.child("reViewWriting").getValue()).toString());

                    reviewList.add(re_review);

                }


                reviewAdapter = new RE_ReviewAdapter(mContext, reviewList);
                //connect Adapter with RecyclerView
                mRecyclerView.setAdapter(reviewAdapter);
                reviewAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    private void ClearAll() {
        if (reviewList != null) {
            reviewList.clear();
            if (reviewAdapter != null) {
                reviewAdapter.notifyDataSetChanged();
            }
        }

        reviewList = new ArrayList<>();
    }





/*
    final  SharedPreferences 후기 = getSharedPreferences("후기정보", MODE_PRIVATE);
    final  SharedPreferences.Editor editor = 후기.edit(); // 사용자가 입력한 내용을 저장할 데이터 이름 = 회원정보


    AlertDialog.Builder builder = new AlertDialog.Builder(A_review_page.this);
    View view = LayoutInflater.from(A_review_page.this).inflate(review_editbox,null,false);
                builder.setView(view);

    final Button ButtonSubmit = view.findViewById(R.id.BT_edit);

    EditText editTextTitle = view.findViewById(R.id.review_edit_title);
    EditText editTextWriting = view.findViewById(R.id.review_edit_writing);


                Log.v("실행",String.valueOf(ButtonSubmit));
                ButtonSubmit.setText("데이터 추가");



    final AlertDialog dialog = builder.create();
                ButtonSubmit.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String strId = userID;

            String strTitle = editTextTitle.getText().toString();
            String strWriting = editTextWriting.getText().toString();

            RE_Review review = new RE_Review(strId,strTitle,strWriting);
            ReviewArrayList.add(review);// 첫줄에 삽입

            //mArrayList.add(qna); >> 마지막 줄에 삽입
            ReviewAdapter.notifyDataSetChanged(); // 변경된 데이터를 화면에 반영

            Gson gson = new Gson();

            String userListResult = gson.toJson(review);
            editor.putString(strId,userListResult);
            editor.commit();

            dialog.dismiss();
        }
    });
                dialog.show();*/


               /* count ++;
                RE_QnA data = new RE_QnA(count+"","제목"+count,"글 입력");




                mArrayList.add(data);
                mAdapter.notifyDataSetChanged();*/


    //SharedPreference
//    public void saveUserData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("후기정보", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        String strId = userID;
//        String strTitle = editTextTitle.getText().toString();
//        String strWriting = editTextWriting.getText().toString();
//
//        UserReview userReview = new UserReview(strId, strTitle, strWriting);
//
//        reviewList.add(userReview);
//        Gson gson = new Gson();
//        String json = gson.toJson(userReview);
//        editor.putString(strId, json);
//        editor.apply();
//    }
//
//    public void loadReViewData() { //읽어오는 메소드
//        SharedPreferences sharedPreferences = getSharedPreferences("후기정보", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("회원ID", null);
//        Type type = new TypeToken<ArrayList<RE_Review>>() {
//        }.getType();
//        reviewList = gson.fromJson(json, type);
//
//        if (reviewList == null) {
//            reviewList = new ArrayList<>();
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();
        // Get Data Method
        getDataFromFirebase();
        Log.v("A_review_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("A_review_onResume", 실행);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("A_review_onPause", 실행);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("A_review_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("A_review_onDestroy", 실행);
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) {
            return;
        }


        if (requestCode == REQUEST) {

            // epdlxjfmf 받아옴.



            String 제목 = data.getExtras().getString("ReViewTitle");
            String 후기글 = data.getExtras().getString("ReViewWriting");


            data.getStringExtra("ReViewTitle");
            data.getStringExtra("ReViewWriting");

            RE_Review re_review = new RE_Review(userID,제목,후기글);
            ReviewArrayList.add(re_review);
            ReviewAdapter.notifyDataSetChanged();

        }


    }*/

}
