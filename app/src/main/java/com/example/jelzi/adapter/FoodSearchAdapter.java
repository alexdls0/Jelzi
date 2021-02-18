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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class FoodSearchAdapter extends RecyclerView.Adapter<FoodSearchAdapter.ItemHolder>{
    private HashMap<Long,Food> dataholder;
    Iterator it ;

    public FoodSearchAdapter(HashMap<Long,Food> dataholder) {
        this.dataholder = dataholder;
        it= dataholder.entrySet().iterator();
    }

    @NonNull
    @Override
    public FoodSearchAdapter.ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food,parent,false);
        return new FoodSearchAdapter.ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodSearchAdapter.ItemHolder holder, int position) {
        int p = 0;
        while (it.hasNext()) {
            if (p == position) {
                Map.Entry pair = (Map.Entry) it.next();
                holder.tvCals.setText(""+((Food)pair.getValue()).getCals());
                break;
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