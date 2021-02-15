package com.example.jelzi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String UUID;
    public String userName;
    public int height;//In cm
    public int age;
    public boolean gender;//True women, False men
    public int weight;//In Kg
    public int tmb;
    public double activity;
    public int dailyCals;
    public int objective;// 1 lose weigth, 2 maintenance, 3 gain weight
    public int prot;
    public int carbs;
    public int fats;
    public boolean highProtein;//True women, False men

    public User(String UUID, String userName) {
        this.UUID = UUID;
        this.userName = userName;
        this.highProtein=true;
    }

    public User() {
    }

    protected User(Parcel in) {
        UUID = in.readString();
        userName = in.readString();
        height = in.readInt();
        age = in.readInt();
        gender = in.readByte() != 0;
        weight = in.readInt();
        tmb = in.readInt();
        activity = in.readDouble();
        dailyCals = in.readInt();
        prot = in.readInt();
        carbs = in.readInt();
        fats = in.readInt();
        highProtein = in.readByte() != 0;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setTmb(int tmb) {
        this.tmb = tmb;
    }

    public void setActivity(double activity) {
        this.activity = activity;
    }

    public void setDailyCals(int dailyCals) {
        this.dailyCals = dailyCals;
    }

    public void setHighProtein(boolean highProtein) {
        this.highProtein = highProtein;
    }

    @Override
    public String toString() {
        return "User{" +
                "UUID='" + UUID + '\'' +
                ", userName='" + userName + '\'' +
                ", height=" + height +
                ", age=" + age +
                ", gender=" + gender +
                ", weight=" + weight +
                ", TMB=" + tmb +
                ", activity=" + activity +
                ", dailyCals=" + dailyCals +
                ", prot=" + prot +
                ", carbs=" + carbs +
                ", fats=" + fats +
                ", highProtein=" + highProtein +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(UUID);
        parcel.writeString(userName);
        parcel.writeInt(height);
        parcel.writeInt(age);
        parcel.writeByte((byte) (gender ? 1 : 0));
        parcel.writeInt(weight);
        parcel.writeInt(tmb);
        parcel.writeDouble(activity);
        parcel.writeInt(dailyCals);
        parcel.writeInt(prot);
        parcel.writeInt(carbs);
        parcel.writeInt(fats);
        parcel.writeByte((byte) (highProtein ? 1 : 0));
    }
}
