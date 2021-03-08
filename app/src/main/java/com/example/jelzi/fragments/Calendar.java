package com.example.jelzi.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jelzi.Historical;
import com.example.jelzi.R;
import com.example.jelzi.model.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.HashMap;
import java.util.Map;

public class Calendar extends Fragment implements OnNavigationButtonClickedListener {

    private CustomCalendar customCalendar;
    private FirebaseAuth mAuth;
    private TextView tvMonth;
    private TextView day0, day1, day2, day3, day4, day5, day6;
    private final Utils utils = new Utils();

    public Calendar() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        mAuth=FirebaseAuth.getInstance();
        day0 = view.findViewById(R.id.tv_day_of_week_0);
        day1 = view.findViewById(R.id.tv_day_of_week_1);
        day2 = view.findViewById(R.id.tv_day_of_week_2);
        day3 = view.findViewById(R.id.tv_day_of_week_3);
        day4 = view.findViewById(R.id.tv_day_of_week_4);
        day5 = view.findViewById(R.id.tv_day_of_week_5);
        day6 = view.findViewById(R.id.tv_day_of_week_6);
        day0.setTextColor(getResources().getColor(R.color.colorWhite));
        day1.setTextColor(getResources().getColor(R.color.colorWhite));
        day2.setTextColor(getResources().getColor(R.color.colorWhite));
        day3.setTextColor(getResources().getColor(R.color.colorWhite));
        day4.setTextColor(getResources().getColor(R.color.colorWhite));
        day5.setTextColor(getResources().getColor(R.color.colorWhite));
        day6.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMonth = view.findViewById(R.id.tv_month_year);
        tvMonth.setTypeface(null, Typeface.BOLD);
        tvMonth.setTextColor(getResources().getColor(R.color.colorWhite));
        tvMonth.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                getResources().getDimension(R.dimen.mediumSize));

        customCalendar = view.findViewById(R.id.CustomCalendar);

        HashMap<Object, Property>descHashMap = new HashMap<>();
        Property defaultProperty = new Property();
        defaultProperty.layoutResource = R.layout.default_view;
        defaultProperty.dateTextViewResource  = R.id.text_view;
        descHashMap.put("default", defaultProperty);

        Property dataProperty = new Property();
        dataProperty.layoutResource = R.layout.withdata_view;
        dataProperty.dateTextViewResource  = R.id.text_view;
        descHashMap.put("withdata", dataProperty);
        customCalendar.setMapDescToProp(descHashMap);

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, java.util.Calendar selectedDate, Object desc) {
                if (utils.isConnected(getActivity())){
                    String date = selectedDate.get(java.util.Calendar.YEAR) +"-";
                    if(selectedDate.get(java.util.Calendar.MONTH)+1 < 10){
                        date +="0";
                    }
                    date +=selectedDate.get(java.util.Calendar.MONTH)+1+ "/";
                    if(selectedDate.get(java.util.Calendar.DAY_OF_MONTH) < 10){
                        date +="0";
                    }
                    date += selectedDate.get(java.util.Calendar.DAY_OF_MONTH);

                    final String dateToCheck = date;

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid()+"/foodEntries");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.hasChild(dateToCheck)){
                                Intent i = new Intent(getActivity(), Historical.class);
                                i.putExtra("date", dateToCheck);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                }else{
                    TastyToast.makeText(getActivity(), getString(R.string.connectionlost), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }
        });

        drawInitialMonth();

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

        return view;
    }

    private void drawInitialMonth() {
        final HashMap<Integer, Object> dateHashMap = new HashMap<>();
        final java.util.Calendar calendar = java.util.Calendar.getInstance();

        String actualMonth = calendar.get(java.util.Calendar.YEAR)+"-";
        if(calendar.get(java.util.Calendar.MONTH)+1 < 10){
            actualMonth+="0";
        }
        actualMonth +=(calendar.get(java.util.Calendar.MONTH)+1);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(mAuth.getCurrentUser().getUid()+"/foodEntries/"+actualMonth);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()){
                    String key = child.getKey();
                    dateHashMap.put(Integer.parseInt(key), "withdata");
                }
                customCalendar.setDate(calendar, dateHashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, final java.util.Calendar newMonth) {
        final Map<Integer, Object>[] arr = new Map[2];
        final HashMap<Integer, Object> newDateHashMap = new HashMap<>();

        String actualMonth = newMonth.get(java.util.Calendar.YEAR)+"-";
        if(newMonth.get(java.util.Calendar.MONTH)+1 < 10){
            actualMonth+="0";
        }
        actualMonth +=(newMonth.get(java.util.Calendar.MONTH)+1);

        arr[0] = new HashMap<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users")
                .child(mAuth.getCurrentUser().getUid()+"/foodEntries/"+actualMonth);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child: dataSnapshot.getChildren()){
                    String key = child.getKey();
                    newDateHashMap.put(Integer.parseInt(key), "withdata");
                }
                customCalendar.setDate(newMonth, newDateHashMap);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        return arr;
    }
}