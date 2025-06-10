package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class Activity_Exercise_Congratulations extends AppCompatActivity {

    KonfettiView konfettiView;
    Button next, doitagain;
    TextView exercise, calories, day;
    int day_workout;
    int num_exercise, current_day;
    float exercise_calories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise__congratulations);
        next = findViewById(R.id.exercise_congratulations_next_button);
        doitagain = findViewById(R.id.exercise_congratulations_doitagain_button);
        exercise = findViewById(R.id.exercise_congratulations_exercise_text);
        calories = findViewById(R.id.exercise_congratulations_calories_text);
        day = findViewById(R.id.exercise_congratulations_day_text);

        num_exercise = getIntent().getIntExtra("EXTRA_EXERCISE", 0);
        current_day = getIntent().getIntExtra("EXTRA_DAY", 0);
        exercise_calories = getIntent().getLongExtra("EXTRA_CALORIES", 0);

        exercise.setText("" + num_exercise);
        day.setText("DAY " + current_day);
        calories.setText("" + exercise_calories);

        day_workout = current_day;

        doitagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_Exercise_Plan_Lose_Day_Exercise.class);
                intent.putExtra("EXTRA_DAY", current_day);
                startActivity(intent);
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Activity_Exercise_Plan.class));
            }
        });

        konfettiView = findViewById(R.id.viewKonfetti);

        konfettiView.build()
                .addColors(Color.rgb(168, 100, 253), Color.rgb(41, 205, 255), Color.rgb(120, 255, 68), Color.rgb(255, 113, 141),
                        Color.rgb(253, 255, 106))
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(3000L)
                .addShapes(Shape.RECT)
                .addSizes(new Size(12, 16))
                .setPosition(konfettiView.getX() + konfettiView.getWidth() / 2, konfettiView.getY() + konfettiView.getHeight() / 3)
                .streamFor(300, 5000L);

    }


}
