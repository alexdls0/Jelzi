package com.example.jelzi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jelzi.R;
import com.example.jelzi.interfaces.OnDayMomentClick;
import com.example.jelzi.model.DayMoment;
import com.example.jelzi.model.Food;

import java.util.ArrayList;

public class DayMomentsAdapter extends RecyclerView.Adapter<DayMomentsAdapter.ItemHolder> {

    private ArrayList<DayMoment> dayMoments;
    private OnDayMomentClick onDayMomentClick;
    public DayMomentsAdapter(ArrayList<DayMoment> dayMoments, OnDayMomentClick onDayMomentClick) {
        this.dayMoments=dayMoments;
        this.onDayMomentClick=onDayMomentClick;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_block,parent,false);
        return new DayMomentsAdapter.ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        View currentView=holder.itemView;
        currentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDayMomentClick.OnFoodClick(dayMoments.get(position));
            }
        });
        holder.dayMoment.setText(""+dayMoments.get(position).getName());
        holder.calsTotal.setText(dayMoments.get(position).getCals()+" cals");
    }

    @Override
    public int getItemCount() {
        return dayMoments.size();
    }
    public void putFoods(DayMoment dayMoment, ArrayList<Food> foods){
        for (DayMoment moment:dayMoments) {
            if (moment.equals(dayMoment)){
                moment.foods=foods;
                System.out.println("moment actualizado "+moment.toString());
            }
        }
        notifyDataSetChanged();
    }
    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView dayMoment, calsTotal;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            dayMoment=itemView.findViewById(R.id.dayMoment);
            calsTotal=itemView.findViewById(R.id.calsTotal);
        }
    }
}
