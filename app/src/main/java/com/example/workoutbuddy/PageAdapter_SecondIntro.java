package com.example.workoutbuddy;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter_SecondIntro extends FragmentPagerAdapter {

    public PageAdapter_SecondIntro(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new Fragment_SecondIntro_1();
            case 1:
                return new Fragment_SecondIntro_2();
            case 2:
                return new Fragment_SecondIntro_3();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
