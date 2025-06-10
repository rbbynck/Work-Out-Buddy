package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class Activity_Exercise_Plan extends AppCompatActivity {

    LinearLayout[] day = new LinearLayout[30];
    DatabaseHelper db;
    String[] dayStatus = new String[30];
    int[] plan_progress = new int[30];
    ProgressBar[] progress = new ProgressBar[30];
    TextView[] progress_text = new TextView[30];
    TextView[] plan_day = new TextView[30];
    FrameLayout[] status = new FrameLayout[30];
    FrameLayout[] done = new FrameLayout[30];
    Cursor cursor;
    Intent intent;
    String next;
    TextView days_left;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise__plan);

        db = new DatabaseHelper(this);
        cursor = db.viewWorkout_Progress();

        toolbar();
        initialization();
        data();
    }

    public void initialization() {
        int id;
        for (int i = 0; i < 30; i++) {
            id = Activity_Exercise_Plan.this.getResources().getIdentifier(
                    "exercise_plan_lose_day" + (i + 1) + "_DONE",
                    "id",
                    Activity_Exercise_Plan.this.getPackageName());
            done[i] = findViewById(id);
            id = Activity_Exercise_Plan.this.getResources().getIdentifier(
                    "exercise_plan_lose_day" + (i + 1),
                    "id",
                    Activity_Exercise_Plan.this.getPackageName());
            day[i] = findViewById(id);
            id = Activity_Exercise_Plan.this.getResources().getIdentifier(
                    "exercise_plan_lose_day" + (i + 1) + "_TEXT",
                    "id",
                    Activity_Exercise_Plan.this.getPackageName());
            plan_day[i] = findViewById(id);
            id = Activity_Exercise_Plan.this.getResources().getIdentifier(
                    "exercise_plan_lose_day" + (i + 1) + "_STATUS",
                    "id",
                    Activity_Exercise_Plan.this.getPackageName());
            status[i] = findViewById(id);
            id = Activity_Exercise_Plan.this.getResources().getIdentifier(
                    "exercise_plan_lose_day" + (i + 1) + "_Progress",
                    "id",
                    Activity_Exercise_Plan.this.getPackageName());
            progress[i] = findViewById(id);
            id = Activity_Exercise_Plan.this.getResources().getIdentifier(
                    "exercise_plan_lose_day" + (i + 1) + "_ProgressText",
                    "id",
                    Activity_Exercise_Plan.this.getPackageName());
            progress_text[i] = findViewById(id);
        }

        days_left = findViewById(R.id.exercise_plan_status_day);
        progressBar = findViewById(R.id.exercise_plan_status_progress);


    }

    public void data() {
        int d = 0;
        while (cursor.moveToNext()) {
            plan_progress[d] = cursor.getInt(0);
            d++;
        }

        for (int i = 0; i < 30; i++) {
            if (!(i == 3 || i == 7 || i == 11 || i == 15 || i == 19 || i == 23 || i == 27)) {
                progress[i].setProgress(plan_progress[i]);
                progress_text[i].setText(plan_progress[i] + "%");
            }
        }


        for (int i = 0; i < day.length; i++) {
            int c = i;
            day[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cursor = db.viewWorkoutPlan();
                    while (cursor.moveToNext()) {
                        if (cursor.getInt(0) == 1) {
                            if (!(c == 3 || c == 7 || c == 11 || c == 15 || c == 19 || c == 23 || c == 27)) {
                                intent = new Intent(Activity_Exercise_Plan.this, Activity_Exercise_Plan_Lose_Day.class);
                            } else {
                                intent = new Intent(Activity_Exercise_Plan.this, Activity_Exercise_Plan_Lose_Day_Rest.class);
                            }
                        } else if (cursor.getInt(0) == 0) {
                            if (!(c == 3 || c == 7 || c == 11 || c == 15 || c == 19 || c == 23 || c == 27)) {
                                intent = new Intent(Activity_Exercise_Plan.this, Activity_Exercise_Plan_Gain_Day.class);
                            } else {
                                intent = new Intent(Activity_Exercise_Plan.this, Activity_Exercise_Plan_Gain_Day_Rest.class);
                            }
                        }
                        intent.putExtra("EXTRA_DAY", c + 1);
                        startActivity(intent);
                    }
                }
            });
        }

        for (int i = 0; i < 30; i++) {
            dayStatus[i] = db.viewWorkoutPlan_Status(i + 1);
            switch (dayStatus[i]) {
                case "Done":
                    progressBar.setProgress(i + 1);
                    days_left.setText((30 - (i + 1)) + " Days Left");
                    status[i].setVisibility(View.GONE);
                    done[i].setVisibility(View.VISIBLE);
                    plan_day[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
                    break;
                case "Active":
                    day[i].setBackgroundDrawable(getResources().getDrawable(R.drawable.exercise_plan_layout_background_active));
                    break;
                case "Not_Active":
                    day[i].setClickable(false);
                    break;
                default:
                    break;
            }
        }
    }

    public void toolbar() {
        Toolbar toolbar = findViewById(R.id.toolBar_exercise_plan);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
        Intent i = new Intent(Activity_Exercise_Plan.this, Activity_Exercise_Plan.class);
        startActivity(i);
    }
}
