package com.example.blueberryapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageSwitcher;

public class SplashActivity extends Activity {

    private String 실행 = "실행";
    Handler handler = new Handler();
    ImageSwitcher imageSwitcher;
    boolean aBoolean;
    Thread thread;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);






        Log.v("Splash_onCreate",실행);

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e){
            e.printStackTrace();
        }

        startActivity(new Intent(this, main_page.class));
        finish();
        }




    @Override
    protected void onStart() {
        super.onStart();
        Log.v("Splash_onStart", 실행);

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("Splash_onResume", 실행);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("Splash_onPause", 실행);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("Splash_onStop", 실행);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("Splash_onDestroy", 실행);
    }
}
