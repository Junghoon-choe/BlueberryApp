package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class B_Manager_Page extends AppCompatActivity {

    private Button 상품관리,회원관리,로그아웃;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_manager_page);

        상품관리 = findViewById(R.id.BT_상품관리);
        회원관리 = findViewById(R.id.BT_회원관리);
        로그아웃 = findViewById(R.id.BT_로그아웃);

        상품관리.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(B_Manager_Page.this,B_ItemView.class);
                startActivity(intent);


            }
        });

        회원관리.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        로그아웃.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(B_Manager_Page.this, main_page.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
