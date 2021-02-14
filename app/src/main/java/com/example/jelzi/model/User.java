package com.example.jelzi.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {
    public String userName;
    public int height;//In cm
    public int age;
    public boolean gender;//True women, False men
    public int weight;//In Kg
    public int tmb;
    public double activity;
    public int dailyCals;
    public int objective;// 1 lose weigth, 2 maintenance, 3 gain weight
    public boolean highProtein;

    public User(){}

    public User(String userName) {
        this.userName = userName;
    }

    protected User(Parcel in) {
        userName = in.readString();
        height = in.readInt();
        age = in.readInt();
        gender = in.readByte() != 0;
        weight = in.readInt();
        tmb = in.readInt();
        activity = in.readDouble();
        dailyCals = in.readInt();
        objective = in.readInt();
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

    public void setHighProtein(boolean highProtein) {
        this.highProtein = highProtein;
    }

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

    public void setObjective(int objective) {
        this.objective = objective;
    }

    public String getUserName() {
        return userName;
    }

    public int getHeight() {
        return height;
    }

    public int getAge() {
        return age;
    }

    public boolean isHighProtein() {
        return highProtein;
    }

    public boolean isGender() {
        return gender;
    }

    public int getWeight() {
        return weight;
    }

    public int getTmb() {
        return tmb;
    }

    public double getActivity() {
        return activity;
    }

    public int getDailyCals() {
        return dailyCals;
    }

    public int getObjective() {
        return objective;
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                ", height=" + height +
                ", age=" + age +
                ", gender=" + gender +
                ", weight=" + weight +
                ", TMB=" + tmb +
                ", activity=" + activity +
                ", dailyCals=" + dailyCals +
                ", objective=" + objective +
                ", high protein diet=" + highProtein +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeInt(height);
        parcel.writeInt(age);
        parcel.writeByte((byte) (gender ? 1 : 0));
        parcel.writeInt(weight);
        parcel.writeInt(tmb);
        parcel.writeDouble(activity);
        parcel.writeInt(dailyCals);
        parcel.writeInt(objective);
        parcel.writeByte((byte) (highProtein ? 1 : 0));
    }
}
