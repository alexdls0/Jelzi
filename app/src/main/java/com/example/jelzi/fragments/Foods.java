package com.example.jelzi.fragments;

import android.content.Intent;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jelzi.Login;
import com.example.jelzi.R;
import com.example.jelzi.Register;
import com.example.jelzi.SearchFoodActivity;
import com.example.jelzi.adapter.DayMomentsAdapter;
import com.example.jelzi.adapter.FoodAdapter;
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

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Foods extends Fragment implements OnDayMomentClick {

    private RecyclerView rvFoods;
    private RecyclerView rvDayMoments;
    private ArrayList<Food> foodList;
    private FoodAdapter foodAdapter;
    private DayMomentsAdapter dayMomentsAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceDayMoment;
    private FragmentFoodsBinding binding;
    private User user;
    private Utils utils;
    public ArrayList<Food> foods= new ArrayList<>();

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
        //rvFoods.setLayoutManager(new LinearLayoutManager(getContext()));
        //foodAdapter = new FoodAdapter(foodList);
        //rvFoods.setAdapter(foodAdapter);
        //ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        //itemTouchHelper.attachToRecyclerView(rvFoods);

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
        dayMoments.add(new DayMoment(getResources().getString(R.string.morning)));
        dayMoments.add(new DayMoment(getResources().getString(R.string.midday)));
        dayMoments.add(new DayMoment(getResources().getString(R.string.afternoon)));
        dayMoments.add(new DayMoment(getResources().getString(R.string.night)));

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
            getDayMomentFood(dayMoment);
        }
    }

    private void getDayMomentFood(DayMoment dayMoment) {
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
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        snapshot.getChildren().forEach(new Consumer<DataSnapshot>() {
                            @Override
                            public void accept(DataSnapshot dataSnapshot) {
                                foods.add(dataSnapshot.getValue(Food.class));
                                System.out.println("daymoment "+dayMoment.name+ " food "+dataSnapshot.getValue(Food.class));
                            }
                        });
                        dayMoment.cals=calcCals();
                        dayMoment.foods=foods;
                        System.out.println(foods);
                        dayMomentsAdapter.notifyDataSetChanged();
                        
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }

    private void putDayMomentFood(ArrayList<Food> foods) {

    }

    private int calcCals() {
        int cals=0;
        for (Food food:foods) {
            cals+=food.getCals();
        }
        return cals;
    }

    private void putDayData() {
        //provisional
        int actualCalsOfDay=1500;

        //macros and cals data
        binding.calsDaily.setText(String.valueOf(user.dailyCals));
        binding.currentCals.setText(String.valueOf(actualCalsOfDay));

        binding.tvProt.setText("0"+" / "+user.prot+"g");
        binding.tvCarbh.setText("0"+" / "+user.carbs+"g");
        binding.tvFat.setText("0"+" / "+user.fats+"g");

        //progressCals data
        binding.progressCals.setTotal(user.dailyCals);

        binding.progressCals.setProgress(1200,true);

    }


    @Override
    public void OnFoodClick(DayMoment dayMoment) {
        dayMomentsAdapter.notifyDataSetChanged();
        System.out.println(dayMoment.toString());
    }
}