package com.example.jelzi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.jelzi.databinding.ActivityAuthCheckBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AuthCheckActivity extends AppCompatActivity {
    public ActivityAuthCheckBinding binding;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        binding=ActivityAuthCheckBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        startAnim();
        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser() != null){
            hasTracing();
        }else{
            goLogin();
        }
    }

    private void hasTracing() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()+"/tracing");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild("dailyCals")){
                    goTracing();
                }
                else{
                    goMain();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void goLogin() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_right);
    }

    private void goMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_right);
    }

    public void goTracing(){
        Intent intent = new Intent(this, Tracing.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_out_right,R.anim.slide_in_right);
    }

    private void startAnim() {
            //breath
            ObjectAnimator animator = ObjectAnimator.ofFloat(binding.logoView, View.ALPHA, 0.0f, 1.0f);
            animator.setDuration(500);
            animator.setRepeatMode(ValueAnimator.REVERSE);
            animator.setRepeatCount(ValueAnimator.INFINITE);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.start();
            //pulse
            ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                    binding.logoView,
                    PropertyValuesHolder.ofFloat("scaleX", 1.1f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.1f));
            scaleDown.setDuration(500);

            scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
            scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

            scaleDown.start();
    }
}