package com.example.myapp.model;

public class Shop {
    String shop_name;
    Integer imageUrl;
    String address1;
    String address2;


    public Shop(String shop_name, Integer imageUrl, String address1, String address2) {
        this.shop_name = shop_name;
        this.imageUrl = imageUrl;
        this.address1 = address1;
        this.address2 = address2;

    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public Integer getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(Integer imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }
}
