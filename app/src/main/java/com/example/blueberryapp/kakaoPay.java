package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.type.DateTime;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;


public class kakaoPay extends AppCompatActivity {

    private TextView tv_outPut;
    public WebView mWebView;
    private WebSettings webSettings;
    private long time = 0;

    private String url;
    private JsonPlaceHolderApi jsonPlaceHolderApi;
    private char aChar;

    private String tid, app_url, mobile_url, pc_url, android_scheme, ios_scheme, approvalUrl;
    private String createdTime;
    private String test;
    private String data;

//    private String a,b,c,d,e,f;
//    private DateTime g;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakao_pay);

//        tv_outPut = findViewById(R.id.tv_outPut);


        //카카오페이 웹뷰
        mWebView = findViewById(R.id.mWebView);
        mWebView.setWebViewClient(new WebViewClient()); // 클릭시 새창 안뜨게
        webSettings = mWebView.getSettings(); //세부 세팅 등록
        webSettings.setJavaScriptEnabled(true); // 웹페이지 자바스클비트 허용 여부
        webSettings.setSupportMultipleWindows(false); // 새창 띄우기 허용 여부
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); // 자바스크립트 새창 띄우기(멀티뷰) 허용 여부
        webSettings.setLoadWithOverviewMode(true); // 메타태그 허용 여부
        webSettings.setUseWideViewPort(true); // 화면 사이즈 맞추기 허용 여부
        webSettings.setSupportZoom(false); // 화면 줌 허용 여부
        webSettings.setBuiltInZoomControls(false); // 화면 확대 축소 허용 여부
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN); // 컨텐츠 사이즈 맞추기
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE); // 브라우저 캐시 허용 여부
        webSettings.setDomStorageEnabled(true); // 로컬저장소 허용 여부


//        url = "https://mockup-pg-web.kakao.com/v1/" + "입력값";


        //TODO : 로그찍은걸 어디서 찍었나 확인하고, 로그 찍을걸 맵형식으로 가져오면 된다고 하셨다. 그럼 먼저
        //로그가 어디서 찍혔는지 어떻게 찍혔는지 알아보자.

        //PATH를 위한 Gson생성.
        Gson gson = new GsonBuilder().serializeNulls().create();


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        OkHttpClient okHttpClient = null;

        okHttpClient = new OkHttpClient.Builder()

                //나만의 인터셉터 메서드추가.

                .addInterceptor(new Interceptor() {


                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request originalRequest = chain.request();

                        Request newRequest = originalRequest.newBuilder()
                                .header("Authorization", "KakaoAK" + " 2802b7d197aab96f25f00e117dd465d0")
                                .build();
                        //. 서버로부터 응답받기 : chain의 process 메서드를 이용하면
                        // 서버로 통신을 하고 응답을 받아올 수 있게 됩니다.
                        // return 값으로 받은 response 객체가 바로 그 응답을 의미하는 객체입니다.
                        return chain.proceed(newRequest); //그 응답을 받아올 수 있는 곳이다.
                    }

                })
                .addInterceptor(loggingInterceptor)
                .build();

//        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY).intercept("tid");


        //레트로릿 라이브러리를 생성해서 URL을 지정하고. 지정된 주소에 Gson컨버터 즉 변환기를 추가로 설정한다.
        // 이후에 빌드함.
        //아래와 같은 방식으로 v3/버전3을 넣으면 뒤에 항상 슬레쉬를 붙여줘야 한다.
        // .baseUrl("http://jsonplaceholder.typicode.com/v3/")
        //                .baseUrl("https://blueberryapp.com/")
        //PATCH를 사용할떄 위의 구문에서 생성했듯이 gson변수를 create파라미터 안에 넣는다.
        // 넣으면 PATCH를 했을때 나왔던 "nesciunt quas odio"문장이 아니라 null값이 나온다.
        //TODO: 어떤 이유로 Gson을 넣으면 이상했던 문장이 null로 바뀌는지 알아보기.
        Retrofit retrofit = new Retrofit.Builder()
                //아래와 같은 방식으로 v3/버전3을 넣으면 뒤에 항상 슬레쉬를 붙여줘야 한다.
                // .baseUrl("http://jsonplaceholder.typicode.com/v3/")
                .baseUrl("https://kapi.kakao.com/")
