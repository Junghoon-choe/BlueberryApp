package com.example.blueberryapp;

public class RE_REVIEW_test {


    private String 작성자이메일;
    private String DocuName;
    private String title_Review;
    private String writingReview; //음식 이름을 저장해두기 위한 변수
    String ImageUrl;
    private String userName;

    //Constructors


    public RE_REVIEW_test() {

    }

    public RE_REVIEW_test(String 작성자이메일, String docuName, String title_Review, String writingReview, String imageUrl, String userName) {
        this.작성자이메일 = 작성자이메일;
        DocuName = docuName;
        this.title_Review = title_Review;
        this.writingReview = writingReview;
        ImageUrl = imageUrl;
        this.userName = userName;
    }

    public String get작성자이메일() {
        return 작성자이메일;
    }

    public void set작성자이메일(String 작성자이메일) {
        this.작성자이메일 = 작성자이메일;
    }

    public String getDocuName() {
        return DocuName;
    }

    public void setDocuName(String docuName) {
        DocuName = docuName;
    }

    public String getTitle_Review() {
        return title_Review;
    }

    public void setTitle_Review(String title_Review) {
        this.title_Review = title_Review;
    }

    public String getWritingReview() {
        return writingReview;
    }

    public void setWritingReview(String writingReview) {
        this.writingReview = writingReview;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
// 이후 새로운 클래스 생성해서 어댑터 구현.