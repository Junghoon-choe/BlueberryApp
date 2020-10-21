package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class A_review_page_clicked extends AppCompatActivity {

    TextView TV_title, TV_writing, TV_userName;
    ImageView IV_image;
    Button BT_댓글추가;
    EditText ET_댓글;
    String 다큐제목;

    String Image;
    String title;
    String writing;
    String userName;

    RecyclerView RC_Review;

    // Variables
    private ArrayList<Reply> List;
    private Reply_Adapter replyAdapter;
    private Context mContext;


    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference ReviewCRef = DB.collection("REVIEW");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_review_page_clicked);

        IV_image = findViewById(R.id.IV_imageReview);
        TV_title = findViewById(R.id.TV_titleReview);
        TV_writing = findViewById(R.id.TV_writingReview);
        TV_userName = findViewById(R.id.TV_userNameReview);
        BT_댓글추가 = findViewById(R.id.BT_댓글추가);
        ET_댓글 = findViewById(R.id.ET_댓글);
        RC_Review = findViewById(R.id.RC_Review);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        RC_Review.setLayoutManager(layoutManager);
        RC_Review.setHasFixedSize(true);

        // 부가 기능
        DividerItemDecoration FooddividerItemDecoration = new DividerItemDecoration(RC_Review.getContext(),
                layoutManager.getOrientation());
        RC_Review.addItemDecoration(FooddividerItemDecoration);

        BT_댓글추가.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ET_댓글.getText().toString() == null) {
                    Toast.makeText(A_review_page_clicked.this, "댓글을 작성해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    댓글추가();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Intent intent = getIntent();
        다큐제목 = intent.getExtras().getString("다큐제목");
        Log.d("@@#!@#!@#$!@", "ReplyDocuName > " + 다큐제목);
        Image = intent.getExtras().getString("Image");
        title = intent.getExtras().getString("Title");
        writing = intent.getExtras().getString("Writing");
        userName = intent.getExtras().getString("UserName");

        MyApplication.ReplyDocuName = 다큐제목;


        if (!(Image == null)) {
            IV_image.setVisibility(View.VISIBLE);
        }
        Picasso.with(this).load(Image).into(IV_image);
        TV_title.setText(title);
        TV_writing.setText(writing);
        TV_userName.setText(userName);

        // ArrayList
        List = new ArrayList<>();

        //Clear ArrayList
        ClearAll();

        // Get Data Method
        getDataFromFireStore();

    }

    private void ClearAll() {
        if (List != null) {
            List.clear();
            if (replyAdapter != null) {
                replyAdapter.notifyDataSetChanged();
            }
        }

        List = new ArrayList<>();
    }

    private void getDataFromFireStore() {

        replyAdapter = new Reply_Adapter(A_review_page_clicked.this, List);
        RC_Review.setAdapter(replyAdapter);


        ReviewCRef.document(다큐제목).collection("댓글").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List.clear();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Reply reply = documentSnapshot.toObject(Reply.class);
                    List.add(reply);
                }


                replyAdapter.notifyDataSetChanged();

            }

        });
    }

    private void 댓글추가() {

        String 댓글 = ET_댓글.getText().toString();
        if (댓글.isEmpty()){
            Toast.makeText(A_review_page_clicked.this,"댓글을 적어주세요.",Toast.LENGTH_SHORT).show();
            return;
        }

            String 작성자 = MyApplication.회원Name;
        String 현재시간 = String.valueOf(System.currentTimeMillis());
        String 작성자이메일 = MyApplication.회원Email;


        String 댓글다큐제목 = 현재시간 + 작성자 + 댓글;

        Toast.makeText(A_review_page_clicked.this, "업로드 성공", Toast.LENGTH_SHORT).show();

        Reply reply = new Reply(댓글, 작성자, 현재시간, 댓글다큐제목, 작성자이메일);

        ReviewCRef.document(다큐제목).collection("댓글").document(댓글다큐제목).set(reply);

        startActivity(new Intent(A_review_page_clicked.this, A_review_page.class));
        finish();
    }

}
