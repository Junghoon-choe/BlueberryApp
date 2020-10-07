package com.example.blueberryapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class A_ReviewClick extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_a_reviewclick);

        String id = "";
        String title = "";
        String writing = "";

        Bundle extras = getIntent().getExtras();


        id = extras.getString("id");
        title = extras.getString("title");
        writing = extras.getString("writing");

        TextView textView = findViewById(R.id.TextResult);

        String str = id;
        String str1 = title;
        String str2 = writing;
        textView.setText(str);
        textView.setText(str1);
        textView.setText(str2);



    }
}
