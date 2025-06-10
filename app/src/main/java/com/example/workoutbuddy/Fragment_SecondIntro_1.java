package com.example.workoutbuddy;


import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class Fragment_SecondIntro_1 extends Fragment {

    TextView next, desc;
    ViewPager viewPager;
    int bmi;

    public Fragment_SecondIntro_1() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_fragment__second_intro_1, container, false);
        viewPager = getActivity().findViewById(R.id.secondIntro_ViewPager);

        desc = view.findViewById(R.id.secIntro_PageOneDescription);

        DatabaseHelper db = new DatabaseHelper(getActivity());
        Cursor cursor = db.viewBMI();
        while (cursor.moveToNext()) {
            bmi = cursor.getInt(0);
            if (bmi < 18.5) {
                desc.setText(R.string.bmi1);
            } else if (bmi >= 18.5 && bmi >= 24.9) {
                desc.setText(R.string.bmi2);
            } else if (bmi >= 25 && bmi >= 29.9) {
                desc.setText(R.string.bmi3);
            } else if (bmi > 30) {
                desc.setText(R.string.bmi4);
            }
        }


        next = view.findViewById(R.id.secIntro_PageOneNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        return view;
    }

}
