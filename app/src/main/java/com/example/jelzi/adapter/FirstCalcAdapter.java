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
            //gender select
            putSelectAgeListeners(currentView);
        }


    }

    private void putSelectAgeListeners(View currentView) {
        NumberPicker AgePicker=((NumberPicker) currentView.findViewById(R.id.agePicker));
        AgePicker.setMinValue(1);
        AgePicker.setMaxValue(99);
    }

    private void putSelectWelcomeListeners(View currentView) {
        ( (TextView) currentView.findViewById(R.id.welcomeText)).setText(((TextView) currentView.findViewById(R.id.welcomeText)).getText()+" "+user.userName);
    }

    private void putSelectGenderListeners(View currentView) {
        final ImageView menIcon= (ImageView)currentView.findViewById(R.id.menIcon);
        final ImageView womenIcon=(ImageView)currentView.findViewById(R.id.womenIcon);
        TextView menButton=(TextView) currentView.findViewById(R.id.menButton);
        TextView  womenButton=(TextView) currentView.findViewById(R.id.womenButton);

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
