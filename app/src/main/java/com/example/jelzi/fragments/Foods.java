package com.example.jelzi.fragments;

import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jelzi.R;
import com.example.jelzi.adapter.FoodAdapter;
import com.example.jelzi.model.Food;

import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Foods extends Fragment {

    private RecyclerView rvFoods;
    private ArrayList<Food> foodList;
    private FoodAdapter foodAdapter;

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
        foodAdapter = new FoodAdapter(foodList);
        rvFoods.setAdapter(foodAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(rvFoods);

        return view;
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            //Borrar adecuadamente la comida
            foodList.remove(position);
            foodAdapter.notifyItemRemoved(position);
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder
                viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeRightBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed))
                    .addSwipeRightActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    };
}