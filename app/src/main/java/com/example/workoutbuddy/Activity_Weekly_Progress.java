package com.example.workoutbuddy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Activity_Weekly_Progress extends AppCompatActivity {

    Button submit;
    EditText height, weight;
    DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__weekly__progress);

        db = new DatabaseHelper(this);
        submit = findViewById(R.id.activity_weekly_progress_submit);
        height = findViewById(R.id.activity_weekly_progress_height);
        weight = findViewById(R.id.activity_weekly_progress_weight);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (height.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please input all the needed informations", Toast.LENGTH_LONG).show();
                } else if (weight.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please input all the needed informations", Toast.LENGTH_LONG).show();
                } else {
                    double height_double = Double.parseDouble(String.valueOf(height.getText()));
                    double weight_double = Double.parseDouble(String.valueOf(weight.getText()));

                    db.addWeeklyProgress(weight_double, height_double);
                    Intent intent = new Intent(Activity_Weekly_Progress.this, Exercises.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
