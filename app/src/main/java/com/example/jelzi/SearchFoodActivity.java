package com.example.jelzi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.jelzi.adapter.FoodSearchAdapter;
import com.example.jelzi.databinding.ActivitySearchFoodBinding;
import com.example.jelzi.interfaces.OnFoodClick;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.services.FatsecretService;

import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.Request;
import com.fatsecret.platform.services.android.ResponseListener;
import java.util.HashMap;
import java.util.List;

public class SearchFoodActivity extends AppCompatActivity implements OnFoodClick {

    private ActivitySearchFoodBinding binding;
    private String key = "209fb171b4ae4144aed8c840da778e78";
    private String secret = "401c46262c7b4ac7abd52dd91aa84285";
    private Request req;
    private FatsecretService fatsecretService;
    private RequestQueue requestQueue;
    public HashMap<Long,com.example.jelzi.model.Food> foodList;
    public  FoodSearchAdapter foodAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySearchFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        init();
    }

    private void init() {

        binding.searchInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchInput.onActionViewExpanded();
            }
        });
        binding.searchInput.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(binding.searchInput.getQuery().length()>0)
                    try {
                        searchFood(binding.searchInput.getQuery().toString());
                    }catch (RuntimeException e){
                    }

                return false;
            }
        });
        binding.rvFoodSearch.setLayoutManager(new LinearLayoutManager(this));
        foodList= new HashMap<>();
        foodAdapter= new FoodSearchAdapter(foodList,this);
        binding.rvFoodSearch.setAdapter(foodAdapter);
    }

    private void searchFood(String query) {

        requestQueue = Volley.newRequestQueue(this);
        Listener listener = new Listener();
        req = new Request(key, secret, listener);
        fatsecretService= new FatsecretService(key,secret);
        //This response contains the list of food items at zeroth page for your query
        req.getFoods(requestQueue, query,0);

        //This response contains the list of food items at page number 3 for your query
        //If total results are less, then this response will have empty list of the food items
        //req.getFoods(requestQueue, query, 3);
        //req.getFood(requestQueue,34503l);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
    }

    @Override
    public void OnFoodClick(com.example.jelzi.model.Food food) {
        Intent intent = new Intent(this, FoodInsertActivity.class);
        intent.putExtra("food",food);
        startActivity(intent);
        this.finish();
        overridePendingTransition(R.anim.slide_up_out,R.anim.slide_up_in);
    }

    class Listener implements ResponseListener {
        @Override
        public void onFoodListRespone(Response<CompactFood> response) {
            List<CompactFood> foods = response.getResults();
            //This list contains summary information about the food items
            foodList.clear();
            foodAdapter.notifyDataSetChanged();
            for (CompactFood food: foods) {
                long id= food.getId();
                com.example.jelzi.model.Food food1=new com.example.jelzi.model.Food();
                food1.setFoodId(String.valueOf(id));
                food1.setFoodName(food.getName());
                foodList.put(id,food1);
                foodAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFoodResponse(Food food) {
        }

    }

}