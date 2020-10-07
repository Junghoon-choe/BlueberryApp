package com.example.blueberryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class A_qna_writing_page extends AppCompatActivity {

    private String userName = MyApplication.회원ID;
//    private FirebaseDatabase database;
//    private DatabaseReference reference;

    private FirebaseFirestore DB = FirebaseFirestore.getInstance();
    private CollectionReference foodStoreCRef = DB.collection("QnA");


    private TextInputLayout ET_Title, ET_Writing;

    private Button 확인;

    private String 실행 = "실행";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_qna_writing_page);



        확인 = findViewById(R.id.BT_QnA_확인);
        ET_Title = findViewById(R.id.QnA_Title);
        ET_Writing = findViewById(R.id.QnA_Writing);

//        // getEditText() 메소드로 간편하게 불러올 수 있음
//        EditText editText_Title = ET_Title.getEditText();
//        EditText editText_Writing = ET_Writing.getEditText();
//
//        final TextWatcher textWatcher = new TextWatcher() {
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//                // 입력난에 변화가 있을 시 조치
//            }
//
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                // 입력이 끝났을 때 조치
//            }
//
//            public void afterTextChanged(Editable s) {
//                // 입력하기 전에 조치
//            }
//        };
//
//        assert editText_Title != null;
//        assert editText_Writing != null;
//
//        editText_Title.addTextChangedListener(textWatcher);
//        editText_Writing.addTextChangedListener(textWatcher);

//        확인.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                database = FirebaseDatabase.getInstance();//파이어 베이스에 있는 데이터 베이스와 연결함.
//                reference = database.getReference("QnA");//참조하는건 어디냐면 Goods란 이름의 데이터 베이스를 참조한다.
//
//                //get all 변수
//                String UserID = userName;
//                String Title = Objects.requireNonNull(ET_Title.getEditText()).getText().toString();
//                String Writing = Objects.requireNonNull(ET_Writing.getEditText()).getText().toString();
//
//                RE_QnA QnAAdder = new RE_QnA(UserID,Title, Writing);
//
//                reference.child(UserID+Title).setValue(QnAAdder); //참조되는 부분에 사용자가 입력하는 값을 참조이름으로 적는다. ID값으로 갖는 키값에 벨류로 유저헬퍼의 정보를 넣는다.
//
//                finish();
//                Log.v("finish", 실행);
//
//
//            }
//        });
    }

    public void addUser(View view) {

        // getEditText() 메소드로 간편하게 불러올 수 있음
        EditText editText_Title = ET_Title.getEditText();
        EditText editText_Writing = ET_Writing.getEditText();

        final TextWatcher textWatcher = new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력난에 변화가 있을 시 조치
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력이 끝났을 때 조치
            }

            public void afterTextChanged(Editable s) {
                // 입력하기 전에 조치
            }
        };

        assert editText_Title != null;
        assert editText_Writing != null;

        editText_Title.addTextChangedListener(textWatcher);
        editText_Writing.addTextChangedListener(textWatcher);

//        String ID = ET_ID.getText().toString();
//        String PW = ET_Password.getText().toString();
//        String name = ET_Name.getText().toString();
//        String Email = ET_Email.getText().toString();
//        String PhoneNum = ET_PhoneNum.getText().toString();



//        RE_QnA re_qnA= new RE_QnA(,editText_Title, editText_Writing);
//
//        foodStoreCRef.document(ID).set(userHelper)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "Document has been saved!");
//                        Toast.makeText(signup_page2.this, "User saved!", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Document was not saved!", e);
//                Toast.makeText(signup_page2.this, "Error!", Toast.LENGTH_SHORT).show();
//            }
//        });
//
////        ItemAdder itemAdder = new ItemAdder(null, foodNameText, foodPriceText);
////        reference.child("Items").setValue(itemAdder); //참조되는 부분에 사용자가 입력하는 값을 참조이름으로 적는다. ID값으로 갖는 키값에 벨류로 유저헬퍼의 정보를 넣는다.
//        Intent intent = new Intent(signup_page2.this, signup_page3.class);
//        startActivity(intent);
//        finish();
//
    }

}
