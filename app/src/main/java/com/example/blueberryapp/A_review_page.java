package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.blueberryapp.R.layout.edit_box;
import static com.example.blueberryapp.R.layout.review_editbox;
import static com.example.blueberryapp.R.layout.review_list;

public class A_review_page extends AppCompatActivity implements RE_REVIEW_test_Adapter.OnItemClickListener {

    //    static final int REQUEST = 100;
//
//    private static ArrayList<RE_Review> reviewList;
//    private RE_ReviewAdapter reviewAdapter;
    private Thread thread;
    //
    //    private RecyclerView mRecyclerView;
//
//    private Context mContext;
//
//
    //    private EditText editTextTitle, editTextWriting;
    private Handler handler = new Handler();
    private boolean aBoolean;
    private ImageSwitcher imageSwitcher;
    private Button BT_이용후기, BT_매거진, BT_질문답변, A_BT_글쓰기;
    private TextView TV_로그아웃, TV_마이페이지, TV_장바구니;
    private ImageView IV_사진, IV_전화기;
    private String 실행 = "실행";
//    private String userID = MyApplication.회원ID;
//
//    private FirebaseDatabase database;
//    private DatabaseReference reference;

    //Widgets
    RecyclerView recyclerView;

    //Firebase
    private DatabaseReference databaseReference;

    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference StoreCRef = DB.collection("REVIEW");

    // Variables
    private ArrayList<RE_REVIEW_test> REVIEWList;
    private RE_REVIEW_test_Adapter re_review_test_adapter;
    private Context mContext;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference("REVIEW_images");


    private FirebaseFirestore firestore;
    private FirebaseStorage mStorage;

    public boolean B_ItemView = false;

    Button 추가; //바꿔야 하는 부분.

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



       /* mRecyclerView = findViewById(R.id.Re_review);
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
        ClearAll();*/

        추가 = findViewById(R.id.BT_추가);
        recyclerView = findViewById(R.id.Re_review);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        // 부가 기능
        DividerItemDecoration FooddividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(FooddividerItemDecoration);

        //Firebase
//        databaseReference = FirebaseDatabase.getInstance().getReference();
        firestore = FirebaseFirestore.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference("FoodImages");


        A_BT_글쓰기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(A_review_page.this, A_review_writing_page.class);
                startActivity(intent);
                finish();
            }
        });

        //TODO : Store에서 가져올수 있게 변경하기. 참조 가능하게 만들기.
        //TODO : 아래 스냅샷에서 어떤역할을 하는지 정확히 알아보기.


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

    private void getDataFromFireStore() {

        re_review_test_adapter = new RE_REVIEW_test_Adapter(A_review_page.this, REVIEWList);
        recyclerView.setAdapter(re_review_test_adapter);

        //클릭 리스너 구현
        re_review_test_adapter.setOnItemClickListener(A_review_page.this);

        StoreCRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                REVIEWList.clear();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    RE_REVIEW_test re_review_test = documentSnapshot.toObject(RE_REVIEW_test.class);
                    REVIEWList.add(re_review_test);
                }


                re_review_test_adapter.notifyDataSetChanged();

            }

        });
    }


//    private void getDataFromFirebase() {
//
//        Query query = reference.child("Review");
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                ClearAll();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//
//                    // push some data from firebase to class what I made already
////                    RE_Food re_food = dataSnapshot.getValue(RE_Food.class);
//                    //                    foodList.add(re_food);
//
//                    // ready to push data what in the box to RecyclerView
//
//                    RE_Review re_review = new RE_Review();
//
//
//                    re_review.setReViewTitle(Objects.requireNonNull(snapshot.child("reViewTitle").getValue()).toString());
//                    re_review.setReViewWriting(Objects.requireNonNull(snapshot.child("reViewWriting").getValue()).toString());
//
//                    reviewList.add(re_review);
//
//                }
//
//
//                reviewAdapter = new RE_ReviewAdapter(mContext, reviewList);
//                //connect Adapter with RecyclerView
//                mRecyclerView.setAdapter(reviewAdapter);
//                reviewAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }


    private void ClearAll() {
        if (REVIEWList != null) {
            REVIEWList.clear();
            if (re_review_test_adapter != null) {
                re_review_test_adapter.notifyDataSetChanged();
            }
        }

        REVIEWList = new ArrayList<>();
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
        // ArrayList
        REVIEWList = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

        // Get Data Method
        getDataFromFireStore();

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
        B_ItemView = false;

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

    //아이템 클릭 리스너 구현 >> 어댑터에 정의한 내용을 implement해서 가져와서 연동시킴.
    @Override
    public void onItemClick(int position) {
        Toast.makeText(this, "클릭 포지션 값 : " + position, Toast.LENGTH_SHORT).show();

        RE_REVIEW_test selectedItem = REVIEWList.get(position);
        final String selectedTitle = selectedItem.getTitle_Review();


        String 다큐제목 = REVIEWList.get(position).getDocuName();
        String title = REVIEWList.get(position).getTitle_Review();
        String writing = REVIEWList.get(position).getWritingReview();
        String Image = REVIEWList.get(position).getImageUrl();
        String userName = REVIEWList.get(position).getUserName();

        Log.d("Test",">"+writing);
        Log.d("Test",">"+userName);

        Intent intent = new Intent(A_review_page.this, A_review_page_clicked.class);
        intent.putExtra("다큐제목",다큐제목);
        intent.putExtra("Title",title);
        intent.putExtra("Writing",writing);
        intent.putExtra("Image",Image);
        intent.putExtra("UserName",userName);
        startActivity(intent);
    }

    @Override
    public void onWhatEverClick(int position) {
        Toast.makeText(this, "이미 값 포지션 값 : " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeletClick(int position) {
        Toast.makeText(this, "지우기 포지션 값 : " + position, Toast.LENGTH_SHORT).show();

        RE_REVIEW_test selectedItem = REVIEWList.get(position);
        final String selectedTitle = selectedItem.getTitle_Review();

        StoreCRef.document(selectedTitle)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(A_review_page.this, "아이템 삭제 완료.", Toast.LENGTH_SHORT).show();
                    }
                });

//        // Create a storage reference from our app
//        StorageReference storageRef = mStorageRef;
//
//        // Create a reference to the file to delete
//        StorageReference desertRef = storageRef.child(selectedItem.ImageUrl);
//
//        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                // File deleted successfully
//            }
//        });
    }
}
