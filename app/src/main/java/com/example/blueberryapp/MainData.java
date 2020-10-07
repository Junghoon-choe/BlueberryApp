package com.example.blueberryapp;

public class MainData {


    private String food_item_name;
    private String food_item_price;


//구조
    // 개터 새터


    public String getFood_item_name() {
        return food_item_name;
    }

    public void setFood_item_name(String food_item_name) {
        this.food_item_name = food_item_name;
    }

    public String getFood_item_price() {
        return food_item_price;
    }

    public void setFood_item_price(String food_item_price) {
        this.food_item_price = food_item_price;
    }


    public MainData(String food_item_name, String food_item_price) {

        this.food_item_name = food_item_name;
        this.food_item_price = food_item_price;
    }
    /*{

        private String ReViewID;
        private String ReViewTitle;
        private String ReViewWriting;

        private String userName = MyApplication.회원Name;

        public String getId() {
        return ReViewID;
    }

        public void setId(String id) {
        this.ReViewID = id;
    }

        public String getTitle() {
        return ReViewTitle;
    }

        public void setTitle(String title) {
        this.ReViewTitle = title;
    }

        public String getWriting() {
        return ReViewWriting;
    }

        public void setWriting(String writing) {
        this.ReViewWriting = writing;
    }

    public RE_Review(String id, String title, String writing) {
        this.ReViewID = userName;
        this.ReViewTitle = title;
        this.ReViewWriting = writing;
    }



    }*/



}
