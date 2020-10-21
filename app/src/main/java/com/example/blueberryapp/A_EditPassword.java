package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.View;

public class A_EditPassword extends AppCompatActivity {


    TextInputLayout password, editPassword;

    String PASSWORD, ID;

    private Button BT_수정확인;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_editpassword);

        password = findViewById(R.id.현재비밀번호);
        editPassword = findViewById(R.id.수정비밀번호);

        reference = FirebaseDatabase.getInstance().getReference("Users"); // instance는 데이터베이스 이름을 말한다.

        MyApplication.회원ID = ID;
        MyApplication.회원PW = PASSWORD;


        BT_수정확인.setClickable(true);
        BT_수정확인.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

            }
        });


    }

    public void upData(View view) {

        Toast.makeText(this, "정보가 바뀌었습니다.", Toast.LENGTH_LONG).show();

    }


}
