package com.example.jelzi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.jelzi.databinding.ActivityFoodInsertBinding;
import com.example.jelzi.model.Food;
import com.example.jelzi.model.Utils;
import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.model.Serving;
import com.fatsecret.platform.services.Response;
import com.fatsecret.platform.services.android.Request;
import com.fatsecret.platform.services.android.ResponseListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class FoodInsertActivity extends AppCompatActivity {
    private ActivityFoodInsertBinding binding;
    private Food food;
    private String key = "209fb171b4ae4144aed8c840da778e78";
    private String secret = "401c46262c7b4ac7abd52dd91aa84285";
    private Request req;
    private RequestQueue requestQueue;
    private List<Serving>servings;
    int calsSave=0,servingSave=0;
    double protSave,fatsSave,carbsSave;
    private FirebaseAuth mAuth;
    private Utils utils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFoodInsertBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        food = getIntent().getParcelableExtra("food");
        init();
        getFood();
    }

    private void getFood() {
        requestQueue = Volley.newRequestQueue(this);
        FoodInsertActivity.Listener listener = new FoodInsertActivity.Listener();
        req = new Request(key, secret, listener);
        req.getFood(requestQueue,Long.valueOf(food.getFoodId()));
    }

    private void init() {
        utils=new Utils();
        binding.FoodName.setText(food.getFoodName());
        binding.Cals.setText(food.getCals()+" cals");
        binding.CarbsValue.setText(food.getCarbh()+"g");
        binding.FatsValue.setText(food.getFat()+"g");
        binding.ProtValue.setText(food.getProt()+"g");
        binding.servingValue.setSelection(binding.servingValue.getText().length());
        binding.BtAddFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (food.getServing()>1)
                    addFood();
            }
        });
        binding.servingValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                binding.servingValue.removeTextChangedListener(this);
                recalcValues(editable.toString());
                binding.servingValue.setSelection(binding.servingValue.getText().length());
                binding.servingValue.addTextChangedListener(this);
            }
        });
    }

    private void saveValues() {
        calsSave=food.getCals();
        servingSave=food.getServing();
        protSave=food.getProt();
        carbsSave=food.getCarbh();
        fatsSave=food.getFat();
    }

    private void putValues(){
        binding.Cals.setText(food.getCals()+" cals");
        binding.CarbsValue.setText(food.getCarbh()+"g");
        binding.FatsValue.setText(food.getFat()+"g");
        binding.ProtValue.setText(food.getProt()+"g");
        binding.servingValue.setText(food.getServing()+"");
        binding.servingValue.setSelection(binding.servingValue.getText().length());

    }
    private void recalcValues(String serving) {
        if(serving.equals("0") || serving.equals("") )
            serving="1";
        //recalc cals
        food.setCals((calsSave*Integer.parseInt(serving))/servingSave);
        food.setServing(Integer.parseInt(serving));
        //recalc macros
        food.setProt((int)(protSave*food.getCals())/calsSave);
        food.setCarbh((int)(carbsSave*food.getCals())/calsSave);
        food.setFat((int)(fatsSave*food.getCals())/calsSave);
        putValues();
    }

    public void addFood(){
        mAuth = FirebaseAuth.getInstance();;
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(mAuth.getCurrentUser().getUid())
                .child("foodEntries")
                .child(utils.getYearMonth())
                .child(utils.getDay())
                .child(utils.getDayMoment())
                .push();
        databaseReference.setValue(food);

        this.finish();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
    }



    class Listener implements ResponseListener {
        @Override
        public void onFoodListRespone(Response<CompactFood> response) {

        }

        @Override
        public void onFoodResponse(com.fatsecret.platform.model.Food foodSearched) {
            servings=foodSearched.getServings();
            Serving serving=null;
            for (Serving servingSave:servings) {
                if (servingSave!=null)
                    serving=servingSave;
            }
            System.out.println("Calories "+serving.getCalories()+" amount "+serving.getMetricServingAmount()+" serving metric unit "+serving.getMetricServingUnit());
            food.setCals(serving.getCalories().intValue());
            food.setFat(serving.getFat().intValue());
            food.setCarbh(serving.getCarbohydrate().intValue());
            food.setProt(serving.getProtein().intValue());
            food.setServing(serving.getMetricServingAmount().intValue());
            saveValues();
            putValues();
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        overridePendingTransition(R.anim.slide_down_in,R.anim.slide_down_out);
    }

}
