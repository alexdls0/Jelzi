package com.example.jelzi.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.jelzi.fragments.Charts;
import com.example.jelzi.fragments.Foods;
import com.example.jelzi.fragments.Profile;

public class PagerAdapter extends FragmentPagerAdapter {
    int numTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numTabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new Foods();
            case 1:
                return new Charts();
            case 2:
                return new Profile();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numTabs;
    }
}
