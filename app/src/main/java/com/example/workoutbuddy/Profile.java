package com.example.workoutbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {

    TextView name, gender, bmi, weight, height;
    DatabaseHelper db;
    Button history;
    TextView playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        bottomNav();

        name = findViewById(R.id.tvProfileName);
        gender = findViewById(R.id.tvProfileGender);
        bmi = findViewById(R.id.tvProfileBMI);
        weight = findViewById(R.id.tvProfileWeight);
        height = findViewById(R.id.tvProfileHeight);
        history = findViewById(R.id.activity_profile_history_button);
        db = new DatabaseHelper(this);

        viewData();

        history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Activity_Workout_History.class));
            }
        });
    }

    private void viewData() {
        Cursor cursor = db.viewData();

        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No Data To Show", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                name.setText(cursor.getString(0));
                gender.setText(cursor.getString(1));
                weight.setText(cursor.getString(2) + " kg");

                height.setText(cursor.getString(3) + " cm");

                bmi.setText(cursor.getString(4));
            }
        }
    }

    public void bottomNav() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profle);
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
