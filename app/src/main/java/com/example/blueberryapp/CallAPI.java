package com.example.blueberryapp;

import android.os.AsyncTask;

import com.google.api.HttpRule;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.google.api.HttpRule.PatternCase.POST;

public class CallAPI extends AsyncTask<String, String, String> {

    public CallAPI(){
        //set context variables if required
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected String doInBackground(String... params) {
        String urlString = params[0]; // URL to call
        String data = params[1]; //data to post
        OutputStream out = null;


        try {

//            POST /v1/payment/ready HTTP/1.1
//            Host: kapi.kakao.com
//            Authorization: KakaoAK 2802b7d197aab96f25f00e117dd465d0;
//            Content-type: application/x-www-form-urlencoded;charset=utf-8

            URL url = new URL(urlString);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            out = new BufferedOutputStream(urlConnection.getOutputStream());

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(data);
            writer.flush();
            writer.close();
            out.close();

            urlConnection.connect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return urlString;
    }
}
