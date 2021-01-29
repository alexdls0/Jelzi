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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.tvCals.setText(""+dataholder.get(position).getCals());
        holder.tvFoodName.setText(""+dataholder.get(position).getFoodName());
        holder.tvCarbh.setText(""+dataholder.get(position).getCarbh());
        holder.tvProt.setText(""+dataholder.get(position).getProt());
        holder.tvFat.setText(""+dataholder.get(position).getFat());
    }

    @Override
    public int getItemCount() {
        return dataholder.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private TextView tvFoodName, tvFat, tvCarbh, tvProt, tvCals;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvFoodName = itemView.findViewById(R.id.tvFoodName);
            tvFat = itemView.findViewById(R.id.tvFat);
            tvCarbh = itemView.findViewById(R.id.tvCarbh);
            tvProt = itemView.findViewById(R.id.tvProt);
            tvCals = itemView.findViewById(R.id.tvCals);
        }
    }
}
