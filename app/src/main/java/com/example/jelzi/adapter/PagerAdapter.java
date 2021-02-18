package com.example.jelzi.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.jelzi.fragments.Calendar;
import com.example.jelzi.fragments.Foods;
import com.example.jelzi.fragments.Profile;

public class PagerAdapter extends FragmentStateAdapter {
    public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new Foods();
            case 1:
                return new Calendar();
            default:
                return new Profile();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
