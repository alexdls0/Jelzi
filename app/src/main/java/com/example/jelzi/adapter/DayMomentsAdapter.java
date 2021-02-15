package com.example.jelzi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jelzi.R;
import com.example.jelzi.model.DayMoment;

import java.util.ArrayList;

public class DayMomentsAdapter extends RecyclerView.Adapter<DayMomentsAdapter.ItemHolder> {

    private ArrayList<DayMoment> dayMoments;

    public DayMomentsAdapter(ArrayList<DayMoment> dayMoments) {
        this.dayMoments=dayMoments;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_block,parent,false);
        return new DayMomentsAdapter.ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.dayMoment.setText(""+dayMoments.get(position).getName());
        holder.calsTotal.setText(dayMoments.get(position).getCals()+" cals");
    }

    @Override
    public int getItemCount() {
        return dayMoments.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView dayMoment, calsTotal;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            dayMoment=itemView.findViewById(R.id.dayMoment);
            calsTotal=itemView.findViewById(R.id.calsTotal);
        }
    }
}
