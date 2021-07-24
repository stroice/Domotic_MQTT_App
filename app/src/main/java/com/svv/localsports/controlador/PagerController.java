package com.svv.localsports.controlador;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PagerController extends FragmentPagerAdapter {
    int numoftabs;

    public PagerController(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.numoftabs = behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new temperature();
            case 1:
                return new open_close();
            case 2:
                return new air_quality();
            case 3:
                return new map();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
