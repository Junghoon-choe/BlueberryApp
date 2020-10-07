package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
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
import android.widget.CheckBox;
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

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Objects;

import static com.example.blueberryapp.R.layout.edit_box;
import static com.example.blueberryapp.R.layout.qna_list;

public class A_qna_page extends AppCompatActivity {


    static final int REQUEST = 1;
    private static ArrayList<RE_QnA> qnaList;
    private RE_QnAAdapter qnaAdapter;


    private Button BT_이용후기, BT_매거진, BT_질문답변, A_BT_글쓰기;
    private TextView TV_로그아웃, TV_마이페이지, TV_장바구니;
    private ImageView IV_사진, IV_전화기;


    private String 실행 = "실행";
    private Handler handler = new Handler();
    private boolean aBoolean;
    private Thread thread;
    private ImageSwitcher imageSwitcher;


    private RecyclerView mRecyclerView;
    private String userID = MyApplication.회원ID;
    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_qna_page);




        A_BT_글쓰기 = findViewById(R.id.A_BT_글쓰기);
        IV_사진 = findViewById(R.id.IV_사진);
        BT_이용후기 = findViewById(R.id.BT_이용후기);
        BT_매거진 = findViewById(R.id.BT_매거진);
        BT_질문답변 = findViewById(R.id.BT_질문답변);
        TV_마이페이지 = findViewById(R.id.TV_마이페이지);
        TV_장바구니 = findViewById(R.id.TV_장바구니);
        TV_로그아웃 = findViewById(R.id.TV_로그아웃);
        Log.v("A_qna_onCreate", 실행);
        IV_전화기 = findViewById(R.id.IV_전화기);
        imageSwitcher = findViewById(R.id.AQnA광고창);

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
//        mAdapter.setOnItemClickListener(new OnListItemClickListener() {
//            @Override
//            public void onItemClick(RE_QnAAdapter.ListViewHolder holder, View view, int position) {
//                RE_QnA item = mAdapter.getItem(position);
//                Toast.makeText(getApplicationContext(),
//                        "아이템 선택됨 : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//
//                Intent intent = new Intent(getBaseContext(), A_QnAClick.class);
//
//                intent.putExtra("id", item.getId());
//                intent.putExtra("title", item.getTitle());
//                intent.putExtra("writing", item.getWriting());
//
//                startActivity(intent);
//            } // 어댑터에 리스너 설정하기.
//
//
//        });


       /* QnA_CheckBox.setChecked(true);//체크박스를 체크합니다.
        QnA_CheckBox.setChecked(false);//체크박스의 체크를 해제합니다.*/

       /* A_BT_삭제.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (QnA_CheckBox.isChecked()){//체크박스가 체크된 경우 그 체크박스의 포지션을 얻어야함.
                    mAdapter.getItem();

                } else { // 체크박스의 체크가 해제 된 경우

                }


            }
        });
*/
        A_BT_글쓰기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(A_qna_page.this, Edit_box.class); // 1번값 : 전환시킬 화면, 2번값 : 밑에서 나올 requestCode로 받을값.
//                startActivityForResult(intent, REQUEST); //예제에서는 REQ_ADD_CONTACT 임.

                Intent intent = new Intent(A_qna_page.this, A_qna_writing_page.class);
                startActivity(intent);



            }
        });


        IV_사진.setClickable(true);
        IV_사진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_qna_page.this, A_main_page.class);
                startActivity(intent);
                finish();
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
                Intent intent = new Intent(A_qna_page.this, A_main_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_이용후기.setClickable(true);
        BT_이용후기.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_qna_page.this, A_review_page.class);
                startActivity(intent);
                finish();
            }
        });

        BT_매거진.setClickable(true);
        BT_매거진.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_qna_page.this, A_magazine_page.class);
                startActivity(intent);
                finish();
            }
        });
        BT_질문답변.setClickable(true);
        BT_질문답변.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_qna_page.this, A_qna_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_로그아웃.setClickable(true);
        TV_로그아웃.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_qna_page.this, main_page.class);
                FirebaseAuth.getInstance().signOut();
                startActivity(intent);
                finish();
            }
        });
        TV_마이페이지.setClickable(true);
        TV_마이페이지.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_qna_page.this, A_my_page.class);
                startActivity(intent);
                finish();
            }
        });
        TV_장바구니.setClickable(true);
        TV_장바구니.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_qna_page.this, A_basket_page.class);
                startActivity(intent);
                finish();
            }
        });
    }

//    private void saveQnAData() { //이 메소드가 shardpreferences에 저장해준다.
//        SharedPreferences sharedPreferences = getSharedPreferences("질문", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(mList);
//        editor.putString("QnAList", json);
//        editor.apply();
//    }
//
//    private void loadQnAData() { //읽어오는 메소드
//        SharedPreferences sharedPreferences = getSharedPreferences("질문", MODE_PRIVATE);
//        Gson gson = new Gson();
//        String json = sharedPreferences.getString("QnAList", null);
//        Type type = new TypeToken<ArrayList<RE_QnA>>() {
//        }.getType();
//        mList = gson.fromJson(json, type);
//
//        if (mList == null) {
//            mList = new ArrayList<>();
//        }
//    }


//    // 글쓰기 액티비티로 부터 가져온 데이터 표시 하는 코드
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // 취소버튼  눌렀을때 나옴. (화면을 취소할 경우 뒤로가기)
//        if (resultCode != RESULT_OK) {
//
//            return;
//
//        }
//        // 리퀘스트에서 값을 받아옴.
//        if (requestCode == REQUEST) {
//
//            // epdlxjfmf 받아옴.
//
//
//            String 제목 = data.getExtras().getString("title");
//            String ID = data.getExtras().getString("id");
//            String 질문 = data.getExtras().getString("writing");
//
//            data.getStringExtra("id");
//            data.getStringExtra("title");
//            data.getStringExtra("writing");
//
//            RE_QnA re_qna = new RE_QnA(ID, 제목, 질문);
//            mList.add(re_qna);
//            mAdapter.notifyDataSetChanged();
//
//        }
//
//
//    }



   /* mList.set(getAdapterPosition(),qna);

    //9. 어댑터에서 RecyclerView에 반영하도록 합니다.
    notifyItemChanged(getAdapterPosition());

    public ArrayList<RE_QnA> getmList() {

        mList.add(String title)

        return mList;
    }*/

    /*editTexttitle.setText(mList.get(getAdapterPosition()).getTitle());
    editTextwriting.setText(mList.get(getAdapterPosition()).getWriting());*/

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
//                    re_qna.setUserID(Objects.requireNonNull(snapshot.child("userID").getValue()).toString());
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


    @Override
    protected void onStart() {
        super.onStart();
        // Get Data Method
        getDataFromFirebase();
        Log.v("A_qna_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("A_qna_onResume", 실행);
        ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("A_qna_onPause", 실행);



    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("A_qna_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("A_qna_onDestroy", 실행);
    }


}
