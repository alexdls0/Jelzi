package com.example.jelzi.model;

public class DayMoment {
    public String name;
    public int cals;

    public DayMoment(String name) {
        this.name = name;
        this.cals=0;
    }

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

    @Override
    public String toString() {
        return "DayMoment{" +
                "name='" + name + '\'' +
                ", cals=" + cals +
                '}';
    }
}
