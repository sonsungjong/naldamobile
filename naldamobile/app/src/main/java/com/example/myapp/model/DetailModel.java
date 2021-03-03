package com.example.myapp.model;

public class DetailModel {

    String dPdtName;
    String dType;
    String dShot;
    String dQuantity;
    String dPrice;

    public DetailModel(String dPdtName, String dType, String dShot, String dQuantity, String dPrice) {
        this.dPdtName = dPdtName;
        this.dType = dType;
        this.dShot = dShot;
        this.dQuantity = dQuantity;
        this.dPrice = dPrice;
    }

    public String getdPdtName() {
        return dPdtName;
    }

    public void setdPdtName(String dPdtName) {
        this.dPdtName = dPdtName;
    }

    public String getdType() {
        return dType;
    }

    public void setdType(String dType) {
        this.dType = dType;
    }

    public String getdShot() {
        return dShot;
    }

    public void setdShot(String dShot) {
        this.dShot = dShot;
    }

    public String getdQuantity() {
        return dQuantity;
    }

    public void setdQuantity(String dQuantity) {
        this.dQuantity = dQuantity;
    }

    public String getdPrice() {
        return dPrice;
    }

    public void setdPrice(String dPrice) {
        this.dPrice = dPrice;
    }
}
