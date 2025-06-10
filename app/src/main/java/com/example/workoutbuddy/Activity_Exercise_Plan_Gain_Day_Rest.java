package com.example.workoutbuddy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class Activity_Exercise_Plan_Gain_Day_Rest extends AppCompatActivity {

    TextView day_text;
    Button done_rest;
    DatabaseHelper db;
    int day, next_day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise__plan__gain__day_rest);

        Toolbar toolbar = findViewById(R.id.toolBar_exercise_plan_gain_day_rest);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        db = new DatabaseHelper(this);
        day = getIntent().getIntExtra("EXTRA_DAY", 0);
        next_day = day + 1;

        day_text = findViewById(R.id.exercise_plan_gain_day_rest_day);
        day_text.setText("Day " + day);
        done_rest = findViewById(R.id.exercise_plan_gain_day_rest_done);
        done_rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.viewDayActiveWorkoutPlan() == day) {
                    db.updateWorkOutPlan(day, "Done", 0, 100);
                    db.updateWorkOutPlan(next_day, "Active", 0, 0);
                }

                startActivity(new Intent(getApplicationContext(), Activity_Exercise_Plan.class));
            }
        });

    }
}
