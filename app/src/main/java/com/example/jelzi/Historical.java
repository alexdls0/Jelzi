package com.example.jelzi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.TextView;

import com.example.jelzi.model.Utils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

public class Historical extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView calsDaily, tvFat, tvCarbh, tvProt;
    private final Utils utils = new Utils();
    private int totalCals=0, totalFat=0, totalCarbh=0, totalProt=0;
    private String proteins = "prot", carbs = "carbs", fats = "fats", calories = "cals";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical);
        init();
    }

    private void init() {
        mAuth=FirebaseAuth.getInstance();
        calsDaily = findViewById(R.id.calsDaily);
        tvFat = findViewById(R.id.tvFat);
        tvCarbh = findViewById(R.id.tvCarbh);
        tvProt = findViewById(R.id.tvProt);

        if (utils.isConnected(Historical.this)){
            Intent intent = getIntent();
            String date = intent.getExtras().getString("date");
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
                    fillWithData();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }else{
            TastyToast.makeText(Historical.this, getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }
    }

    private void fillWithData() {
        calsDaily.setText(Integer.toString(totalCals));
        tvCarbh.setText(totalCarbh+"g");
        tvFat.setText(totalFat+"g");
        tvProt.setText(totalProt+"g");
    }
}