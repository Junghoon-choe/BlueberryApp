package com.example.blueberryapp;

public class RE_Food {



    String FoodName;
    String FoodPrice; //음식 이름을 저장해두기 위한 변수
    String FoodImageUrl;

    //Constructors
    public RE_Food() {
    }


    public RE_Food(String foodName, String foodPrice, String imageUrl) {


        FoodName = foodName;
        FoodPrice = foodPrice;
        FoodImageUrl = imageUrl;
    }

    public String getFoodName() {
        return FoodName;
    }

    public void setFoodName(String foodName) {
        FoodName = foodName;
    }

    public String getFoodPrice() {
        return FoodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        FoodPrice = foodPrice;
    }

    public String getImageUrl() {
        return FoodImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        FoodImageUrl = imageUrl;
    }
}
// 이후 새로운 클래스 생성해서 어댑터 구현.