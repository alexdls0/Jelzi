package com.example.jelzi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable {
    private long foodId;
    private String foodName;
    private int fat, prot, carbh;
    private int cals;
    private int serving;


    public Food(){
        this.foodId=0;
        this.foodName = "";
        this.fat = 0;
        this.prot = 0;
        this.carbh = 0;
        this.cals = 0;
        this.serving=0;
    }

    public Food(String foodName, int fat, int prot, int carbh, int cals) {
        this.foodName = foodName;
        this.fat = fat;
        this.prot = prot;
        this.carbh = carbh;
        this.cals = cals;
    }


    protected Food(Parcel in) {
        foodId = in.readLong();
        foodName = in.readString();
        fat = in.readInt();
        prot = in.readInt();
        carbh = in.readInt();
        cals = in.readInt();
        serving = in.readInt();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public long getFoodId() {
        return foodId;
    }

    public void setFoodId(long foodId) {
        this.foodId = foodId;
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

    public void setFat(int fat) {
        this.fat = fat;
    }

    public double getProt() {
        return prot;
    }

    public void setProt(int prot) {
        this.prot = prot;
    }

    public double getCarbh() {
        return carbh;
    }

    public void setCarbh(int carbh) {
        this.carbh = carbh;
    }

    public int getCals() {
        return cals;
    }

    public void setCals(int cals) {
        this.cals = cals;
    }

    public int getServing() {
        return serving;
    }

    public void setServing(int serving) {
        this.serving = serving;
    }

    @Override
    public String toString() {
        return "Food{" +
                "foodId=" + foodId +
                ", foodName='" + foodName + '\'' +
                ", fat=" + fat +
                ", prot=" + prot +
                ", carbh=" + carbh +
                ", cals=" + cals +
                ", serving=" + serving +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(foodId);
        parcel.writeString(foodName);
        parcel.writeInt(fat);
        parcel.writeInt(prot);
        parcel.writeInt(carbh);
        parcel.writeInt(cals);
        parcel.writeInt(serving);
    }
}
