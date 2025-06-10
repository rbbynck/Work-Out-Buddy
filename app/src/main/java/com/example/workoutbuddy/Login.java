package com.example.workoutbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.*;

public class Login extends AppCompatActivity {

    EditText name, weight, height;
    RadioGroup gender;
    Button register;
    Intent intent;
    DatabaseHelper db;
    long bmi;
    TextView playlist;
    String workoutPlan, playlist_picked = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        transparentStatusAndNavigation();
        initialization();
        pickPlaylist();
        register();

    }


    public void register() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sname, sweight, sheight, sgender;
                double dweight, dheight;

                sname = name.getText().toString();
                sweight = weight.getText().toString();
                sheight = height.getText().toString();

                if (sname.length() <= 1) {
                    Toast.makeText(Login.this, "Please Input your Name", Toast.LENGTH_LONG).show();
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(Login.this, "Please Select a Gender", Toast.LENGTH_LONG).show();
                } else if (sweight.length() <= 1) {
                    Toast.makeText(Login.this, "Please Input your Weight", Toast.LENGTH_LONG).show();
                } else if (sheight.length() <= 1) {
                    Toast.makeText(Login.this, "Please Input your Height", Toast.LENGTH_LONG).show();
                } else if (playlist_picked == "") {
                    Toast.makeText(Login.this, "Please Select a Playlist", Toast.LENGTH_LONG).show();
                } else {
                    dheight = Double.parseDouble(height.getText().toString());
                    dweight = Double.parseDouble(weight.getText().toString());

                    int rgId = gender.getCheckedRadioButtonId();
                    RadioButton genderB = findViewById(rgId);
                    sgender = (String) genderB.getText();

                    computeBMI(dheight, dweight);
                    long val = db.addUser(sname, sgender, dweight, dheight, bmi, workoutPlan);
                    db.addWeeklyProgress1(dweight, dheight);
                    db.addPlaylist(playlist_picked);

                    if (val > 0) {
                        intent = new Intent(Login.this, Second_Intro.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(Login.this, "Database Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                }

            }
        });
    }

    public void pickPlaylist() {
        playlist = findViewById(R.id.playlist);
        playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] playlist_list = {"Hip Hop", "Rock", "Jazz", "Pop", "Blues", "Popular", "Funk", "Disco", "Techno", "Orchestra", "Rap", "Indie Rock", "R&B", "KPop", "None"};
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Pick a playlist")
                        .setItems(playlist_list, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (which == 0) {
                                    playlist_picked = "hiphop";
                                } else if (which == 1) {
                                    playlist_picked = "rock";
                                } else if (which == 2) {
                                    playlist_picked = "jazz";
                                } else if (which == 3) {
                                    playlist_picked = "pop";
                                } else if (which == 4) {
                                    playlist_picked = "blues";
                                } else if (which == 5) {
                                    playlist_picked = "popular";
                                } else if (which == 6) {
                                    playlist_picked = "funk";
                                } else if (which == 7) {
                                    playlist_picked = "disco";
                                } else if (which == 8) {
                                    playlist_picked = "techno";
                                } else if (which == 9) {
                                    playlist_picked = "orchestra";
                                } else if (which == 19) {
                                    playlist_picked = "rap";
                                } else if (which == 11) {
                                    playlist_picked = "indierock";
                                } else if (which == 12) {
                                    playlist_picked = "r&b";
                                } else if (which == 13) {
                                    playlist_picked = "kpop";
                                } else if (which == 14) {
                                    playlist_picked = "none";
                                }
                                playlist.setText(playlist_list[which]);
                            }
                        });
                builder.show();
            }
        });
    }

    public void initialization() {
        name = findViewById(R.id.etName);
        weight = findViewById(R.id.etWeight);
        height = findViewById(R.id.etHeight);
        gender = findViewById(R.id.rgGender);
        register = findViewById(R.id.bRegister);
    }

    public void computeBMI(double dheight, double dweight) {
        bmi = (long) ((703 * dweight) / (dheight * dheight));

        if (bmi < 18.5) {
            workoutPlan = "gain";
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            workoutPlan = "normal";
        } else if (bmi >= 25 && bmi <= 29.9) {
            workoutPlan = "lose";
        } else if (bmi > 30) {
            workoutPlan = "lose";
        }
    }

    private void transparentStatusAndNavigation() {
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            );
        }
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private void setWindowFlag(final int bits, boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
