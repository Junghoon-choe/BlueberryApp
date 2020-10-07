package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.widget.TextView;

public class WebView extends AppCompatActivity {


    private android.webkit.WebView daum_Webview;
    private TextView daum_Result;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        daum_Result =findViewById(R.id.daum_Result);
        주소API();
        handler = new Handler();



    }




    public void 주소API() {

        //webView설정
        daum_Webview = findViewById(R.id.daum_Webview);

        // JavaSctipt 허용
        daum_Webview.getSettings().setJavaScriptEnabled(true);


        // JavaScript의 window.open허용
        daum_Webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // JavaScript이벤트에 대응할 함수를 정의한 클래스를 붙여줌
        daum_Webview.addJavascriptInterface(new AndroidBridge(), "BlueBarryApp");


        // web client를 chrome으로 설정.
        daum_Webview.setWebChromeClient(new WebChromeClient());


        // webview uri load php 파일주소
        daum_Webview.loadUrl("https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js");


    }

    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    daum_Result.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                    주소API();

                    Intent intent = new Intent();
                    intent.putExtra("address",daum_Result.getText().toString());
                    setResult(RESULT_OK,intent);
                    finish();



                }
            });

        }
    }

}

