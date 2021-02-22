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
import com.example.jelzi.model.DayMoment;
import com.example.jelzi.model.Food;
import com.example.jelzi.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Foods extends Fragment {

    private RecyclerView rvFoods;
    private RecyclerView rvDayMoments;
    private ArrayList<Food> foodList;
    private FoodAdapter foodAdapter;
    private DayMomentsAdapter dayMomentsAdapter;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private FragmentFoodsBinding binding;
    private User user;
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
        rvDayMoments.setLayoutManager(new LinearLayoutManager(getContext()));
        dayMomentsAdapter=new DayMomentsAdapter(dayMoments);
        rvDayMoments.setAdapter(dayMomentsAdapter);
        rvDayMoments.setNestedScrollingEnabled(false);

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


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            databaseReference.child("users/"+mAuth.getCurrentUser().getUid()+"/foodEntries").
                    child(foodList.get(position).getFoodKey()).removeValue();
            foodList.remove(position);
            foodAdapter.notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed))
                    .addSwipeRightActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}