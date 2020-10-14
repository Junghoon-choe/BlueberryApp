package com.example.blueberryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Comment;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class kakaoPay extends AppCompatActivity {

    private TextView tv_outPut;

    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_kakao_pay);

        // 위젯에 대한 참조.
        tv_outPut = (TextView) findViewById(R.id.tv_outPut);

        //PATH를 위한 Gson생성.
        Gson gson = new GsonBuilder().serializeNulls().create();

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //나만의 인터셉터 메서드추가.
//                .addInterceptor(new Interceptor() {
//                    @NotNull
//                    @Override
//                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
//                        Request originalRequest = chain.request();
//
//                        Request newRequest = originalRequest.newBuilder()
//                                .header("Interceptor-Header","xyz")
//                                .build();
//                        return chain.proceed(newRequest);
//                    }
//                })
                //
                .addInterceptor(loggingInterceptor)
                .build();

        //레트로릿 라이브러리를 생성해서 URL을 지정하고. 지정된 주소에 Gson컨버터 즉 변환기를 추가로 설정한다.
        // 이후에 빌드함.
        Retrofit retrofit = new Retrofit.Builder()
                //아래와 같은 방식으로 v3/버전3을 넣으면 뒤에 항상 슬레쉬를 붙여줘야 한다.
                // .baseUrl("http://jsonplaceholder.typicode.com/v3/")
                .baseUrl("http://kapi.kakao.com/")
                //PATCH를 사용할떄 위의 구문에서 생성했듯이 gson변수를 create파라미터 안에 넣는다.
                // 넣으면 PATCH를 했을때 나왔던 "nesciunt quas odio"문장이 아니라 null값이 나온다.
                //TODO: 어떤 이유로 Gson을 넣으면 이상했던 문장이 null로 바뀌는지 알아보기.
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

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


//        ---- 참고 ----
//        // URL 설정.
//        String url = "http://kapi.kakao.com/v1/payment/ready";
//
//        // AsyncTask를 통해 HttpURLConnection 수행.
//        NetworkTask networkTask = new NetworkTask(url, null);
//        networkTask.execute();

    }

    private void kakaoPost() {

        kakaoPost kakaoPost = new kakaoPost("TC0ONETIME",
                "partner_order_id",
                "New partner_user_id",
                "초코파이",
                1,
                2200,
                200,
                0,
                "https://developers.kakao.com/success",
                "https://developers.kakao.com/fail",
                "https://developers.kakao.com/cancel");

        //6. 헤더 맵 형식으로 추가.
        Map<String, String> kakaoHeaders = new HashMap<>();
        kakaoHeaders.put("Authorization", "KakaoAK 2802b7d197aab96f25f00e117dd465d0");



        Call<kakaoPost> call = jsonPlaceHolderApi.kakaoPost(kakaoHeaders, kakaoPost);

//        call.enqueue(new Callback<Post>() {
//            @Override
//            public void onResponse(Call<Post> call, Response<Post> response) {
//
//                Post postResponse = response.body();
//
//                String content = "";
//
//                //Code를 적는 이유는 응답하는 코드를 보기 위해서이다.
//                content += "Code: " + response.code() + "\n";
//
//                //101이 입력되는데 아무것도 넣어주지 않았을 경우, 임의 값으로 101을 넣어준다.
//                content += "ID : " + postResponse.getId() + "\n";
//                content += "User ID : " + postResponse.getUserId() + "\n";
//                content += "Title : " + postResponse.getTitle() + "\n";
//                content += "Text : " + postResponse.getText() + "\n";
//
//                tv_outPut.setText(content);
//
//            }
//
//            @Override
//            public void onFailure(Call<Post> call, Throwable t) {
//
//            }
//        });
    }

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


    //        ---- 참고 ----
//    public class NetworkTask extends AsyncTask<Void, Void, String> {
//
//        private String url;
//        private ContentValues values;
//
//        public NetworkTask(String url, ContentValues values) {
//
//            this.url = url;
//            this.values = values;
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//
//            String result; // 요청 결과를 저장할 변수.
//            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
//            result = requestHttpURLConnection.request(url, values); // 해당 URL로 부터 결과물을 얻어온다.
//
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//
//            //doInBackground()로 부터 리턴된 값이 onPostExecute()의 매개변수로 넘어오므로 s를 출력한다.
//            tv_outPut.setText(s);
//        }
//    }
}
