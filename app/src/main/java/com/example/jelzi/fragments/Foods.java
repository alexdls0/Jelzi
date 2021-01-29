package com.example.jelzi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jelzi.R;
import com.example.jelzi.adapter.FoodAdapter;
import com.example.jelzi.model.Food;

import java.util.ArrayList;

public class Foods extends Fragment {

    private RecyclerView rvFoods;
    private ArrayList<Food> foodList;

    public Foods() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_foods, container, false);
        rvFoods = view.findViewById(R.id.rvFoods);
        rvFoods.setLayoutManager(new LinearLayoutManager(getContext()));

        foodList = new ArrayList<>();

        Food comida1 = new Food("Pizza", 32, 12, 28, 160.55 );
        foodList.add(comida1);
        Food comida2 = new Food("Hamburguesa", 122, 97, 34, 6743 );
        foodList.add(comida2);
        Food comida3 = new Food("Ensalada", 156, 43, 89, 2434.55 );
        foodList.add(comida3);
        foodList.add(comida3);
        foodList.add(comida3);
        foodList.add(comida3);
        //Rellenar ese arraylist con las comidas del día actual que tenga el usuario
        rvFoods.setAdapter(new FoodAdapter(foodList));

        return view;
    }
}