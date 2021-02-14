package com.example.jelzi.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.jelzi.Login;
import com.example.jelzi.MainActivity;
import com.example.jelzi.R;
import com.example.jelzi.model.User;
import com.example.jelzi.model.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

public class Profile extends Fragment {

    private TextView tvUsername, tvAge, tvActivity, tvHeight, tvWeight, tvGender, tvObjective, tvDiet;
    private Button btLogout, btRemoveTracing;
    private FirebaseAuth mAuth;
    private final Utils utils = new Utils();
    private DatabaseReference databaseReference;
    private User user;

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        tvUsername = view.findViewById(R.id.tvUsername);
        tvAge = view.findViewById(R.id.tvAge);
        tvActivity = view.findViewById(R.id.tvActivity);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvGender = view.findViewById(R.id.tvGender);
        tvObjective = view.findViewById(R.id.tvObjective);
        tvDiet = view.findViewById(R.id.tvDiet);
        btLogout = view.findViewById(R.id.btLogout);
        btRemoveTracing = view.findViewById(R.id.btRemoveTracing);

        if(utils.isConnected(getActivity())){
            buildUser();
        }else{
            TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }

        tvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvObjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), Login.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        btRemoveTracing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (utils.isConnected(getActivity())){
                    databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild("foods")){
                                databaseReference.child("/foods").removeValue();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                    databaseReference.child("/tracing/dailyCals").removeValue();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }
        });

        return view;
    }

    private void buildUser() {
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference()
                    .child("users").child(mAuth.getCurrentUser().getUid())
                    .child("tracing");
        reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        user = snapshot.getValue(User.class);
                        Activity activity = getActivity();
                        if(activity != null){
                            fillProfileInfo();
                        }
                    }
                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void fillProfileInfo() {
        tvUsername.setText(user.getUserName());
        tvAge.setText(user.getAge()+"");
        tvHeight.setText(user.getHeight()+"");
        tvWeight.setText(user.getWeight()+"");

        if(user.isGender()){
            tvGender.setText(getString(R.string.womenButton));
        }else{
            tvGender.setText(getString(R.string.menButton));
        }

        if(user.isHighProtein()){
            tvDiet.setText(getString(R.string.highProteinDiet));
        }else{
            tvDiet.setText(getString(R.string.normalDiet));
        }

        switch (user.getObjective()){
            case 1:{
                tvObjective.setText(getString(R.string.lossWeight));
                break;
            }
            case 2: {
                tvObjective.setText(getString(R.string.maintenanceWeight));
                break;
            }
            case 3: {
                tvObjective.setText(getString(R.string.gainWeight));
                break;
            }
        }

        switch (Double.toString(user.getActivity())){
            case "1.2":{
                tvActivity.setText(getString(R.string.lowActivity));
                break;
            }
            case "1.375": {
                tvActivity.setText(getString(R.string.midActivity));
                break;
            }
            case "1.55": {
                tvActivity.setText(getString(R.string.strongActivity));
                break;
            }
            case "1.725": {
                tvActivity.setText(getString(R.string.profActivity));
                break;
            }
        }
    }
}