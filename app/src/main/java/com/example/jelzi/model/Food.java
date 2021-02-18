package com.example.jelzi.model;

import java.math.BigDecimal;

public class Food {

    private String foodName, foodKey;
    private double fat, prot, carbh;
    private BigDecimal cals;
    public Food(){
        this.foodName = "";
        this.foodKey = "";
        this.fat = 0;
        this.prot = 0;
        this.carbh = 0;
        this.cals = BigDecimal.valueOf(0);
    }

    public Food(String foodName, String foodKey, double fat, double prot, double carbh, BigDecimal cals) {
        this.foodName = foodName;
        this.foodKey = foodKey;
        this.fat = fat;
        this.prot = prot;
        this.carbh = carbh;
        this.cals = cals;
    }

    public String getFoodKey() {
        return foodKey;
    }

    public void setFoodKey(String foodKey) {
        this.foodKey = foodKey;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getProt() {
        return prot;
    }

    public void setProt(double prot) {
        this.prot = prot;
    }

    public double getCarbh() {
        return carbh;
    }

    public void setCarbh(double carbh) {
        this.carbh = carbh;
    }

    public BigDecimal getCals() {
        return cals;
    }

    public void setCals(BigDecimal cals) {
        this.cals = cals;
    }
}
