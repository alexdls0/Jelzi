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
    private Boolean canGoNext=true;
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
        databaseReference.child("users/"+mAuth.getCurrentUser().getUid()+"/userName").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user= new User(mAuth.getCurrentUser().getUid(),snapshot.getValue(String.class));
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
                R.layout.first_calc_intake_activity,};

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
            canGoNext=false;
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
                canGoNext=true;
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
            if(canGoNext){
                binding.btnNext.setVisibility(View.VISIBLE);
            }else{
                binding.btnNext.setVisibility(View.GONE);
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
        canGoNext=true;
    }

    @Override
    public void selectedAge(int age) {
        user.age=age;
        canGoNext=true;
    }

    @Override
    public void selectedHeight(int height) {
        user.height=height;
        canGoNext=true;
    }

    @Override
    public void selectedWeight(int weight) {
        user.weight=weight;
        canGoNext=true;
    }

    @Override
    public void selectedActivity(double activity) {
        user.activity=activity;
        canGoNext=true;
    }

    @Override
    public void selectedObjective(int objective) {
        user.objective=objective;
        //Calc Tmb and daily cals req
        canGoNext=true;
    }
}