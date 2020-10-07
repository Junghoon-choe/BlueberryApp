package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class Edit_box extends AppCompatActivity {


    private ArrayList<RE_QnA> mList;
    EditText edit_title, edit_writing;
    Button BT_edit;
    private String 실행 = "실행";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_box);

        edit_title = findViewById(R.id.edit_title);
        edit_writing = findViewById(R.id.edit_writing);
        BT_edit = findViewById(R.id.BT_edit);


        BT_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                Log.v("Intent", 실행);
                //String 과일이름 = edit_title.getText().toString();

                //intent.putExtra("과일이름",과일이름);
                //intent.putExtra(과일이름.getText().toString());

                //가져올때
                //Intent intent = getIntent();
                //String 과일이름 = intent.getExtras().getString("과일이름");


                intent.putExtra("title",edit_title.getText().toString());
                Log.v("putExtra.title", 실행);
                intent.putExtra("writing",edit_writing.getText().toString());
                Log.v("putExtra.writing", 실행);

                //체크박스도 넣을수 있음.

                setResult(RESULT_OK,intent);
                Log.v("RESULT_OK", 실행);
                finish();
                Log.v("finish", 실행);


            }
        });
    }
}
