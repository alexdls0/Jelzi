package com.example.jelzi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.sip.SipAudioCall;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.jelzi.databinding.ActivitySearchFoodBinding;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.CompactRecipe;
import com.fatsecret.platform.model.Food;
import com.fatsecret.platform.model.Serving;
import com.fatsecret.platform.services.FatsecretService;
import com.fatsecret.platform.model.Recipe;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.Request;
import com.fatsecret.platform.services.android.ResponseListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFoodActivity extends AppCompatActivity {

    private ActivitySearchFoodBinding binding;
    private String key = "209fb171b4ae4144aed8c840da778e78";
    private String secret = "401c46262c7b4ac7abd52dd91aa84285";
    private Request req;
    private FatsecretService fatsecretService;
    private RequestQueue requestQueue;
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
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchFood(binding.searchInput.getQuery().toString());
                return false;
            }
        });
    }

    private void searchFood(String query) {
        System.out.println("wekfnsodkfndafa");
        requestQueue = Volley.newRequestQueue(this);
        Listener listener = new Listener();
        req = new Request(key, secret, listener);
        fatsecretService= new FatsecretService(key,secret);
        //This response contains the list of food items at zeroth page for your query
        //req.getFoods(requestQueue, query,0);

        //This response contains the list of food items at page number 3 for your query
        //If total results are less, then this response will have empty list of the food items
        //req.getFoods(requestQueue, query, 3);
        req.getFood(requestQueue,34503l);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
    }

    class Listener implements ResponseListener {
        @Override
        public void onFoodListRespone(Response<CompactFood> response) {
            System.out.println("TOTAL FOOD ITEMS: " + response.getTotalResults());

            List<CompactFood> foods = response.getResults();
            //This list contains summary information about the food items
            System.out.println("=========FOODS============");
            for (CompactFood food: foods) {

            }
        }

        @Override
        public void onFoodResponse(Food food) {
            System.out.println("FOOD NAME: " + food.getName());
            ArrayList<Serving> servings= (ArrayList<Serving>) food.getServings();

                System.out.println("desc: "+servings.get(4).getServingDescription()
                        +" calories: "+servings.get(4).getCalories()
                        +" protein: "+servings.get(4).getProtein()
                        +" carbs: "+servings.get(4).getCarbohydrate()
                        +" fats: "+servings.get(4).getFat());

        }

    }
}