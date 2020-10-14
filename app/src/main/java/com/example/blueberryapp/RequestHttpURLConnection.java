package com.example.blueberryapp;

import android.content.ContentValues;
import android.util.Log;

import com.kakao.auth.authorization.authcode.AuthorizationCode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import retrofit2.http.POST;

public class RequestHttpURLConnection {
    private static final Object POST = "https://kapi.kakao.com/v1/payment/ready";

    public String request(String _url, ContentValues _params){

        // HttpURLConnection 참조 변수.
//        String url = "http://kapi.kakao.com/v1/payment/ready";
        HttpURLConnection urlConn = null;
        // URL 뒤에 붙여서 보낼 파라미터.
        StringBuffer sbParams = new StringBuffer();



//        curl -v -X POST 'https://kapi.kakao.com/v1/payment/ready' \
//        -H 'Authorization: KakaoAK {APP_ADMIN_KEY}' \
//        --data-urlencode 'cid=TC0ONETIME' \
//        --data-urlencode 'partner_order_id=partner_order_id' \
//        --data-urlencode 'partner_user_id=partner_user_id' \
//        --data-urlencode 'item_name=초코파이' \
//        --data-urlencode 'quantity=1' \
//        --data-urlencode 'total_amount=2200' \
//        --data-urlencode 'vat_amount=200' \
//        --data-urlencode 'tax_free_amount=0' \
//        --data-urlencode 'approval_url=https://developers.kakao.com/success' \
//        --data-urlencode 'fail_url=https://developers.kakao.com/fail' \
//        --data-urlencode 'cancel_url=https://developers.kakao.com/cancel'


//        urlConn.setRequestProperty("cid","TC0ONETIME");
//        urlConn.setRequestProperty("KakaoAK","2802b7d197aab96f25f00e117dd465d0");
//        urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=utf-8");
//        sbParams.append("");
//        sbParams.append("");
//        sbParams.append("");
//        sbParams.append("");
//        sbParams.append("");
//        sbParams.append("");


        /**
         * 1. StringBuffer에 파라미터 연결
         * */
        // 보낼 데이터가 없으면 파라미터를 비운다.
        if (_params == null)
            sbParams.append("");
            // 보낼 데이터가 있으면 파라미터를 채운다.
        else {
            // 파라미터가 2개 이상이면 파라미터 연결에 &가 필요하므로 스위칭할 변수 생성.
            boolean isAnd = false;
            // 파라미터 키와 값.
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : _params.valueSet()){
                key = parameter.getKey();
                value = parameter.getValue().toString();

                // 파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                if (isAnd)
                    sbParams.append("&");

                sbParams.append(key).append("=").append(value);

                // 파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if (!isAnd)
                    if (_params.size() >= 2)
                        isAnd = true;
            }
        }

        /**
         * 2. HttpURLConnection을 통해 web의 데이터를 가져온다.
         *
         * */
        try{
            //요청하는 부분
//            URL url = new URL("http://www.naver.com");
            URL url = new URL("http://kapi.kakao.com/v1/payment/ready");
            urlConn = (HttpURLConnection) url.openConnection();

            Log.d("LOG", "응답코드 : " + urlConn.getResponseCode());
            Log.d("LOG", "응답메세지 : " + urlConn.getResponseMessage());

            // [2-1]. urlConn 설정.
            urlConn.setRequestMethod("POST"); // URL 요청에 대한 메소드 설정 : POST.

            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.
            urlConn.setRequestProperty("KakaoAK","2802b7d197aab96f25f00e117dd465d0");
            urlConn.setRequestProperty("Context_Type", "application/x-www-form-urlencoded;charset=utf-8");

//            HTTP/1.1 200 OK
//            Content-type: application/json;charset=UTF-8
//            {
//                "tid": "T1234567890123456789",
//                    "next_redirect_app_url": "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/aInfo",
//                    "next_redirect_mobile_url": "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/mInfo",
//                    "next_redirect_pc_url": "https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/info",
//                    "android_app_scheme": "kakaotalk://kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order",
//                    "ios_app_scheme": "kakaotalk://kakaopay/pg?url=https://mockup-pg-web.kakao.com/v1/xxxxxxxxxx/order",
//                    "created_at": "2016-11-15T21:18:22"
//            }


            // [2-2]. parameter 전달 및 데이터 읽어오기.
            String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장. 예)id=id1&pw=123;
            OutputStream os = urlConn.getOutputStream();
            os.write(strParams.getBytes("UTF-8")); // 출력 스트림에 출력.
            os.flush(); // 출력 스트림을 플러시(비운다)하고 버퍼링 된 모든 출력 바이트를 강제 실행.
            os.close(); // 출력 스트림을 닫고 모든 시스템 자원을 해제.

            // [2-3]. 연결 요청 확인.
            // 실패 시 null을 리턴하고 메서드를 종료.
            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            // [2-4]. 읽어온 결과물 리턴.
            // 요청한 URL의 출력물을 BufferedReader로 받는다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(), "UTF-8"));

            // 출력물의 라인과 그 합에 대한 변수.
            String line;
            String page = "";

            // 라인을 받아와 합친다.
            while ((line = reader.readLine()) != null){
                page += line;
            }

            return page;

        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }

        return null;

    }

}
