package com.example.workoutbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class Activity_Exercise_Plan_Lose_Day extends AppCompatActivity {

    ImageView[] exercise = new ImageView[13];
    LinearLayout[] layout = new LinearLayout[13];
    AnimationDrawable[] exercise_animation = new AnimationDrawable[13];
    TextView[] title = new TextView[13], seconds = new TextView[13];
    View[] line = new View[13];
    TextView day_text, time_text;
    Button start;
    int day;
    DatabaseHelper db;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise__plan__lose__day);
        setTitle("");

        db = new DatabaseHelper(this);
        day = getIntent().getIntExtra("EXTRA_DAY", 0);
        toolbar();
        initialization();

        start = findViewById(R.id.exercise_plan_lose_day_start);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Activity_Exercise_Plan_Lose_Day_Exercise.class);
                intent.putExtra("EXTRA_DAY", day);
                startActivity(intent);
            }
        });

    }


    public void initialization() {
        String text = "", string_title = "";
        int id;

        cursor = db.viewLoseTime(day);
        while (cursor.moveToNext()) {
            text = cursor.getInt(0) + " Mins, " + cursor.getInt(1) + " Workouts";
        }

        time_text = findViewById(R.id.exercise_plan_lose_day_time_text);
        time_text.setText(text);
        day_text = findViewById(R.id.exercise_plan_lose_day_day_text);
        day_text.setText("Day " + day);

        for (int i = 0; i < 13; i++) {
            id = Activity_Exercise_Plan_Lose_Day.this.getResources().getIdentifier(
                    "exercise_plan_lose_day_layout_" + (i + 1),
                    "id",
                    Activity_Exercise_Plan_Lose_Day.this.getPackageName());
            layout[i] = findViewById(id);
            id = Activity_Exercise_Plan_Lose_Day.this.getResources().getIdentifier(
                    "exercise_plan_lose_day_layout_image_" + (i + 1),
                    "id",
                    Activity_Exercise_Plan_Lose_Day.this.getPackageName());
            exercise[i] = findViewById(id);
            id = Activity_Exercise_Plan_Lose_Day.this.getResources().getIdentifier(
                    "exercise_plan_lose_day_layout_title_" + (i + 1),
                    "id",
                    Activity_Exercise_Plan_Lose_Day.this.getPackageName());
            title[i] = findViewById(id);
            id = Activity_Exercise_Plan_Lose_Day.this.getResources().getIdentifier(
                    "exercise_plan_lose_day_layout_seconds_" + (i + 1),
                    "id",
                    Activity_Exercise_Plan_Lose_Day.this.getPackageName());
            seconds[i] = findViewById(id);
            id = Activity_Exercise_Plan_Lose_Day.this.getResources().getIdentifier(
                    "exercise_plan_lose_day_layout_view_" + (i + 1),
                    "id",
                    Activity_Exercise_Plan_Lose_Day.this.getPackageName());
            line[i] = findViewById(id);

        }

        cursor = db.viewLose(day);
        int o = 0;

        while (cursor.moveToNext()) {
            layout[o].setVisibility(View.VISIBLE);
            line[o].setVisibility(View.VISIBLE);
            title[o].setText(cursor.getString(0) + "");
            string_title = cursor.getString(0);
            seconds[o].setText(cursor.getInt(3) + "s");
            id = getResources().getIdentifier(getPackageName() + ":drawable/exercise_icon_lose_" + cursor.getString(1), null, null);
            exercise[o].setBackgroundResource(id);
            if (id == R.drawable.exercise_icon_lose_plank) {

            } else {
                exercise_animation[o] = (AnimationDrawable) exercise[o].getBackground();
                exercise_animation[o].start();
            }

            o++;
        }

    }

    public void toolbar() {
        Toolbar toolbar = findViewById(R.id.toolBar_exercise_plan_lose_day);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
