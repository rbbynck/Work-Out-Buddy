package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

public class Second_Intro extends AppCompatActivity {

    ViewPager viewPager;
    PageAdapter_SecondIntro adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second__intro);

        viewPager = findViewById(R.id.secondIntro_ViewPager);

        adapter = new PageAdapter_SecondIntro(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
    }
}