//                .baseUrl("https://blueberryapp.com/")
                //PATCH를 사용할떄 위의 구문에서 생성했듯이 gson변수를 create파라미터 안에 넣는다.
                // 넣으면 PATCH를 했을때 나왔던 "nesciunt quas odio"문장이 아니라 null값이 나온다.
                //TODO: 어떤 이유로 Gson을 넣으면 이상했던 문장이 null로 바뀌는지 알아보기.

                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();


        //TODO : 해당 로그의 변수들이 어디서 찍히는지 알면, 쉽게 구현 가능하다.
        //TODO : 해당 로그가 어디서 찍히는지 알아보기.
        // retrofit에서 찍힐수도 있다, 이유는 클라이언트 메서드에 인터셉터가 포함된 okhttp클라이언트를 넣기 떄문이다.
        //그렇다는 것은 okhttp에서도 로그가 찍힐수 있다는 것이다.
        //그렇다면, 먼저 okhttp에서 로그가 어디 찍힐지를 알아봐야겠다.


        //주소를 참조하는 인터페이스를 선언하고, 위에 선언된 레트로 생성자에 (연동)넣어서 불러 올수 있게 지정한다.
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);


        // 해당 URL에 있는 모든 정보 가져오는 함수
//        getPosts();

        // 해당 URL에 코멘트에 해당하는 정보만 가져오는 함수
//        getComments();

        // 지정된 URL에 임의값을 Sending Post한다.
//        createPost();

        // 지정된 URL에 값을 업데이트한다.
//        updataPost();

        // 지정된 URL에 임의 값을 지운다.
//        deletePost();

        //카카오페이 Post 넣어서 가져오기.

        kakaoPost();


//        kakaoGetResponse();

        //이 부분에 해당 아이템을 가져온 url을 넣어야 한다. 흠...

//        approvalUrl = "https://blueberryapp.com/success";


