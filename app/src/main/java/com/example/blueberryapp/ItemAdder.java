package com.example.blueberryapp;

class ItemAdder {

    String Image,ItemCode,Price;

    public ItemAdder() {
    }



    public ItemAdder(String image, String itemCode, String price) {
        Image = image;
        ItemCode = itemCode;
        Price = price;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }
}
