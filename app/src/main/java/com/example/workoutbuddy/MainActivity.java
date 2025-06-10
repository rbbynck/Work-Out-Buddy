package com.example.workoutbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    BarChart barChartHeight, barChartWeight;
    ArrayList<BarEntry> entriesHeight, entriesWeight;
    BarDataSet bardataset_height, bardataset_weight;
    ArrayList<String> labels;
    DatabaseHelper db;
    Cursor cursor;
    int total_weekNo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        total_weekNo = db.viewWeeklyProgress_WeekNo();
        Toast.makeText(this, "WEEK: " + Calendar.WEEK_OF_MONTH, Toast.LENGTH_LONG).show();
        bottomNav();
        barChartsHeight();
        barChartsWeight();
    }

    public void bottomNav() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.progress);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.progress:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.exercises:
                        startActivity(new Intent(getApplicationContext(), Exercises.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.schedule:
                        startActivity(new Intent(getApplicationContext(), Schedule.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.profle:
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return true;
            }
        });
    }

    public void barChartsHeight() {
        barChartHeight = findViewById(R.id.exercise_workout_progress_barcharts_height);
        entriesHeight = new ArrayList<>();
        bardataset_height = new BarDataSet(entriesHeight, "Cells");
        labels = new ArrayList<String>();

        for (int i = 0; i < total_weekNo; i++) {
            entriesHeight.add(new BarEntry(db.viewWeeklyProgress_Height(i+1), i));
            labels.add("Week " + (i + 1));
        }

        BarData data = new BarData(labels, bardataset_height);
        barChartHeight.setData(data); // set the data and list of labels into chart
        bardataset_height.setColors(ColorTemplate.JOYFUL_COLORS);
        bardataset_height.setBarSpacePercent(50f);
        barChartHeight.setScaleYEnabled(false);
        barChartHeight.setScaleXEnabled(false);
        barChartHeight.setDescription("");
        barChartHeight.setDoubleTapToZoomEnabled(false);
        barChartHeight.getAxisRight().setDrawGridLines(false);
        barChartHeight.getAxisLeft().setDrawGridLines(false);
        barChartHeight.getXAxis().setDrawGridLines(false);
        barChartHeight.getLegend().setEnabled(false);
        barChartHeight.getAxisRight().setStartAtZero(true);
        barChartHeight.getAxisLeft().setStartAtZero(true);
        barChartHeight.setDrawValueAboveBar(false);
    }




    public void barChartsWeight() {
        barChartWeight = findViewById(R.id.exercise_workout_progress_barcharts_weight);
        entriesWeight = new ArrayList<>();
        labels = new ArrayList<String>();
        bardataset_weight = new BarDataSet(entriesWeight, "Cells");

        for (int i = 0; i < total_weekNo; i++) {
            entriesWeight.add(new BarEntry(db.viewWeeklyProgress_Weight(i+1), i));
            labels.add("Week " + (i + 1));
        }

        BarData data = new BarData(labels, bardataset_weight);
        barChartWeight.setData(data); // set the data and list of labels into chart
        bardataset_weight.setColors(ColorTemplate.JOYFUL_COLORS);
        bardataset_weight.setBarSpacePercent(50f);
        barChartWeight.setScaleYEnabled(false);
        barChartWeight.setDescription("");
        barChartWeight.setScaleXEnabled(false);
        barChartWeight.setDoubleTapToZoomEnabled(false);
        barChartWeight.getAxisRight().setDrawGridLines(false);
        barChartWeight.getAxisLeft().setDrawGridLines(false);
        barChartWeight.getXAxis().setDrawGridLines(false);
        barChartWeight.getLegend().setEnabled(false);
        barChartWeight.getAxisRight().setStartAtZero(true);
        barChartWeight.getAxisLeft().setStartAtZero(true);
        barChartWeight.setDrawValueAboveBar(false);
    }


}


