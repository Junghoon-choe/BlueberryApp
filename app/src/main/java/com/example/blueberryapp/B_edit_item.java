package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

public class B_edit_item extends AppCompatActivity {

    Button 사진변경, 확인;
    ImageView 상품사진;
    EditText 상품명입력, 상품가격입력;



//        EditText edit_title, edit_writing;
//        Button BT_edit;

    private String 실행 = "실행";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_add_item);

        상품사진 = findViewById(R.id.IV_상품사진);
        확인 = findViewById(R.id.BT_확인);
        사진변경 = findViewById(R.id.BT_사진변경);
        상품명입력 = findViewById(R.id.ET_상품명입력);
        상품가격입력 = findViewById(R.id.ET_상품가격입력);



        사진변경.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(B_edit_item.this,B_AddItem.class);
                startActivity(intent);
            }
        });


        확인.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
