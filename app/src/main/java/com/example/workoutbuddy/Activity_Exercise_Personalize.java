package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Activity_Exercise_Personalize extends AppCompatActivity {

    ImageView add;
    LinearLayout parent;
    DatabaseHelper db;
    Cursor cursor;
    LinearLayout[] exercises;
    TextView[] exercise_title;
    String[] title;
    Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise__personalize);
        setTitle("");
        toolBar();

        db = new DatabaseHelper(this);
        cursor = db.viewPersonalizeExercise();
        parent = findViewById(R.id.exercise_personalize_plan_linear_layout);
        exercises = new LinearLayout[cursor.getCount()];
        exercise_title = new TextView[cursor.getCount()];
        title = new String[cursor.getCount()];
        int d = 0;
        while (cursor.moveToNext()) {
            title[d] = cursor.getString(0);
            d++;
        }
        d = 0;


        for (int i = 0; i < exercises.length; i++) {
            exercises[i] = new LinearLayout(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 150);
            lp.setMargins(0, 50, 0, 0);
            exercises[i].setLayoutParams(lp);
            exercises[i].setBackgroundResource(R.drawable.exercises_background_layout_2);
            exercises[i].setGravity(Gravity.CENTER);
            exercises[i].setOrientation(LinearLayout.VERTICAL);
            parent.addView(exercises[i]);
            //
            exercise_title[i] = new TextView(this);
            exercise_title[i].setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            exercise_title[i].setText(title[i].toUpperCase());
            exercise_title[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            exercise_title[i].setTextSize(20);
            exercise_title[i].setTypeface(Typeface.SANS_SERIF, Typeface.BOLD);
            exercises[i].addView(exercise_title[i]);
        }

        for (int i = 0; i < exercises.length; i++) {
            int p = i;
            exercises[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    intent = new Intent(Activity_Exercise_Personalize.this, Activity_Exercise_Personalize_Exercise.class);
                    intent.putExtra("EXTRA_TITLE", title[p]);
                    startActivity(intent);
                }
            });
        }

        add = findViewById(R.id.exercise_personalize_plan_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Activity_Exercise_Personalize_AddExercise.class));
            }
        });

    }


    public void toolBar() {
        Toolbar toolbar = findViewById(R.id.exercise_personalize_plan_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void recreate() {
        super.recreate();
    }
}
