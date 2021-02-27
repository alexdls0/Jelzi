package com.example.jelzi.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class DayMoment implements Parcelable {
    public String name;
    public int cals;
    public ArrayList<Food> foods= new ArrayList<>();
    public DayMoment(String name) {
        this.name = name;
        this.cals=0;
    }

    protected DayMoment(Parcel in) {
        name = in.readString();
        cals = in.readInt();
        foods = in.createTypedArrayList(Food.CREATOR);
    }

    public static final Creator<DayMoment> CREATOR = new Creator<DayMoment>() {
        @Override
        public DayMoment createFromParcel(Parcel in) {
            return new DayMoment(in);
        }

        @Override
        public DayMoment[] newArray(int size) {
            return new DayMoment[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCals() {
        return cals;
    }

    public void setCals(int cals) {
        this.cals = cals;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    @Override
    public String toString() {
        return "DayMoment{" +
                "name='" + name + '\'' +
                ", cals=" + cals +
                ", foods=" + foods.toString() +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(cals);
        parcel.writeTypedList(foods);
    }
}
