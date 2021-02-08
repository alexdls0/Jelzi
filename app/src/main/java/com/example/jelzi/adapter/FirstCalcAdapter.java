package com.example.jelzi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.jelzi.R;
import com.example.jelzi.interfaces.FirstCalcInterface;
import com.example.jelzi.model.User;


public class FirstCalcAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private final Context context;
    private final FirstCalcInterface firstCalcInterface;
    private int[] layouts;
    public User user;

    public FirstCalcAdapter(int[] layouts, FirstCalcInterface firstCalcInterface, Context context, User user) {
        this.user=user;
        this.layouts=layouts;
        this.firstCalcInterface=firstCalcInterface;
        this.context=context;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        System.out.println(position);
        View currentView=holder.itemView;
        if(position==0){
            //welcome
            putSelectWelcomeListeners(currentView);
        }
        if (position==1){
            //gender select
            putSelectGenderListeners(currentView);
        }
        if (position==2){
            //age select
            putSelectAgeListeners(currentView);
        }
        if (position==3){
            //height select
            putSelectHeightListeners(currentView);
        }
        if (position==4){
            //weight select
            putSelectWeightListeners(currentView);
        }
        if (position==5){
            //activity select
            putSelectActivityListeners(currentView);
        }
        if (position==6){
            //objective select
            putSelectObjectiveListeners(currentView);
        }

    }

    private void putSelectObjectiveListeners(View currentView) {
        final TextView loss= currentView.findViewById(R.id.lossWeight);
        final TextView maint= currentView.findViewById(R.id.maintenanceWeight);
        final TextView gain= currentView.findViewById(R.id.gainWeight);

        loss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedObjective(1);
                loss.setTextColor(context.getResources().getColor(R.color.colorGreen));
                maint.setTextColor(context.getResources().getColor(R.color.colorWhite));
                gain.setTextColor(context.getResources().getColor(R.color.colorWhite));
            }
        });
        maint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedObjective(2);
                loss.setTextColor(context.getResources().getColor(R.color.colorWhite));
                maint.setTextColor(context.getResources().getColor(R.color.colorGreen));
                gain.setTextColor(context.getResources().getColor(R.color.colorWhite));
            }
        });
        gain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedObjective(3);
                loss.setTextColor(context.getResources().getColor(R.color.colorWhite));
                maint.setTextColor(context.getResources().getColor(R.color.colorWhite));
                gain.setTextColor(context.getResources().getColor(R.color.colorGreen));
            }
        });
    }

    private void putSelectActivityListeners(View currentView) {
        final TextView none= currentView.findViewById(R.id.noneActivity);
        final TextView low= currentView.findViewById(R.id.lowActivity);
        final TextView mid= currentView.findViewById(R.id.midActivity);
        final TextView high= currentView.findViewById(R.id.strongActivity);
        final TextView prof= currentView.findViewById(R.id.professionalActivity);

        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedActivity(1.2);
                none.setTextColor(context.getResources().getColor(R.color.colorGreen));
                low.setTextColor(context.getResources().getColor(R.color.colorWhite));
                mid.setTextColor(context.getResources().getColor(R.color.colorWhite));
                high.setTextColor(context.getResources().getColor(R.color.colorWhite));
                prof.setTextColor(context.getResources().getColor(R.color.colorWhite));
            }
        });
        low.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedActivity(1.375);
                none.setTextColor(context.getResources().getColor(R.color.colorWhite));
                low.setTextColor(context.getResources().getColor(R.color.colorGreen));
                mid.setTextColor(context.getResources().getColor(R.color.colorWhite));
                high.setTextColor(context.getResources().getColor(R.color.colorWhite));
                prof.setTextColor(context.getResources().getColor(R.color.colorWhite));
            }
        });
        mid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedActivity(1.55);
                none.setTextColor(context.getResources().getColor(R.color.colorWhite));
                low.setTextColor(context.getResources().getColor(R.color.colorWhite));
                mid.setTextColor(context.getResources().getColor(R.color.colorGreen));
                high.setTextColor(context.getResources().getColor(R.color.colorWhite));
                prof.setTextColor(context.getResources().getColor(R.color.colorWhite));
            }
        });
        high.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedActivity(1.725);
                none.setTextColor(context.getResources().getColor(R.color.colorWhite));
                low.setTextColor(context.getResources().getColor(R.color.colorWhite));
                mid.setTextColor(context.getResources().getColor(R.color.colorWhite));
                high.setTextColor(context.getResources().getColor(R.color.colorGreen));
                prof.setTextColor(context.getResources().getColor(R.color.colorWhite));
            }
        });
        prof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedActivity(1.9);
                none.setTextColor(context.getResources().getColor(R.color.colorWhite));
                low.setTextColor(context.getResources().getColor(R.color.colorWhite));
                mid.setTextColor(context.getResources().getColor(R.color.colorWhite));
                high.setTextColor(context.getResources().getColor(R.color.colorWhite));
                prof.setTextColor(context.getResources().getColor(R.color.colorGreen));
            }
        });


    }

    private void putSelectWeightListeners(View currentView) {
        NumberPicker WeightPicker=((NumberPicker) currentView.findViewById(R.id.weightPicker));
        WeightPicker.setMinValue(30);
        WeightPicker.setMaxValue(200);
        WeightPicker.setValue(60);
        WeightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                firstCalcInterface.selectedWeight(i1);
            }
        });
    }
    private void putSelectHeightListeners(View currentView) {
        NumberPicker HeightPicker=((NumberPicker) currentView.findViewById(R.id.heightPicker));
        HeightPicker.setMinValue(50);
        HeightPicker.setMaxValue(250);
        HeightPicker.setValue(150);
        HeightPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                firstCalcInterface.selectedHeight(i1);
            }
        });
    }

    private void putSelectAgeListeners(View currentView) {
        NumberPicker AgePicker=((NumberPicker) currentView.findViewById(R.id.agePicker));
        AgePicker.setMinValue(16);
        AgePicker.setMaxValue(99);
        AgePicker.setValue(25);
        AgePicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
                firstCalcInterface.selectedAge(i1);
            }
        });
    }

    private void putSelectWelcomeListeners(View currentView) {
        ( (TextView) currentView.findViewById(R.id.welcomeText)).setText(((TextView) currentView.findViewById(R.id.welcomeText)).getText()+" "+user.userName);
    }

    private void putSelectGenderListeners(View currentView) {
        final ImageView menIcon= (ImageView)currentView.findViewById(R.id.menIcon);
        final ImageView womenIcon=(ImageView)currentView.findViewById(R.id.womenIcon);
        TextView menButton=(TextView) currentView.findViewById(R.id.menButton);
        TextView  womenButton=(TextView) currentView.findViewById(R.id.womenButton);
        System.out.println("something");
        menButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedGender(false);
                menIcon.setImageResource(R.drawable.ic_male_selected);
                womenIcon.setImageResource(R.drawable.ic_female);
            }
        });
        menIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedGender(false);
                menIcon.setImageResource(R.drawable.ic_male_selected);
                womenIcon.setImageResource(R.drawable.ic_female);
            }
        });
        womenIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedGender(true);
                menIcon.setImageResource(R.drawable.ic_male);
                womenIcon.setImageResource(R.drawable.ic_female_selected);
            }
        });
        womenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstCalcInterface.selectedGender(true);
                menIcon.setImageResource(R.drawable.ic_male);
                womenIcon.setImageResource(R.drawable.ic_female_selected);
            }
        });
    }



    @Override
    public int getItemViewType(int position) {
        return layouts[position];
    }

    @Override
    public int getItemCount() {
        return layouts.length;
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {

        public SliderViewHolder(View view) {
            super(view);
        }
    }

}
