package com.example.myapp.model;

public class HistoryModel {

    String hShopName;
    String hState;
    String hDateTime;
    String hPdtName;
    String hPayPrice;
    String hClassify;
    String hReserveTime;

    public HistoryModel(String hShopName, String hState, String hDateTime, String hPdtName, String hPayPrice, String hClassify, String hReserveTime) {
        this.hShopName = hShopName;
        this.hState = hState;
        this.hDateTime = hDateTime;
        this.hPdtName = hPdtName;
        this.hPayPrice = hPayPrice;
        this.hClassify = hClassify;
        this.hReserveTime = hReserveTime;
    }

    public String gethShopName() {
        return hShopName;
    }

    public void sethShopName(String hShopName) {
        this.hShopName = hShopName;
    }

    public String gethState() {
        return hState;
    }

    public void sethState(String hState) {
        this.hState = hState;
    }

    public String gethDateTime() {
        return hDateTime;
    }

    public void sethDateTime(String hDateTime) {
        this.hDateTime = hDateTime;
    }

    public String gethPdtName() {
        return hPdtName;
    }

    public void sethPdtName(String hPdtName) {
        this.hPdtName = hPdtName;
    }

    public String gethPayPrice() {
        return hPayPrice;
    }

    public void sethPayPrice(String hPayPrice) {
        this.hPayPrice = hPayPrice;
    }

    public String gethClassify() {
        return hClassify;
    }

    public void sethClassify(String hClassify) {
        this.hClassify = hClassify;
    }

    public String gethReserveTime() {
        return hReserveTime;
    }

    public void sethReserveTime(String hReserveTime) {
        this.hReserveTime = hReserveTime;
    }
}
