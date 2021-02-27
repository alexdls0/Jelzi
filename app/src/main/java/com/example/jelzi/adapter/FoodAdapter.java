package com.example.jelzi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jelzi.R;
import com.example.jelzi.model.Food;

import java.util.ArrayList;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ItemHolder>{
    private ArrayList<Food> dataholder;

    public FoodAdapter(ArrayList<Food> dataholder) {
        this.dataholder = dataholder;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_macros,parent,false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tvCals.setText(dataholder.get(position).getCals()+" cals");
        holder.tvFoodName.setText(dataholder.get(position).getFoodName());
        holder.tvServingSize.setText(dataholder.get(position).getCals()+"g");
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvFoodName, tvServingSize, tvCals;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvServingSize = itemView.findViewById(R.id.tvServingSize);
            tvCals = itemView.findViewById(R.id.tvCalsItem);
        }
    }
}
