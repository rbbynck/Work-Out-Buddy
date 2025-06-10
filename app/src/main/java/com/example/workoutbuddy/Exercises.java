package com.example.workoutbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Exercises extends AppCompatActivity {

    LinearLayout exercise_Plan, exercise_Personalize;
    TextView exercise_gain_or_lose_title, exercise_gain_or_lose_description;
    ImageView exercise_gain_or_lose_image;
    DatabaseHelper db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercises);

        db = new DatabaseHelper(this);
        bottomNav();
        Initialization();


    }

    public void Initialization() {
        exercise_gain_or_lose_title = findViewById(R.id.exercise_lose_or_gain_text);
        exercise_gain_or_lose_description = findViewById(R.id.exercise_lose_or_gain_description);
        exercise_gain_or_lose_image = findViewById(R.id.exercise_lose_or_gain_image);

        cursor = db.viewWorkoutPlan();
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == 1) {
                exercise_gain_or_lose_title.setText("LOSE WEIGHT");
                exercise_gain_or_lose_description.setText("30 days of work-out to lose weight");
                exercise_gain_or_lose_image.setImageResource(R.drawable.ic_lose_weight);
            } else if (cursor.getInt(0) == 0){
                exercise_gain_or_lose_title.setText("GAIN WEIGHT");
                exercise_gain_or_lose_description.setText("30 days of work-out to gain weight");
                exercise_gain_or_lose_image.setImageResource(R.drawable.ic_gain_weight);
            }
        }

        exercise_Plan = findViewById(R.id.exercise_plan);
        exercise_Plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Exercises.this, Activity_Exercise_Plan.class));
            }
        });

        exercise_Personalize = findViewById(R.id.exercise_personalize_plan);
        exercise_Personalize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Exercises.this, Activity_Exercise_Personalize.class));
            }
        });
    }

    public void bottomNav() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setSelectedItemId(R.id.exercises);

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
}
