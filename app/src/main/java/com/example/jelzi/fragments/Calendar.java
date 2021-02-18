package com.example.jelzi.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jelzi.R;
import com.example.jelzi.Register;
import com.google.android.material.snackbar.Snackbar;
import com.sdsmdg.tastytoast.TastyToast;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.HashMap;
import java.util.Map;

public class Calendar extends Fragment implements OnNavigationButtonClickedListener {

    private CustomCalendar customCalendar;

    public Calendar() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        customCalendar = view.findViewById(R.id.CustomCalendar);

        //1.-Un array de strings con AÑO/MES/DIA, de el usuario

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
                //4.-Se pasa la fecha al intent historial, cuya misión es cargar los datos
                //de ese día
                String date = selectedDate.get(java.util.Calendar.DAY_OF_MONTH)
                        + "/" + (selectedDate.get(java.util.Calendar.MONTH)+ 1)
                        + "/" + selectedDate.get(java.util.Calendar.YEAR);
                Snackbar.make(customCalendar, date, Snackbar.LENGTH_LONG).show();
            }
        });

        //2.-Pintar el mes actual con su info
        HashMap<Integer, Object> dateHashMap = new HashMap<>();
        java.util.Calendar calendar = java.util.Calendar.getInstance();
        dateHashMap.put(1, "withdata");
        dateHashMap.put(3, "withdata");
        dateHashMap.put(12, "withdata");
        dateHashMap.put(23, "withdata");
        customCalendar.setDate(calendar, dateHashMap);

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, this);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, this);

        return view;
    }

    @Override
    public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, java.util.Calendar newMonth) {
        Map<Integer, Object>[] arr = new Map[2];
        //3.-En este listener al pulsar el botón se comprueba si coincide con alguna de esas
        //fechas, en cuyo caso, se crearia el hashMap con los días
        TastyToast.makeText(getContext(), newMonth.get(java.util.Calendar.YEAR)+"", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
        switch(newMonth.get(java.util.Calendar.MONTH)) {
            case java.util.Calendar.JANUARY:
                arr[0] = new HashMap<>(); //This is the map linking a date to its description
                arr[0].put(3, "withdata");
                arr[0].put(6, "withdata");
                arr[0].put(21, "withdata");
                arr[0].put(24, "withdata");
                arr[1] = null; //Optional: This is the map linking a date to its tag.
                break;
            case java.util.Calendar.FEBRUARY:
                arr[0] = new HashMap<>(); //This is the map linking a date to its description
                arr[0].put(1, "withdata");
                arr[0].put(3, "withdata");
                arr[0].put(12, "withdata");
                arr[0].put(23, "withdata");
                break;
            case java.util.Calendar.MARCH:
                arr[0] = new HashMap<>();
                arr[0].put(5, "withdata");
                arr[0].put(10, "withdata");
                arr[0].put(19, "withdata");
                break;
            default:
                arr[0] = new HashMap<>();
                break;
        }
        return arr;
    }
}