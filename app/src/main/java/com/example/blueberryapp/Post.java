package com.example.blueberryapp;


import com.google.gson.annotations.SerializedName;

//여기서 HTTP에서 가져올 데이터들의 항목들의 타입을 선언해준다.
public class Post {

    private int userId;

    private Integer id;

    private String title;


    //키를 패스 시켜준다. String으로~>
    // @SerializedName("body") = 어노테이션은 Json응답에서 각각의 필드를 구분하기 위해 사용한다.
    @SerializedName("body")
    private String text;

    public Post(int userId, String title, String text) {
        this.userId = userId;
        this.title = title;
        this.text = text;
    }

    //getter 부분.
    public int getUserId() {
        return userId;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }
}
