package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ActionBar;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class Activity_Workout_History extends AppCompatActivity {

    TextView month_textview;
    ImageView next, prev;
    int month, day;
    String[] month_text = {"JANUARY", "FEBRUARY", "MARCH", "APRIL", "MAY", "JUNE", "JULY", "AUGUST", "SEPTEMBER", "OCTOBER", "NOVEMBER", "DECEMBER"};
    int[] last_day_of_month = {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    float[][] calories = new float[12][31];
    BarChart barChart;
    ArrayList<BarEntry> entries;
    BarDataSet bardataset;
    ArrayList<String> labels;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__workout__history);
        setTitle("");
        toolBar();

        db = new DatabaseHelper(this);
        barChart = findViewById(R.id.exercise_workout_history_barcharts_month);
        entries = new ArrayList<>();
        bardataset = new BarDataSet(entries, "Cells");
        labels = new ArrayList<String>();


        Cursor cursor = db.viewWorkoutHistory(); // LAGYAN NG LAHMAN FOR COMPARISONS
        while (cursor.moveToNext()) {
            for (int i = 0; i < month_text.length; i++) {
                for (int c = 0; c < last_day_of_month[i]; c++) {
                    if (cursor.getInt(0) == (i + 1)) {
                        if (cursor.getInt(1) == (c + 1)) {
                            calories[i][c] = cursor.getFloat(3);
                        }
                    }
                }
            }
        }

        Calendar calendar = Calendar.getInstance();

        month = calendar.get(Calendar.MONTH) + 1;

        barCharts(month - 1);

        month_textview = findViewById(R.id.exercise_workout_history_text_month);
        month_textview.setText(month_text[month - 1]);
        next = findViewById(R.id.exercise_workout_history_next_month);
        prev = findViewById(R.id.exercise_workout_history_prev_month);


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month++;
                month_textview.setText(month_text[month - 1]);

                if (month == 12) {
                    next.setVisibility(View.INVISIBLE);
                }
                if (month != 1) {
                    prev.setVisibility(View.VISIBLE);
                }

                barCharts(month - 1);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                month--;
                month_textview.setText(month_text[month - 1]);

                if (month == 1) {
                    prev.setVisibility(View.INVISIBLE);
                }
                if (month != 12) {
                    next.setVisibility(View.VISIBLE);
                }

                barCharts(month - 1);
            }
        });

    }

    public void barCharts(int number) {

        entries.clear();
        barChart.invalidate();
        barChart.clear();
        labels.clear();


        for (int i = 0; i < last_day_of_month[number]; i++) {
            if (calories[number][i] > 0) {
                entries.add(new BarEntry(calories[number][i], i));
            }
        }


        for (int i = 0; i < last_day_of_month[number]; i++) {
            labels.add((i + 1) + "");
        }

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        bardataset.setColors(ColorTemplate.JOYFUL_COLORS);
        bardataset.setBarSpacePercent(50f);
        barChart.setDescriptionColor(Color.WHITE);
        barChart.setScaleMinima(10f, 1f);
        barChart.setScaleYEnabled(false);
        barChart.setScaleXEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
        barChart.getAxisRight().setStartAtZero(true);
        barChart.getAxisLeft().setStartAtZero(true);
        barChart.setDrawValueAboveBar(false);
        barChart.setDescription("Month of " + month_text[number]);
    }


    public void toolBar() {
        Toolbar toolbar = findViewById(R.id.exercise_workout_history_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

}
