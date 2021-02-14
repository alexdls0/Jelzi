package com.example.jelzi;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.example.jelzi.adapter.FirstCalcAdapter;
import com.example.jelzi.controllers.CaloryIntakeController;
import com.example.jelzi.databinding.ActivityMainBinding;
import com.example.jelzi.interfaces.FirstCalcInterface;
import com.example.jelzi.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements FirstCalcInterface {
    public ActivityMainBinding binding;

    private int[] layouts;
    private FirstCalcAdapter mAdapter;
    private TextView[] dots;
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private User user;
    boolean genderPage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createUser();
    }

    private void createUser() {
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users/"+mAuth.getCurrentUser().getUid()+"/tracing/userName").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user= new User(snapshot.getValue(String.class));
                init();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {

        layouts = new int[]{
                R.layout.first_calc_intake_welcome,
                R.layout.first_calc_intake_gender,
                R.layout.first_calc_intake_age,
                R.layout.first_calc_intake_height,
                R.layout.first_calc_intake_weight,
                R.layout.first_calc_intake_activity,
                R.layout.first_calc_intake_objective,};

        mAdapter = new FirstCalcAdapter(layouts,this,this,user);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setAdapter(mAdapter);
        binding.viewPager.registerOnPageChangeCallback(pageChangeCallback);

        binding.btnBack.setVisibility(View.GONE);
        binding.btnNext.setVisibility(View.GONE);

        binding.btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.goBackPage();
            }
        });

        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.goNextPage();
            }
        });
        // adding bottom dots
        addBottomDots(0);
    }

    private void goNextPage() {
        int current = getItem(+1);
        if (current < layouts.length) {
            // move to next screen
            binding.viewPager.setCurrentItem(current);
        }else{
            launchSession();
        }
    }

    private void goBackPage() {
        int current = getItem(-1);
        if (current >= 0) {
            // move to back screen
            if (current==0){
                binding.btnNext.setVisibility(View.VISIBLE);
            }
            binding.viewPager.setCurrentItem(current);
        } else {
            binding.btnBack.setVisibility(View.GONE);
        }
    }

    /*
     * Adds bottom dots indicator
     * */
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        binding.layoutDots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.dots_size));
            dots[i].setTextColor(getColor(R.color.white));
            binding.layoutDots.addView(dots[i]);
            binding.layoutDots.invalidate();
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getColor(R.color.colorGreen));

    }

    private int getItem(int i) {
        return binding.viewPager.getCurrentItem() + i;
    }

    private void launchSession() {
        Intent intent=new Intent(this, Tracing.class);
        startActivity(intent);
    }

    ViewPager2.OnPageChangeCallback pageChangeCallback = new ViewPager2.OnPageChangeCallback() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onPageSelected(int position) {
            super.onPageSelected(position);
            addBottomDots(position);
            binding.btnNext.setVisibility(View.INVISIBLE);
            switch (position){
                case 0:
                    binding.btnNext.setVisibility(View.VISIBLE);
                case 1:
                    if(genderPage){
                        binding.btnNext.setVisibility(View.VISIBLE);
                    }
                case 2:
                    if (user.age>0){
                        binding.btnNext.setVisibility(View.VISIBLE);
                    }
                case 3:
                    if (user.height>0){
                        binding.btnNext.setVisibility(View.VISIBLE);
                    }
                case 4:
                    if (user.weight>0){
                        binding.btnNext.setVisibility(View.VISIBLE);
                    }
                case 5:
                    if (user.activity>0){
                        binding.btnNext.setVisibility(View.VISIBLE);
                    }
            }
            if (position == 0) {
                binding.btnBack.setVisibility(View.GONE);
            }else if (position >0){
                binding.btnBack.setVisibility(View.VISIBLE);
            }

        }
    };

    //Interface  methods for User creation
    @Override
    public void selectedGender(Boolean gender) {
        user.gender=gender;
        binding.btnNext.setVisibility(View.VISIBLE);
        genderPage=true;
    }

    @Override
    public void selectedAge(int age) {
        user.age=age;
        binding.btnNext.setVisibility(View.VISIBLE);

    }

    @Override
    public void selectedHeight(int height) {
        user.height=height;
        binding.btnNext.setVisibility(View.VISIBLE);
    }

    @Override
    public void selectedWeight(int weight) {
        user.weight=weight;
        binding.btnNext.setVisibility(View.VISIBLE);

    }

    @Override
    public void selectedActivity(double activity) {
        user.activity=activity;
        binding.btnNext.setVisibility(View.VISIBLE);
    }

    @Override
    public void selectedObjective(int objective) {
        user.objective=objective;
        //Calc Tmb and daily cals and macros
        CaloryIntakeController caloryIntakeController= new CaloryIntakeController();
        user.tmb =caloryIntakeController.calcTMB(user.height,user.weight,user.age,user.gender);
        System.out.println("tmb: "+user.tmb);
        user.dailyCals=caloryIntakeController.calcDailyCalsIntake(user.tmb,user.activity,user.objective);
        user.setHighProtein(false);
        System.out.println("dailyCals: "+user.dailyCals);
        System.out.println("macros: "+caloryIntakeController.calcMacros(user.dailyCals,user.weight,user.isHighProtein()));
        System.out.println(user);
        binding.btnNext.setVisibility(View.VISIBLE);
        saveTracingFirebase();
    }

    private void saveTracingFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("tracing");
        databaseReference.setValue(user);

    }
}