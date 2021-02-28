package com.example.jelzi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import com.example.jelzi.adapter.DayMomentsAdapter;
import com.example.jelzi.databinding.FragmentFoodsBinding;
import com.example.jelzi.interfaces.OnDayMomentClick;
import com.example.jelzi.model.DayMoment;
import com.example.jelzi.model.Food;
import com.example.jelzi.model.User;
import com.example.jelzi.model.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.function.Consumer;

public class Historical extends AppCompatActivity implements OnDayMomentClick {

    private FirebaseAuth mAuth;
    private TextView calsDaily, tvFat, tvCarbh, tvProt, tvDay;
    private final Utils utils = new Utils();
    private int totalCals=0, totalFat=0, totalCarbh=0, totalProt=0;
    private String proteins = "prot", carbs = "carbh", fats = "fat", calories = "cals";
    private String date;
    private String goal;


    private RecyclerView rvDayMoments;
    private DayMomentsAdapter dayMomentsAdapter;
    private DatabaseReference databaseReferenceDayMoment;
    public ArrayList<Food> foods= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical);
        Intent intent = getIntent();
        date = intent.getExtras().getString("date");
        init();
    }

    private void init() {
        mAuth=FirebaseAuth.getInstance();
        calsDaily = findViewById(R.id.calsDaily);
        tvFat = findViewById(R.id.tvFat);
        tvCarbh = findViewById(R.id.tvCarbh);
        tvProt = findViewById(R.id.tvProt);
        tvDay = findViewById(R.id.tvDay);

        if (utils.isConnected(Historical.this)){

            try {
                String newDate = date.replace("/", "-");
                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date theDate = inFormat.parse(newDate);
                SimpleDateFormat outFormat = new SimpleDateFormat("MMMM dd, yyyy");
                goal = outFormat.format(theDate);
                tvDay.setText(goal);
            } catch (Exception e) {
                e.printStackTrace();
            }

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users")
                    .child(mAuth.getCurrentUser().getUid()+"/foodEntries")
                    .child(date);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot horary: dataSnapshot.getChildren()){
                        for(DataSnapshot food: horary.getChildren()){
                            if(food.hasChild(proteins)){
                                totalProt += Integer.parseInt(food.child(proteins).getValue().toString());
                            }
                            if(food.hasChild(calories)){
                                totalCals +=Integer.parseInt(food.child(calories).getValue().toString());
                            }
                            if(food.hasChild(fats)){
                                totalFat +=Integer.parseInt(food.child(fats).getValue().toString());
                            }
                            if(food.hasChild(carbs)){
                                totalCarbh +=Integer.parseInt(food.child(carbs).getValue().toString());
                            }
                        }
                    }
                    calsDaily.setText(Integer.toString(totalCals));
                    tvCarbh.setText(totalCarbh+"g");
                    tvFat.setText(totalFat+"g");
                    tvProt.setText(totalProt+"g");

                    putDayMoments();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else{
            TastyToast.makeText(Historical.this, getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    private void putDayMoments() {
        ArrayList<DayMoment> dayMoments= new ArrayList<>();
        dayMoments.add(new DayMoment(getResources().getString(R.string.morning)));
        dayMoments.add(new DayMoment(getResources().getString(R.string.midday)));
        dayMoments.add(new DayMoment(getResources().getString(R.string.afternoon)));
        dayMoments.add(new DayMoment(getResources().getString(R.string.night)));

        rvDayMoments = findViewById(R.id.rvDayMoments);
        LinearLayoutManager layoutManager = new LinearLayoutManager(Historical.this){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        rvDayMoments.setLayoutManager(new GridLayoutManager(Historical.this,2));
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
                .child(date)
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
                .child(date)
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
                    Intent intent = new Intent(Historical.this, DayMomentActivity.class);
                    intent.putExtra("dayMoment", dayMoment);
                    intent.putExtra("dateDayMoment", goal);
                    databaseReferenceDayMoment.removeEventListener(this);
                    startActivity(intent);
                    Historical.this.overridePendingTransition(R.anim.slide_up_out,R.anim.slide_up_in);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void OnFoodClick(DayMoment dayMoment) {
        getDayMomentFoods(dayMoment);
    }
}