//        Intent intent = null;
//        try {
//            intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//        Intent existPackage = getPackageManager().getLaunchIntentForPackage(intent.getPackage());
//        if (existPackage != null) {
//            startActivity(intent);
//        }


        mWebView.loadUrl("https://mockup-pg-web.kakao.com/v1/");

    }


    private void kakaoPost() {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("cid", "TC0ONETIME");
        parameters.put("partner_order_id", "partner_order_id");
        parameters.put("partner_user_id", "New partner_user_id");
        parameters.put("item_name", "초코파이");
        parameters.put("quantity", "1");
        parameters.put("total_amount", "2200");
        parameters.put("tax_free_amount", "0");
        parameters.put("approval_url", "https://blueberryapp.com/success");
        parameters.put("fail_url", "https://blueberryapp.com/fail");
        parameters.put("cancel_url", "https://blueberryapp.com/cancel");

        Call<kakaoPost> call = jsonPlaceHolderApi.kakaoPost(parameters);


        call.enqueue(new Callback<kakaoPost>() {
            @Override
            public void onResponse(Call<kakaoPost> call, Response<kakaoPost> response) {
                if (!response.isSuccessful()) {
                    return;
                }

                kakaoPost kakaoResponse = response.body().string;


                kakaoResponse.getTid();
                Log.d("respons Body", "내가 원하는 코드 : " + kakaoResponse.getTid());

                //통신이 성공하였을시,,
                //참고 :  OkHttp의 Interceptor를 이용해서
                // Retrofit을 이용한 서버 요청과 응답을 중간에서 가로채
                // 원하는 값을 끼워서 요청하거나 응답을 원하는 형태로 조금 변형할 수 있습니다.
                // 응답의 body를 JSON 문자열로 만들기
//                Log.d("respons Body", "내가 원하는 코드 : " + response.body());
//                Log.d("respons ErrorBody", "에러 코드" + jsonPlaceHolderApi.kakaoGet(tid,app_url,mobile_url,pc_url,android_scheme,ios_scheme,createdTime));
//                jsonPlaceHolderApi.kakaoGet(tid,app_url,mobile_url,pc_url,android_scheme,ios_scheme,createdTime).enqueue(new Callback<List<kakaoGet>>() {
//                    @Override
//                    public void onResponse(Call<List<kakaoGet>> call, Response<List<kakaoGet>> response) {
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<List<kakaoGet>> call, Throwable t) {
//
//                    }
//                });


                //상위 목록에서 데이터를 가져올수 있게 만들고, 그 데이터들을 map형식으로 바꿔서 사용할 수 있다.
                //결국은 여기서 데이트를 요청 받아올 수 있다.

//                if (response.isSuccessful) {
//                    if (response.body()?.success == true) {
//                        if (response.body()?.data != null) {
//                            Timber.e("우리가 원하는 데이터 : ${response.body()!!.data!!}")
//                        } else {
//                            Timber.e("네트워크 요청 실패! 2") // 4
//                        }
//                    } else {
//                        Timber.e("네트워크 요청 실패! 3") // 3
//                    }
//                } else {
//                    Timber.e("네트워크 요청 실패! 4") // 2
//                }

                test = response.body().toString();
                Log.d("Successful ", "approvalUrl : " + response.body().toString());
                kakaoPost body = response.body();
                url = String.valueOf(call.request().url());


                Log.d("Successful ", "approvalUrl : " + url);


            }

            @Override
            public void onFailure(Call<kakaoPost> call, Throwable t) {
                Log.d("Code is Failure", ">>" + t.getMessage());
            }
        });
    }

    private void getTime(String url) {
        OkHttpClient okHttpClient = new OkHttpClient();

        okHttpClient.newCall(new Request.Builder().url(url).build()).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull final okhttp3.Response response) throws IOException {

                new Handler(getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject jsonObject = new JSONObject(response.body().string());

                            Log.d("tid", "테스트 :" + jsonObject.getString("tid"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        });
    }

//    private void kakaoGetResponse() {
//        Map<String, String> parameters = new HashMap<>();
//        parameters.get("tid");
//        parameters.get("next_redirect_app_url");
//        parameters.get("next_redirect_mobile_url");
//        parameters.get("next_redirect_pc_url");
//        parameters.get("android_app_scheme");
//        parameters.get("ios_app_scheme");
//        parameters.get("created_at");
//
//        Call<List<kakaoGet>> call = jsonPlaceHolderApi.kakaoGet(parameters);
//
//        call.enqueue(new Callback<List<kakaoGet>>() {
//            @Override
//            public void onResponse(Call<List<kakaoGet>> call, Response<List<kakaoGet>> response) {
//
//            }
//
//            @Override
//            public void onFailure(Call<List<kakaoGet>> call, Throwable t) {
//
//            }
//        });
//    }


    private void deletePost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                tv_outPut.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                tv_outPut.setText(t.getMessage());
            }
        });
    }

    private void updataPost() {
        Post post = new Post(12, null, "New Text");

        //아래 Call부분의 풋 패치 메서드만 바꿔주면 된다.
        //6. 해더추가
//        Call<Post> call = jsonPlaceHolderApi.putPost("abc",5,post);


        //6. 헤더 맵 형식으로 추가.
        Map<String, String> headers = new HashMap<>();
        headers.put("Map-Header1", "def");
        headers.put("Map-Header2", "ghi");

        //patch를 하고 title에 null값을 넣으면 아래와 같은 임의 메세지를 받는다.
        //"nesciunt quas odio"
        //위에 Gson을 추가한다 결과 : 위의 문장 대신 null 메세지가 입력된다.
        Call<Post> call = jsonPlaceHolderApi.patchPost(headers, 5, post);


        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                Post postResponse = response.body();

                String content = "";

                //Code를 적는 이유는 응답하는 코드를 보기 위해서이다.
                content += "Code: " + response.code() + "\n";

                //101이 입력되는데 아무것도 넣어주지 않았을 경우, 임의 값으로 101을 넣어준다.
                content += "ID : " + postResponse.getId() + "\n";
                content += "User ID : " + postResponse.getUserId() + "\n";
                content += "Title : " + postResponse.getTitle() + "\n";
                content += "Text : " + postResponse.getText() + "\n";

                tv_outPut.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {

            }
        });
    }

    private void createPost() {
        //벨류를 바로 입력하기 위해서 지워준다.
        Post post = new Post(23, "New Title", "New Text");

        //맵을 사용하여 구현할떄 사용
        //이후 파라미터에 키 벨류가 포함된 맵을 넣어주면 구현 됨.
        Map<String, String> fields = new HashMap<>();
        fields.put("userId", "25");
        fields.put("title", "New Title");


        //이부분이 Sending Post Body부분이다.
        //위에 입력된 값을 Call함수로 보내준다.
        Call<Post> call = jsonPlaceHolderApi.createPost(fields);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    tv_outPut.setText("Code : " + response.code());
                    return;
                }

                Post postResponse = response.body();

                String content = "";

                //Code를 적는 이유는 응답하는 코드를 보기 위해서이다.
                content += "Code: " + response.code() + "\n";

                //101이 입력되는데 아무것도 넣어주지 않았을 경우, 임의 값으로 101을 넣어준다.
                content += "ID : " + postResponse.getId() + "\n";
                content += "User ID : " + postResponse.getUserId() + "\n";
                content += "Title : " + postResponse.getTitle() + "\n";
                content += "Text : " + postResponse.getText() + "\n";

                tv_outPut.setText(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                tv_outPut.setText(t.getMessage());
            }
        });


    }

    private void getComments() {
        //getComments 뒤의 파라메터값 안에 입력할 Id 값을 넣으면 된다. 예시로 3을 넣어보겠다.
//        Call<List<PostComment>> call = jsonPlaceHolderApi.getComments(3);

        //위와 같이 하면 직접 주소를 적어서 해당 주소를 불러올수 있다.
        //posts/3/comments의 주소로 이동할 것이다.
        //아래 정보를 수정하면 조금더 편하게 사용할 수 있다.
//        Call<List<PostComment>> call = jsonPlaceHolderApi.getComments("posts/3/comments");

        //전체URL을 불러 올 수있다.
        Call<List<PostComment>> call = jsonPlaceHolderApi.getComments("http://jsonplaceholder.typicode.com/posts/3/comments");

        call.enqueue(new Callback<List<PostComment>>() {
            @Override
            public void onResponse(Call<List<PostComment>> call, Response<List<PostComment>> response) {
                if (!response.isSuccessful()) {
                    tv_outPut.setText("Code : " + response.code());
                    return;
                }
                List<PostComment> postComments = response.body();

                for (PostComment postComment : postComments) {
                    String content = "";
                    content += "ID :" + postComment.getId() + "\n";
                    content += "Post ID :" + postComment.getPostId() + "\n";
                    content += "Name :" + postComment.getName() + "\n";
                    content += "Email :" + postComment.getEmail() + "\n";
                    content += "Text :" + postComment.getText() + "\n";

                    tv_outPut.append(content);
                }
            }


            @Override
            public void onFailure(Call<List<PostComment>> call, Throwable t) {
                tv_outPut.setText(t.getMessage());
            }
        });
    }

    private void getPosts() {

        //인터페이스의 맵을 사용하기 위해서 작성한 구문이다.
        //아래와 같이 작성하면
        Map<String, String> parameters = new HashMap<>();
        parameters.put("userId", "1");
        parameters.put("_sort", "id");
        parameters.put("_order", "desc");

        //인터페이스에 있는 call을 선언해서 jsonplace안에 getPosts를 불러온다.
        //아래와 같이 선언하면 파라메터값이 jsonPlaceHolderApi 인터페이스로 이동하고 그 안에서 값을 넣어주어서
        //결과 적으로 comments?userId=4&_sort=id&_order=desc 와 같은 결과물이 나타나게 된다.
        //sort 와 order값에 null을 넣어서 설정이 안되게 설정할 수 있다.
        //Integer을 새로 생성해서 그 안에 검색할 아이디를 갯수 많큼 넣는다.(출력은 아래에서 부터 출력된다.)
//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(new Integer[]{2,5,10},"id","desc");

        //Map을 사용하여 간소화 하였다 맵에서 입력된 값을 파라메터값에 parameters(맵)를 넣어서 구현하면 위와같은 방식으로 구현이 가능하다.
        //즉 파라메터 값에 들어간 값으로
        //Map<String, String> parameters = new HashMap<>();
        //        parameters.put("userId","1");
        //        parameters.put("_sort","id");
        //        parameters.put("_order","desc");
        //파라메터 값안에 넣어서 불러오는 식이다.
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);


        //리스트안에 있는 포스트 를 response 하는 execute메서드 말고,
        // enqueue메서드를 선언한다. enqueue메서드는 콜벡 메서드임.
        call.enqueue(new Callback<List<Post>>() {

            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                //만약 리스폰이 성공적이지 못하면 아래 코드를 불러 올수 있게 설정한다. (에러방지용)
                if (!response.isSuccessful()) {
                    tv_outPut.setText("Code : " + response.code());
                    return;
                }
                //인터페이스에 있는 포스트에 페이지의 바디를 넣는다.
                List<Post> posts = response.body();

                //for문으로 돌면서 포스트의 전부를 불러온다.
                for (Post post : posts) {
                    //선언될 문자들을 String으로 지정한다.
                    String content = "";
                    content += "ID : " + post.getId() + "\n";
                    content += "User ID : " + post.getUserId() + "\n";
                    content += "Title : " + post.getTitle() + "\n";
                    content += "Text : " + post.getText() + "\n";

                    //append 메서드는 위에 선언된 모든 원소를 추가한다는 뜻이다.
                    tv_outPut.append(content);
                }
            }

            //페이지 불러오기가 실패하면:404 그냥 텍스트를 불러서 추가한다.
            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                tv_outPut.setText(t.getMessage());
            }
        });
    }

    //TODO : 로그를 찍어보고 어디서 오류가 발생하는지 정확하게 알아볼것.... 후,,,


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        Intent intent = getIntent();
//        if ( intent != null ) {
//            Uri intentData = intent.getData();
//
//            if ( intentData != null ) {
//                //카카오페이 인증 후 복귀했을 때 결제 후속조치
//                String url = intentData.toString();
//
//                if ( url.startsWith(APP_SCHEME) ) {
//                    String path = url.substring(APP_SCHEME.length());
//                    if ( "process".equalsIgnoreCase(path) ) {
//                        mainWebView.loadUrl("javascript:IMP.communicate({result:'process'})");
//                    } else {
//                        mainWebView.loadUrl("javascript:IMP.communicate({result:'cancel'})");
//                    }
//                }
//            }
//        }
//
//    }

}
