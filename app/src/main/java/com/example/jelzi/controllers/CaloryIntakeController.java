package com.example.jelzi.controllers;

import java.util.HashMap;

public class CaloryIntakeController {

    public int calcTMB(int height, int weight, int age, boolean gender){
        int TMB=0;
        //Is a men
        if(!gender){
            TMB= (int) ((10*weight)+(6.25*height)-(5*age)+5);
        }else {
            // Is a women
            TMB= (int) ((10*weight)+(6.25*height)-(5*age)-161);
        }
        return TMB;
    }

    public int calcDailyCalsIntake(int TMB, double activity, int objective){
        int dailyCals= (int) (TMB*activity);
        //check objective, 1 lose, 2 maintenance, 3 gain
        //2 no needed to do anything
        if(objective==1){
            //lose weight -300 cals
            dailyCals-=300;
        }else if(objective==3){
            dailyCals+=300;
        }
        return  dailyCals;
    }

    public HashMap<String,Integer> calcMacros(int dailyCals, int weight, boolean highProtein){
        //1g of fat have 9 cals, 1g of prot or carb have 4 cals
        //if highProtein selected 2g per kg of weight

        int remainCals=dailyCals;
        int carbs=0;
        int prot=0;
        int fats= weight;

        if (highProtein){
            prot=weight*2;
        }else{
            prot=weight;
        }
        remainCals-=((prot*4)+(fats*9));
        carbs=remainCals/4;
        if(carbs <= 0){
            carbs = 0;
        }

        HashMap<String,Integer> macros= new HashMap<String, Integer>();
        macros.put("carbs",carbs);
        macros.put("prot",prot);
        macros.put("fats",fats);
        return macros;
    }
}
