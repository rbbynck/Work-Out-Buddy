package com.example.workoutbuddy;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragment_SecondIntro_3 extends Fragment {

    TextView back, done;
    ViewPager viewPager;

    public Fragment_SecondIntro_3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment__second_intro_3, container, false);
        viewPager = getActivity().findViewById(R.id.secondIntro_ViewPager);

        done = view.findViewById(R.id.secIntro_PageThreeDone);

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Exercises.class);
                startActivity(intent);
            }
        });

        back = view.findViewById(R.id.secIntro_PageThreeBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        return view;
    }

}
