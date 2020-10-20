package com.example.blueberryapp;

import androidx.media.AudioAttributesCompat;

import com.google.type.DateTime;
import com.kakao.auth.authorization.authcode.AuthorizationCode;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @FormUrlEncoded
    @POST("v1/payment/ready")
    Call<kakaoPost> kakaoPost(
            @FieldMap Map<String, String> parameters
//            @Field("cid") String cid,
//            @Field("partner_order_id") String partner_order_id,
//            @Field("partner_user_id") String partner_user_id,
//            @Field("item_name") String item_name,
//            @Field("quantity") Integer quantity,
//            @Field("total_amount") Integer total_amount,
//            @Field("tax_free_amount") Integer tax_free_amount,
//            @Field("approval_url") String approval_url,
//            @Field("fail_url") String fail_url,
//            @Field("cancel_url") String cancel_url
    );

    @FormUrlEncoded
    @GET("v1/payment/ready")
    Call<List<kakaoGet>> kakaoGet(
//            @Body Map<String, String> parameters




            //int >> Integer로 변경하면 null로도 출력이 가능하다.
            //Integer >> Integer[]로 변경해서 입력이 되는 값마다 출력 할수 있게 한다.
            // []{} 중괄호에 넣고싶은 값을 넣는다.
            @Query("tid") String tid,
            @Query("next_redirect_app_url") String next_redirect_app_url,
            @Query("next_redirect_mobile_url") String next_redirect_mobile_url,
            @Query("next_redirect_pc_url") String next_redirect_pc_url,
            @Query("android_app_scheme") String android_app_scheme,
            @Query("ios_app_scheme") String ios_app_scheme,
            @Query("created_at") String created_at
    );


    //  6.헤더 넣기
    //    @Headers({"Static-Header1: 123", "Static-Header2: 456"})
    //    @PUT("posts/{id}")
    //    Call<Post> putPost(@Header("Dynamic-Header") String header,
    //                       @Path("id") int id,
    //                       @Body Post post);


// GET

    //base URL뒤에 있는 posts URL을 참조한다.
    //아래와 같이 포스팅을 하게 되면
    //주소창에 이런식으로 검색이 된다comments?userId=1&_sort=id&_order=desc
    // (,)콤마가 주소창에서의 &역할을 한다.
    //userId=빈칸(메인에서 입력받음)&_sort=빈칸(메인에서 입력받음)&_order=빈칸(메인에서 입력받음)
    //메인에서 입력받는다는 말은 메인에서 해당 메서드가 적용된 파라미터칸에 넣어야 된다는 말이다.


    //      아래와 같이 전체 주소를 넣어서 사용할 수 있다.
//      @GET("http://jsonplaceholder.typicode.com/posts")
    //또한 "http://jsonplaceholder.typicode.com/v3/"
    // @GET("/posts") 이렇게 앞에 슬레쉬를 붙여줘야 com/뒤에 붙는다. 붙이지 않으면 v3/post이렇게 된다.
    @GET("posts")
    Call<List<Post>> getPosts(

            //int >> Integer로 변경하면 null로도 출력이 가능하다.
            //Integer >> Integer[]로 변경해서 입력이 되는 값마다 출력 할수 있게 한다.
            // []{} 중괄호에 넣고싶은 값을 넣는다.
            @Query("userId") Integer[] userId,
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    //이런식으로 주소창에 작성이 된다

    //맵에는 STRING타입의 키와 벨류를 넣을수 있다.
    // MAP<키,벨류> 이런식으로 넣으면 된다.
    //@Query( (키 :) "userId") Integer[] (벨류 :) userId,
    // 현재 맵의 키값에는  "userId"
    // 벨류값은 userId 이 들어간다.
    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> paramters);


    //base URL뒤에 있는 posts/입력된값/comments URL을 참조한다.
    //입력된 값의 주소를 갖고 그 주소에 맞게 파싱을 한다.
    //뒤의 파라메터값을 메인클라스에서 갖는다.
    @GET("posts/{id}/comments")
    Call<List<PostComment>> getComments(@Path("id") int postId);


    //메인에 직접 URL을 적어서 불러올수 있다.
    @GET
    Call<List<PostComment>> getComments(@Url String url);


    // POST

    @POST("posts")
    Call<Post> createPost(@Body Post post);

    //TODO : @FormUrlEncoded 왜 붙이는지 알아보기.
    //@FormUrlEncoded : Field 형식을 사용할 때는 Form이 Encoding되어야 합니다.
    // 따라서 FormUrlEncoded라는 Annotation을 해주어야 합니다.
    //출처: https://kor45cw.tistory.com/5 [Developer's]

    //만들고자 하는 주소방식
    //userId=23&title=New%20Title&body=New%20Text
    //userId =23 & title =New %20 Title & body =New %20 Text
    //userId 에 23이라는 값을 넣고 뒤에 & 붙혀서 뒤에올 키를 받는다.
    //GET에서의 Query방식과 비슷하다.
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(
            //다양한 변수를 넣을 수 있다. ~ List 등등...
            @Field("userId") int userId,
            @Field("title") String title,
            @Field("body") String text
    );


    //GET에서  맵을 사용시 따로 Path를 할 수 있게 만들어 줘야 하는데
    //POST에서는 따로 Path구문을 만들어서 작업할 필요가 없다.
    @FormUrlEncoded
    @POST("posts")
    Call<Post> createPost(@FieldMap Map<String, String> fields);


    // PUT PATCH DELETE

    //PUT PATCH와 다른점 : 패치는 해당하는 필드의 값만 바꿔주는데 풋같은 경우는 전체적으로 소스를 덮어버린다.
    //The main difference between the PUT and PATCH method is that
    // the PUT method uses the request URI to supply a modified version of the requested
    // resource which replaces the original version of the resource,
    // whereas the PATCH method supplies a set of instructions to modify the resource.

    // 6.헤더 넣기
    @Headers({"Static-Header1: 123", "Static-Header2: 456"})
    @PUT("posts/{id}")
    Call<Post> putPost(@Header("Dynamic-Header") String header,
                       @Path("id") int id,
                       @Body Post post);

    // 6.헤더 맵으로 넣기. getPost의 퀘리와 createPost의 필드맵과 같다.
    @PATCH("posts/{id}")
    Call<Post> patchPost(@HeaderMap Map<String, String> headers,
                         @Path("id") int id,
                         @Body Post post);

    @DELETE("posts/{id}")
    Call<Void> deletePost(@Path("id") int id);


}
