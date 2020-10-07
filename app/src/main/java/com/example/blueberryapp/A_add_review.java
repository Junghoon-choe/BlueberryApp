package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class A_add_review extends AppCompatActivity {



    EditText ET_title, ET_writing;
    Button enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_add_review);

        enter = findViewById(R.id.BT_확인);
        ET_title = findViewById(R.id.ET_제목);
        ET_writing = findViewById(R.id.ET_후기글);


        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent();

                        intent.putExtra("ReViewTitle", ET_title.getText().toString());
                        intent.putExtra("ReViewWriting", ET_writing.getText().toString());


                        //체크박스도 넣을수 있음.

                        setResult(RESULT_OK, intent);
                        finish();

                    }
                };
            }
        });
    }
}
