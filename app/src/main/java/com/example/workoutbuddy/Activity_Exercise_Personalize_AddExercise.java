package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.Objects;

public class Activity_Exercise_Personalize_AddExercise extends AppCompatActivity {

    ImageView sets_add, sets_minus, work_add, work_minus, rest_add, rest_minus;
    TextView sets_text, work_text, rest_text;
    EditText title;
    Button add;
    int rest_minute = 0, rest_second = 0;
    int work_minute = 0, work_second = 0;
    int sets_no = 1;
    int rest_time, work_time;
    String exercise_title;
    DatabaseHelper db;
    String[] title_not_available;
    Cursor cursor;
    boolean title_exist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise__personalize__add_exercise);
        toolBar();
        setTitle("");

        initialization();
        sets();

        db = new DatabaseHelper(this);
        cursor = db.viewPersonalizeExercise();
        title_not_available = new String[cursor.getCount()];
        int o = 0;
        while (cursor.moveToNext()) {
            title_not_available[o] = cursor.getString(0);
            o++;
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_title = title.getText().toString();
                for (int i = 0; i < title_not_available.length; i++) {
                    if (title_not_available[i] == exercise_title) {
                        title_exist = true;
                    }
                }
                Toast.makeText(Activity_Exercise_Personalize_AddExercise.this, exercise_title, Toast.LENGTH_LONG).show();
                rest_time = (rest_minute * 60) + rest_second;
                work_time = (work_minute * 60) + work_second;


                if (rest_time == 0 || work_time == 0 || sets_no == 0 || exercise_title == "" || title_exist == true) {
                    Toast.makeText(getApplicationContext(), "Incomplete Details", Toast.LENGTH_LONG).show();
                } else {
                    db.addPersonalizeExercise(exercise_title, sets_no, work_time, rest_time);
                    startActivity(new Intent(Activity_Exercise_Personalize_AddExercise.this, Activity_Exercise_Personalize.class));
                }
            }
        });

    }


    public void initialization() {
        add = findViewById(R.id.exercise_personalize_plan_addexercise_add);
        title = findViewById(R.id.exercise_personalize_plan_addexercise_title_text);
        sets_text = findViewById(R.id.exercise_personalize_plan_addexercise_sets_text);
        work_text = findViewById(R.id.exercise_personalize_plan_addexercise_work_text);
        rest_text = findViewById(R.id.exercise_personalize_plan_addexercise_rest_text);
        sets_add = findViewById(R.id.exercise_personalize_plan_addexercise_sets_add);
        sets_minus = findViewById(R.id.exercise_personalize_plan_addexercise_sets_minus);
        work_add = findViewById(R.id.exercise_personalize_plan_addexercise_work_add);
        work_minus = findViewById(R.id.exercise_personalize_plan_addexercise_work_minus);
        rest_add = findViewById(R.id.exercise_personalize_plan_addexercise_rest_add);
        rest_minus = findViewById(R.id.exercise_personalize_plan_addexercise_rest_minus);
    }

    public void sets() {
        sets_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sets_no++;
                sets_text.setText(sets_no + "");
            }
        });

        sets_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sets_no > 0) {
                    sets_no--;
                    sets_text.setText(sets_no + "");
                }
            }
        });

        work_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat formatter = new DecimalFormat("00");

                work_second++;
                if (work_second == 60) {
                    work_second = 0;
                    work_minute++;
                }

                work_text.setText(formatter.format(work_minute) + ":" + formatter.format(work_second));
            }
        });

        work_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat formatter = new DecimalFormat("00");

                if (work_minute > 0 || work_second > 0) {
                    if (work_second == 00) {
                        work_second = 59;
                        work_minute--;
                    } else {
                        work_second--;
                    }
                    work_text.setText(formatter.format(work_minute) + ":" + formatter.format(work_second));
                }
            }
        });

        rest_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat formatter = new DecimalFormat("00");

                rest_second++;
                if (rest_second == 60) {
                    rest_second = 0;
                    rest_minute++;
                }

                rest_text.setText(formatter.format(rest_minute) + ":" + formatter.format(rest_second));
            }
        });

        rest_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat formatter = new DecimalFormat("00");

                if (rest_minute > 0 || rest_second > 0) {
                    if (rest_second == 00) {
                        rest_second = 59;
                        rest_minute--;
                    } else {
                        rest_second--;
                    }
                    rest_text.setText(formatter.format(rest_minute) + ":" + formatter.format(rest_second));
                }
            }
        });
    }

    public void toolBar() {
        Toolbar toolbar = findViewById(R.id.exercise_personalize_plan_addexercise_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }
}
