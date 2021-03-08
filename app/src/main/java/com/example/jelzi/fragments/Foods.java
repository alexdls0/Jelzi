package com.example.jelzi.fragments;

import android.content.Intent;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jelzi.DayMomentActivity;
import com.example.jelzi.R;
import com.example.jelzi.SearchFoodActivity;
import com.example.jelzi.adapter.DayMomentsAdapter;
import com.example.jelzi.controllers.CaloryIntakeController;
import com.example.jelzi.databinding.FragmentFoodsBinding;
import com.example.jelzi.interfaces.OnDayMomentClick;
import com.example.jelzi.model.DayMoment;
import com.example.jelzi.model.Food;
import com.example.jelzi.model.User;
import com.example.jelzi.model.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class Foods extends Fragment implements OnDayMomentClick {

    //private RecyclerView rvFoods;
    private RecyclerView rvDayMoments;
    private ArrayList<Food> foodList;
    private DayMomentsAdapter dayMomentsAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceDayMoment;
    DatabaseReference databaseReferenceDayData;
    private FragmentFoodsBinding binding;
    private User user;
    private Utils utils;
    public ArrayList<Food> foods= new ArrayList<>();

    private int totalDayCals=0, totalFat=0, totalCarbh=0, totalProt=0;
    private String proteins = "prot", carbs = "carbh", fats = "fat", calories = "cals";

    public Foods() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentFoodsBinding.inflate(
                inflater,  container, false);
        View view = binding.getRoot();
        init();
        getUser();

        foodList = new ArrayList<>();
        return view;
    }

    private void init() {
        utils=new Utils();
        binding.addFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SearchFoodActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_up_out,R.anim.slide_up_in);
            }
        });
    }

    private void getUser() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference =  FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("tracing");
        //getUser
        databaseReference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.hasChild("dailyCals")){
                    user= snapshot.getValue(User.class);
                    //Update user values if occur any change
                    CaloryIntakeController caloryIntakeController= new CaloryIntakeController();
                    user.tmb =caloryIntakeController.calcTMB(user.height,user.weight,user.age,user.gender);
                    user.dailyCals=caloryIntakeController.calcDailyCalsIntake(user.tmb,user.activity,user.objective);
                    HashMap<String,Integer> macros = caloryIntakeController.calcMacros(user.dailyCals,user.weight,user.highProtein);
                    user.fats=macros.get("fats");
                    user.carbs=macros.get("carbs");
                    user.prot=macros.get("prot");
                    databaseReference.setValue(user);
                    putDayData();
                    putDayMoments();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void putDayMoments() {
        ArrayList<DayMoment> dayMoments= new ArrayList<>();
        dayMoments.add(new DayMoment("Morning"));
        dayMoments.add(new DayMoment("Midday"));
        dayMoments.add(new DayMoment("Afternoon"));
        dayMoments.add(new DayMoment("Night"));

        rvDayMoments = binding.rvDayMoments;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvDayMoments.setLayoutManager(new GridLayoutManager(getContext(),2));
        dayMomentsAdapter=new DayMomentsAdapter(dayMoments,this);
        rvDayMoments.setAdapter(dayMomentsAdapter);
        rvDayMoments.setNestedScrollingEnabled(false);
        for (DayMoment dayMoment:dayMoments) {
            getDayMomentCals(dayMoment);
        }
    }

    private void getDayMomentCals(DayMoment dayMoment) {

        mAuth = FirebaseAuth.getInstance();
        databaseReferenceDayMoment =  FirebaseDatabase.getInstance().getReference().child("users")
                    .child(mAuth.getCurrentUser().getUid())
                    .child("foodEntries")
                    .child(utils.getYearMonth())
                    .child(utils.getDay())
                    .child(dayMoment.name.toLowerCase());

        databaseReferenceDayMoment.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    dayMoment.cals=0;
                    foods.removeAll(foods);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        snapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                            @Override
                            public void accept(DataSnapshot dataSnapshot) {
                                Food food=dataSnapshot.getValue(Food.class);
                                food.setFoodId(dataSnapshot.getKey());
                                dayMoment.cals+=food.getCals();
                            }
                        });
                        dayMomentsAdapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    private void getDayMomentFoods(DayMoment dayMoment) {

        mAuth = FirebaseAuth.getInstance();
        databaseReferenceDayMoment =  FirebaseDatabase.getInstance().getReference().child("users")
                .child(mAuth.getCurrentUser().getUid())
                .child("foodEntries")
                .child(utils.getYearMonth())
                .child(utils.getDay())
                .child(dayMoment.name.toLowerCase());

        databaseReferenceDayMoment.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                foods.removeAll(foods);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    snapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                        @Override
                        public void accept(DataSnapshot dataSnapshot) {
                            Food food=dataSnapshot.getValue(Food.class);
                            food.setFoodId(dataSnapshot.getKey());
                            foods.add(food);
                        }
                    });
                    dayMoment.setFoods(foods);
                    Intent intent = new Intent(getContext(), DayMomentActivity.class);
                    intent.putExtra("dayMoment",dayMoment);
                    databaseReferenceDayMoment.removeEventListener(this);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.slide_up_out,R.anim.slide_up_in);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void putDayData() {

        //macros and cals data from database
        databaseReferenceDayData = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(mAuth.getCurrentUser().getUid())
                .child("foodEntries")
                .child(utils.getYearMonth())
                .child(utils.getDay());

        databaseReferenceDayData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalDayCals=0;
                totalCarbh=0;
                totalFat=0;
                totalProt=0;
                for (DataSnapshot horary: dataSnapshot.getChildren()){
                    for(DataSnapshot food: horary.getChildren()){
                        if(food.hasChild(proteins)){
                            totalProt += Integer.parseInt(food.child(proteins).getValue().toString());
                        }
                        if(food.hasChild(calories)){
                            totalDayCals +=Integer.parseInt(food.child(calories).getValue().toString());
                        }
                        if(food.hasChild(fats)){
                            totalFat +=Integer.parseInt(food.child(fats).getValue().toString());
                        }
                        if(food.hasChild(carbs)){
                            totalCarbh +=Integer.parseInt(food.child(carbs).getValue().toString());
                        }
                    }
                }

                binding.currentCals.setText(totalDayCals+"");

                binding.tvProt.setText(totalProt+" / "+user.prot+"g");
                binding.tvCarbh.setText(totalCarbh+" / "+user.carbs+"g");
                binding.tvFat.setText(totalFat+" / "+user.fats+"g");

                binding.progressCals.setProgress(totalDayCals,true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        //userObjectiveCals
        binding.calsDaily.setText(String.valueOf(user.dailyCals));
        binding.progressCals.setTotal(user.dailyCals);
    }


    @Override
    public void OnFoodClick(DayMoment dayMoment) {
        getDayMomentFoods(dayMoment);
    }
}