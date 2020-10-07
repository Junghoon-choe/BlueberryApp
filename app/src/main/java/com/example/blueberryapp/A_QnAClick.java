package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

public class A_QnAClick extends AppCompatActivity {

    public EditText edit_title, edit_writing;
    public Button BT_qna_edit, BT_qna_remove;

    private Context mContext;
    static public ArrayList<RE_QnA> qnaList;
    static public RE_QnAAdapter qnaAdapter;

    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_qnaclick);


        BT_qna_edit = findViewById(R.id.BT_qna_edit);
        BT_qna_remove = findViewById(R.id.BT_qna_remove);
        qnaAdapter = new RE_QnAAdapter(mContext,qnaList);
        getDataFromFirebase();



        Bundle extras = getIntent().getExtras();

        String title = extras.getString("title");
        String writing = extras.getString("writing"); //밑에있는 키를 말함

        edit_title = findViewById(R.id.edit_title);
        edit_writing = findViewById(R.id.edit_writing);

        String str1 = title;
        String str2 = writing; // 키


        edit_title.setText(str1);
        edit_writing.setText(str2);



    }

//    public void BT_CLICK(View view) {
//
//       /* if(check.isChecked()) {
//            check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        TERMS_AGREE = 1;
//                    } else {
//                        TERMS_AGREE = 0;
//                    }
//                }
//            });
//        }*/
//
//
//        if (view.getId() == R.id.BT_qna_edit) {
//            updateData();
//
//
//            Intent intent = new Intent();
//            Log.v("Intent", 실행);
//
//            intent.putExtra("상품명", 상품명변경.getText().toString());
//            intent.putExtra("상품가격", 상품가격변경.getText().toString());
//
//
//
//
//            setResult(RESULT_OK, intent);
//            Log.v("RESULT_OK", 실행);
//
//
//            finish();
//            Log.v("finish", 실행);
//
//
//        } else if (view.getId() == R.id.BT_qna_remove) {
//            deleteData();
//            Intent intent = new Intent();
//            Log.v("Intent", 실행);
//            intent.putExtra("상품명", 상품명변경.getText().toString());
//            intent.putExtra("상품가격", 상품가격변경.getText().toString());
//
//
//
//
//
//
//            mList.remove(0);
//
//            mAdapter.notifyDataSetChanged();
//            saveData();
//
//
//        }
//    }
//
//    private void saveData() { //이 메소드가 shardpreferences에 저장해준다.
//        SharedPreferences sharedPreferences = getSharedPreferences("질문", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(mList);
//        editor.putString("상품목록", json);
//        editor.apply();
//    }
//
//    public void updateData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("질문", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        Bundle extra = getIntent().getExtras();
//        String title = extra.getString("상품명");
//        String price = extra.getString("상품가격");
//
//        EditText 상품명 = findViewById(R.id.B_ET_상품명변경);
//        EditText 상품가격 = findViewById(R.id.B_ET_상품명변경);
//
//        String a = 상품명.getText().toString();
//        String b = 상품가격.getText().toString();
//        editor.putString(title, a);
//        editor.putString(price, b);
//
//        mAdapter.notifyDataSetChanged();
//        editor.apply();
//        saveData();
//
//
//    }
//
//    public void deleteData() {
//        SharedPreferences sharedPreferences = getSharedPreferences("상품", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.clear();
//
//        mAdapter.notifyDataSetChanged();
//        saveData();
//
//
//    }
//
//
//    private void loadData() { //읽어오는 메소드
//        SharedPreferences sharedPreferences = getSharedPreferences("상품", MODE_PRIVATE);
//        String json = sharedPreferences.getString("상품목록", null);
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList<RE_Food>>() {
//        }.getType();
//        mList = gson.fromJson(json, type);
//
//
//        if (mList == null) {
//            mList = new ArrayList<>();
//        }
//    }

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

                    RE_QnA re_qna = new RE_QnA();
                    re_qna.setQnATitle(snapshot.child("QnATitle").getValue().toString());
                    re_qna.setQnAWriting(snapshot.child("QnAWriting").getValue().toString());

                    qnaList.add(re_qna);

                }


                qnaAdapter = new RE_QnAAdapter(mContext, qnaList);
                //connect Adapter with RecyclerView
//                mRecyclerView.setAdapter(qnaAdapter);
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

}
