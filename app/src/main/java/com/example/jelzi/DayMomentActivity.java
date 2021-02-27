package com.example.jelzi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jelzi.adapter.FoodAdapter;
import com.example.jelzi.databinding.ActivityDayMomentBinding;
import com.example.jelzi.model.DayMoment;
import com.example.jelzi.model.Food;
import com.example.jelzi.model.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.function.Consumer;

public class DayMomentActivity extends AppCompatActivity {

    public FoodAdapter foodAdapter;
    public ActivityDayMomentBinding  binding;
    public DayMoment dayMoment;
    public ArrayList<Food> foods;
    public int prot,fat,carb, cals;
    private DatabaseReference databaseReferenceDayMoment;
    private FirebaseAuth mAuth;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityDayMomentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getIntentData();
        init();
    }

    private void getIntentData() {
        dayMoment=getIntent().getParcelableExtra("dayMoment");
        foods=dayMoment.foods;
    }

    private void init() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT | ItemTouchHelper.DOWN | ItemTouchHelper.UP) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                //Remove swiped item from list and notify the RecyclerView
                int position = viewHolder.getAdapterPosition();
                Food food=foods.get(position);
                foods.remove(food);
                removeFromFirebase(food);
                foodAdapter.notifyDataSetChanged();
                putValues();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(binding.rvFoodList);
        binding.rvFoodList.setLayoutManager(new LinearLayoutManager(this));
        foodAdapter= new FoodAdapter(foods);
        binding.rvFoodList.setAdapter(foodAdapter);
        binding.tvDayMoment.setText(dayMoment.getName());

        putValues();
    }

    private void removeFromFirebase(Food food) {
        utils=new Utils();
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
                            if(food.getFoodId().equals(dataSnapshot.getKey()))
                                dataSnapshot.getRef().removeValue();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void putValues() {
        prot=0;
        carb=0;
        fat=0;
        cals=0;
        for (Food food:foods) {
            prot+=(int)food.getProt();
            fat+=(int)food.getFat();
            carb+=(int)food.getCarbh();
            cals+=food.getCals();
        }
        binding.calsDaily.setText(cals+"");
        binding.tvCarbh.setText(carb+"");
        binding.tvFat.setText(fat+"");
        binding.tvProt.setText(prot+"");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
    }
}