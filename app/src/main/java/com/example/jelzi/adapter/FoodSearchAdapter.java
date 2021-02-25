package com.example.jelzi.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.jelzi.R;
import com.example.jelzi.interfaces.OnFoodClick;
import com.example.jelzi.model.Food;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

public class FoodSearchAdapter extends RecyclerView.Adapter<FoodSearchAdapter.ItemHolder>{
    private HashMap<Long,Food> dataholder;
    Iterator it ;
    OnFoodClick foodClick;

    public FoodSearchAdapter(HashMap<Long,Food> dataholder, OnFoodClick foodClick) {
        this.dataholder = dataholder;
        this.foodClick=foodClick;
    }

    @NonNull
    @Override
    public FoodSearchAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
        return new FoodSearchAdapter.ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSearchAdapter.ItemHolder holder, int position) {
        View currentView=holder.itemView;
        it= dataholder.entrySet().iterator();
        int p = 0;
        while (it.hasNext()) {
            if (p == position) {
                Map.Entry pair = (Map.Entry) it.next();
                System.out.println(position+" "+((Food)pair.getValue()).getFoodName());
                currentView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        foodClick.OnFoodClick((Food)pair.getValue());
                    }
                });
                holder.tvFoodName.setText(""+((Food)pair.getValue()).getFoodName());
            }else{
                it.next();
            }
            p++;
        }
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
            tvCals = itemView.findViewById(R.id.currentCals);
        }
    }
}