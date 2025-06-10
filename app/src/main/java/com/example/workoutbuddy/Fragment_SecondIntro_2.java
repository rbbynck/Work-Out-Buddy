package com.example.workoutbuddy;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class Fragment_SecondIntro_2 extends Fragment {

    TextView next, back, desc;
    ViewPager viewPager;
    int bmi;


    public Fragment_SecondIntro_2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__second_intro_2 , container, false);
        viewPager = getActivity().findViewById(R.id.secondIntro_ViewPager);

        desc = view.findViewById(R.id.secIntro_PageTwoDescription);

        DatabaseHelper db = new DatabaseHelper(getActivity());
        Cursor cursor = db.viewBMI();
        while (cursor.moveToNext()) {
            bmi = cursor.getInt(0);
            if (bmi < 18.5) {
                desc.setText(R.string.exerciseplan1);
            } else if (bmi >= 18.5 && bmi >= 24.9) {
                desc.setText(R.string.exerciseplan2);
            } else if (bmi >= 25 && bmi >= 29.9) {
                desc.setText(R.string.exerciseplan3);
            } else if (bmi > 30) {
                desc.setText(R.string.exerciseplan4);
            }
        }

        next = view.findViewById(R.id.secIntro_PageTwoNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        back = view.findViewById(R.id.secIntro_PageTwoBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        return view;
    }

}
