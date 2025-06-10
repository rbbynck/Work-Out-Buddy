package com.example.workoutbuddy;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.*;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Activity_Exercise_Personalize_Exercise extends AppCompatActivity {

    LinearLayout ready_to_go_layout, exercise_layout, rest_layout;
    DatabaseHelper db;
    Cursor cursor;
    Intent intent;

    //ReadyToGo
    TextView ready_to_go_time;
    ImageView ready_to_go_skip;
    long ready_to_go_time_left;

    //Exercise
    TextView exercise_time, exercise_title;
    ImageView exercise_button, exercise_next, exercise_prev;
    ProgressBar exercise_progressbar;

    //Rest
    TextView rest_time, rest_skip;

    //OTHER
    int sets_no, work_time, rest_time_, exercise_no = 1;
    TextToSpeech textToSpeech;
    MediaPlayer mediaPlayer;
    String exercise_title_current;
    String[] motivationalQuotes = new String[50];
    double progress_initial = (double) 100 / 7;
    double progress = 0;

    //Countdown
    CountDownTimer rest_timer, exercise_timer, readytogo_timer;
    long exercise_time_left, rest_time_left;
    Boolean exercise_running = false, rest_running = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise__personalize__exercise);
        setTitle("");

        toolBar();
        textToSpeech();

        initialization();
        database();
        motivationalQuotes();
        rest();
        exercise();
        readytogo();
    }

    public void toolBar() {
        Toolbar toolbar = findViewById(R.id.activity_exercise_personalize_exercise_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    public void textToSpeech() {
        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    textToSpeech.setLanguage(Locale.US);
                }
            }
        });
    }

    public void initialization() {
        db = new DatabaseHelper(this);
        ready_to_go_layout = findViewById(R.id.activity_exercise_personalize_exercise_ReadyToGOLAYOUT);
        exercise_layout = findViewById(R.id.activity_exercise_personalize_exercise_exerciseLAYOUT);
        rest_layout = findViewById(R.id.activity_exercise_personalize_exercise_RESTLAYOUT);

        ready_to_go_time = findViewById(R.id.activity_exercise_personalize_exercise_ReadyToGO_text);
        ready_to_go_skip = findViewById(R.id.activity_exercise_personalize_exercise_ReadyToGO_next);

        exercise_time = findViewById(R.id.activity_exercise_personalize_exercise_exerciseTEXTTIME);
        exercise_title = findViewById(R.id.activity_exercise_personalize_exercise_exerciseTEXTTITLE);
        exercise_button = findViewById(R.id.activity_exercise_personalize_exercise_exerciseBUTTON);
        exercise_next = findViewById(R.id.activity_exercise_personalize_exercise_exerciseNEXT);
        exercise_prev = findViewById(R.id.activity_exercise_personalize_exercise_exercisePREV);
        exercise_progressbar = findViewById(R.id.activity_exercise_personalize_exercise_exerciseProgressBar);

        rest_time = findViewById(R.id.activity_exercise_personalize_exercise_restTIME);
        rest_skip = findViewById(R.id.activity_exercise_personalize_exercise_restSKIP);

    }

    public void database() {
        cursor = db.viewPersonalizeExercise(getIntent().getStringExtra("EXTRA_TITLE"));

        while (cursor.moveToNext()) {
            sets_no = cursor.getInt(0);
            work_time = cursor.getInt(1);
            rest_time_ = cursor.getInt(2);
        }

        progress_initial = (double) 100 / sets_no;
    }

    public void motivationalQuotes() {
        int i = 0;
        cursor = db.motivationQuotes();

        while (cursor.moveToNext()) {
            motivationalQuotes[i] = cursor.getString(0);
            i++;
        }

    }

    public void readytogo() {
        ready_to_go_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readytogo_timer.cancel();
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hiphop);
                }
                mediaPlayer.start();

                exercise_progressbar.setMax(work_time);
                ready_to_go_layout.setVisibility(View.GONE);
                exercise_layout.setVisibility(View.VISIBLE);
                exercise_countdown(work_time * 1000);
                textToSpeech.speak("Start", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        readytogo_countdown();
    }

    public void exercise() {
        exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exercise_button.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp).getConstantState()) {
                    //PAUSE
                    exercise_timer.cancel();
                    exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                } else if (exercise_button.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp).getConstantState()) {
                    //PLAY
                    exercise_countdown(exercise_time_left);
                    exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                }
            }
        });

        exercise_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_running = false;
                progress = (double) progress_initial * exercise_no;
                exercise_timer.cancel();
                exercise_no++;
                if (exercise_no == sets_no) {
                    exercise_next.setVisibility(View.INVISIBLE);
                } else {
                    exercise_next.setVisibility(View.VISIBLE);
                }
                if (exercise_no == 1) {
                    exercise_prev.setVisibility(View.INVISIBLE);
                } else {
                    exercise_prev.setVisibility(View.VISIBLE);
                }
                exercise_change(exercise_no);
                rest_layout.setVisibility(View.VISIBLE);
                exercise_layout.setVisibility(View.GONE);
                rest_countdown(rest_time_ * 1000);
                textToSpeech.speak("Take a Rest", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        exercise_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_timer.cancel();
                exercise_no--;
                if (exercise_no == 1) {
                    exercise_prev.setVisibility(View.INVISIBLE);
                } else {
                    exercise_prev.setVisibility(View.VISIBLE);
                }
                rest_layout.setVisibility(View.VISIBLE);
                exercise_layout.setVisibility(View.GONE);
                rest_countdown(rest_time_ * 1000);
                textToSpeech.speak("Take a Rest", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    public void rest() {

        rest_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rest_timer.cancel();
                exercise_countdown(work_time * 1000);
                rest_layout.setVisibility(View.GONE);
                exercise_layout.setVisibility(View.VISIBLE);

                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(0.5f, 0.5f);
                }
                textToSpeech.speak("Start", TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }

    public void readytogo_countdown() {
        readytogo_timer = new CountDownTimer(10 * 1000, 1000) {
            int seconds;
            String time;

            @Override
            public void onTick(long millisUntilFinished) {
                ready_to_go_time_left = millisUntilFinished;
                seconds = (int) ready_to_go_time_left / 1000;
                time = "" + seconds;
                if (seconds <= 10) {
                    if (seconds == 5) {
                        textToSpeech.speak("Ready to go!", TextToSpeech.QUEUE_FLUSH, null);
                    }

                    if (seconds > 0) {
                        ready_to_go_time.setText(seconds + "'");
                    }
                }
            }

            @Override
            public void onFinish() {
                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.hiphop);
                }
                mediaPlayer.start();
                readytogo_timer.cancel();
                exercise_countdown(work_time * 1000);
                exercise_progressbar.setMax(work_time);
                exercise_layout.setVisibility(View.VISIBLE);
                ready_to_go_layout.setVisibility(View.GONE);
                textToSpeech.speak("Start", TextToSpeech.QUEUE_FLUSH, null);

            }
        }.start();
    }

    public void exercise_countdown(long time) {
        exercise_timer = new CountDownTimer(time, 1000) {
            int seconds;
            String texttime;
            int halfTime = work_time / 2;

            @Override
            public void onTick(long millisUntilFinished) {
                exercise_time_left = millisUntilFinished;
                seconds = (int) exercise_time_left / 1000;
                texttime = "" + seconds;

                if (seconds <= work_time) {
                    if (seconds == halfTime) {
                        textToSpeech.speak("Half The Time", TextToSpeech.QUEUE_FLUSH, null);
                    }

                    if (seconds != 0) {
                        exercise_running = true;
                        if (seconds <= 3) {
                            textToSpeech.speak(texttime, TextToSpeech.QUEUE_FLUSH, null);
                        }
                        exercise_time.setText(texttime + "/" + work_time);
                        exercise_progressbar.setProgress(work_time - seconds);
                    }
                }
            }

            @Override
            public void onFinish() {
                progress = (double) progress_initial * exercise_no;
                exercise_running = false;

                if (exercise_no == sets_no) {
                    if (mediaPlayer != null) {
                        mediaPlayer.stop();
                    }

                    //workout history
                    intent = new Intent(Activity_Exercise_Personalize_Exercise.this, Activity_Exercise_Congratulations.class);
                    intent.putExtra("EXTRA_TITLE", exercise_title_current);
                    intent.putExtra("EXTRA_EXERCISE", sets_no);
                    startActivity(intent);
                } else {
                    textToSpeech.speak("Take a Rest", TextToSpeech.QUEUE_FLUSH, null);
                    exercise_no++;
                    if (exercise_no == sets_no) {
                        exercise_next.setVisibility(View.INVISIBLE);
                    } else {
                        exercise_next.setVisibility(View.VISIBLE);
                    }
                    exercise_change(exercise_no);
                    rest_layout.setVisibility(View.VISIBLE);
                    exercise_layout.setVisibility(View.GONE);
                    rest_countdown(rest_time_ * 1000);
                }
            }
        }.start();
    }


    public void rest_countdown(long time) {
        if (time == 0) {
            time = rest_time_;
        }

        rest_timer = new CountDownTimer(time, 1000) {
            int seconds;
            String texttime;

            @Override
            public void onTick(long millisUntilFinished) {
                rest_time_left = millisUntilFinished;
                seconds = (int) rest_time_left / 1000;
                texttime = "" + seconds;
                rest_running = true;

                if (seconds == rest_time_) {
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0.2f, 0.2f);
                    }
                }

                if (seconds == (rest_time_ / 2)) {
                    int random = new Random().nextInt((50 - 1) + 1) + 1;
                    textToSpeech.speak(motivationalQuotes[random], TextToSpeech.QUEUE_FLUSH, null);
                }

                if (seconds > 0) {
                    if (seconds <= 3) {
                        textToSpeech.speak(texttime, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    rest_time.setText(texttime);
                }
            }

            @Override
            public void onFinish() {
                rest_time_left = 0;
                if (mediaPlayer != null) {
                    mediaPlayer.setVolume(0.5f, 0.5f);
                }
                rest_running = false;
                exercise_countdown(work_time * 1000);
                rest_layout.setVisibility(View.GONE);
                exercise_layout.setVisibility(View.VISIBLE);
                textToSpeech.speak("Start", TextToSpeech.QUEUE_FLUSH, null);
            }
        }.start();
    }


    public void exercise_change(int exercise_no) {
        exercise_title.setText("Exercise " + exercise_no);
        exercise_progressbar.setProgress(0);
        exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
    }

    @Override
    protected void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        if (mediaPlayer != null) {
            mediaPlayer.pause();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (exercise_running == true) {
            exercise_timer.cancel();
            exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        }

        if (exercise_running == true) {
            rest_timer.cancel();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit or Continue?");
        builder.setMessage("Do you really want to quit? ");
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mediaPlayer.stop();
                //db.addWorkoutHistory(calories);
                Activity_Exercise_Personalize_Exercise.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (exercise_running == true) {
                    exercise_countdown(exercise_time_left);
                    exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                }
                if (rest_running == true) {
                    rest_countdown(rest_time_left);
                }
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (exercise_running == true) {
                    exercise_countdown(exercise_time_left);
                    exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                }
                if (rest_running == true) {
                    rest_countdown(rest_time_left);
                }

            }
        });
        builder.show();
    }

}



