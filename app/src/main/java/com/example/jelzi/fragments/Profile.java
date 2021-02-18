package com.example.jelzi.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.jelzi.Login;
import com.example.jelzi.MainActivity;
import com.example.jelzi.R;
import com.example.jelzi.controllers.CaloryIntakeController;
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
    private CardView cvUsername, cvAge, cvActivity, cvHeight, cvWeight,
    cvGender, cvObjective, cvDiet;
    private CaloryIntakeController caloryIntakeController= new CaloryIntakeController();

    public Profile() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        view.setBackgroundColor(getResources().getColor(R.color.colorBackground));
        mAuth = FirebaseAuth.getInstance();
        tvUsername = view.findViewById(R.id.tvUsername);
        tvAge = view.findViewById(R.id.tvAge);
        tvActivity = view.findViewById(R.id.tvActivity);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvWeight = view.findViewById(R.id.tvWeight);
        tvGender = view.findViewById(R.id.tvGender);
        tvObjective = view.findViewById(R.id.tvObjective);
        tvDiet = view.findViewById(R.id.tvDiet);
        cvUsername = view.findViewById(R.id.cvUsername);
        cvAge = view.findViewById(R.id.cvAge);
        cvActivity = view.findViewById(R.id.cvActivity);
        cvHeight = view.findViewById(R.id.cvHeight);
        cvWeight = view.findViewById(R.id.cvWeight);
        cvGender = view.findViewById(R.id.cvGender);
        cvObjective = view.findViewById(R.id.cvObjective);
        cvDiet = view.findViewById(R.id.cvDiet);
        btLogout = view.findViewById(R.id.btLogout);
        btRemoveTracing = view.findViewById(R.id.btRemoveTracing);

        if(utils.isConnected(getActivity())){
            buildUser();
        }else{
            TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
        }

        cvUsername.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        cvAge.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        cvActivity.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        cvHeight.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        cvWeight.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        cvGender.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        cvObjective.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));
        cvDiet.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.transparent));

        cvUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_username, null);
                final EditText username = dialogView.findViewById(R.id.etProfileDialogUsername);
                username.setText(user.getUserName());
                Button btChange = dialogView.findViewById(R.id.btChange);

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();

                btChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = username.getText().toString();
                        if(!name.isEmpty() && utils.checkUsername(name)){
                            if (utils.isConnected(getActivity())){
                                user.setUserName(name);
                                saveTracingFirebase();
                                dialog.dismiss();
                            }else{
                                TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        }else{
                            TastyToast.makeText(getActivity(), getString(R.string.usernameerror), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                });
            }
        });

        cvAge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_numbs, null);

                NumberPicker AgePicker=((NumberPicker) dialogView.findViewById(R.id.numberPicker));
                AgePicker.setMinValue(16);
                AgePicker.setMaxValue(99);
                AgePicker.setValue(user.getAge());
                AgePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        user.setAge(i1);
                    }
                });

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (utils.isConnected(getActivity())){
                            saveTracingFirebase();
                        }else{
                            TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                });
            }
        });

        cvActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_activity, null);

                final TextView none= dialogView.findViewById(R.id.noneActivity);
                final TextView low= dialogView.findViewById(R.id.lowActivity);
                final TextView mid= dialogView.findViewById(R.id.midActivity);
                final TextView high= dialogView.findViewById(R.id.strongActivity);
                final TextView prof= dialogView.findViewById(R.id.professionalActivity);

                none.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setActivity(1.2);
                        none.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                        low.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        mid.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        high.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        prof.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                });
                low.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setActivity(1.375);
                        none.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        low.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                        mid.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        high.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        prof.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                });
                mid.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setActivity(1.55);
                        none.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        low.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        mid.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                        high.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        prof.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                });
                high.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setActivity(1.725);
                        none.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        low.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        mid.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        high.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                        prof.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                });
                prof.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setActivity(1.9);
                        none.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        low.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        mid.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        high.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        prof.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                    }
                });

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (utils.isConnected(getActivity())){
                            saveTracingFirebase();
                        }else{
                            TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                });
            }
        });

        cvHeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_numbs, null);

                NumberPicker HeightPicker=((NumberPicker) dialogView.findViewById(R.id.numberPicker));
                HeightPicker.setMinValue(50);
                HeightPicker.setMaxValue(250);
                HeightPicker.setValue(user.getHeight());
                HeightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        user.setHeight(i1);
                    }
                });

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (utils.isConnected(getActivity())){
                            saveTracingFirebase();
                        }else{
                            TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                });
            }
        });

        cvWeight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_numbs, null);

                NumberPicker WeightPicker=((NumberPicker) dialogView.findViewById(R.id.numberPicker));
                WeightPicker.setMinValue(30);
                WeightPicker.setMaxValue(200);
                WeightPicker.setValue(user.getWeight());
                WeightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                        user.setWeight(i1);
                    }
                });

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (utils.isConnected(getActivity())){
                            saveTracingFirebase();
                        }else{
                            TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                });
            }
        });

        cvGender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_gender, null);

                final ImageView menIcon= (ImageView)dialogView.findViewById(R.id.menIcon);
                final ImageView womenIcon=(ImageView)dialogView.findViewById(R.id.womenIcon);
                TextView menButton=(TextView) dialogView.findViewById(R.id.menButton);
                TextView  womenButton=(TextView) dialogView.findViewById(R.id.womenButton);
                menButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setGender(false);
                        menIcon.setImageResource(R.drawable.ic_male_selected);
                        womenIcon.setImageResource(R.drawable.ic_female);
                    }
                });
                menIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setGender(false);
                        menIcon.setImageResource(R.drawable.ic_male_selected);
                        womenIcon.setImageResource(R.drawable.ic_female);
                    }
                });
                womenIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setGender(true);
                        menIcon.setImageResource(R.drawable.ic_male);
                        womenIcon.setImageResource(R.drawable.ic_female_selected);
                    }
                });
                womenButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setGender(true);
                        menIcon.setImageResource(R.drawable.ic_male);
                        womenIcon.setImageResource(R.drawable.ic_female_selected);
                    }
                });

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (utils.isConnected(getActivity())){
                            saveTracingFirebase();
                        }else{
                            TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                });
            }
        });

        cvObjective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_objective, null);

                final TextView loss= dialogView.findViewById(R.id.lossWeight);
                final TextView maint= dialogView.findViewById(R.id.maintenanceWeight);
                final TextView gain= dialogView.findViewById(R.id.gainWeight);

                loss.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setObjective(1);
                        loss.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                        maint.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        gain.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                });
                maint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setObjective(2);
                        loss.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        maint.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                        gain.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                });
                gain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setObjective(3);
                        loss.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        maint.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        gain.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                    }
                });

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (utils.isConnected(getActivity())){
                            saveTracingFirebase();
                        }else{
                            TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                });
            }
        });

        cvDiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_diet, null);

                final TextView normal= dialogView.findViewById(R.id.normalDiet);
                final TextView hightProtein= dialogView.findViewById(R.id.highProteinDiet);

                normal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setHighProtein(false);
                        normal.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                        hightProtein.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                    }
                });
                hightProtein.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        user.setHighProtein(true);
                        normal.setTextColor(getContext().getResources().getColor(R.color.colorWhite));
                        hightProtein.setTextColor(getContext().getResources().getColor(R.color.colorGreen));
                    }
                });

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        if (utils.isConnected(getActivity())){
                            saveTracingFirebase();
                        }else{
                            TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }
                });
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
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View dialogView = getLayoutInflater().inflate(R.layout.dialog_profile_remove_tracing, null);
                Button btRemove = dialogView.findViewById(R.id.btRemove);
                Button btBack = dialogView.findViewById(R.id.btBack);

                builder.setView(dialogView);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                btRemove.setOnClickListener(new View.OnClickListener() {
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

                btBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
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
                tvActivity.setText(getString(R.string.strongActivity));
                break;
            }
            case "1.9": {
                tvActivity.setText(getString(R.string.profActivity));
                break;
            }
        }
    }

    public void updateUser() {
        user.tmb =caloryIntakeController.calcTMB(user.height,user.weight,user.age,user.gender);
        System.out.println("tmb: "+user.tmb);
        user.dailyCals=caloryIntakeController.calcDailyCalsIntake(user.tmb,user.activity,user.objective);;
        System.out.println("dailyCals: "+user.dailyCals);
        System.out.println("macros: "+caloryIntakeController.calcMacros(user.dailyCals,user.weight,user.isHighProtein()));
        System.out.println(user);
        saveTracingFirebase();
    }

    private void saveTracingFirebase() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()).child("tracing");
        databaseReference.setValue(user);
    }
}