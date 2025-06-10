package com.example.workoutbuddy;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;
import java.util.Objects;
import java.util.Random;

public class Activity_Exercise_Plan_Gain_Day_Exercise extends AppCompatActivity {

    ProgressBar exercise_progress;
    LinearLayout readytogo_layout, exercise_layout, rest_layout;
    TextView readytogo_text, exercise_title, exercise_time_text, rest_time, rest_skip, rest_title, rest_time_text, rest_next_title, readytogo_exercise;
    ImageView readytogo_skip, exercise_image, exercise_next, exercise_prev, exercise_button, rest_image, readytogo_image, volume;
    AnimationDrawable exercise_animation, rest_animation, readytogo_animation;
    String[] exercises, motivationalQuotes = new String[50];
    long readytogo_time, exercise_time_left, rest_time_left = 0, calories, current_calories = 0;
    int current_day, next_day, total_exercise, exerciseNo = 1, exerciseTime, mediaplayer_length;
    int[] drawable, exercise_calories, exercise_seconds;
    double progress_initial, progress = 0;
    boolean exerciseIsRunning = false, restIsRunning = false;
    CountDownTimer readytogo_timer, exercise_timer, rest_timer;
    TextToSpeech textToSpeech;
    MediaPlayer mediaPlayer;
    DatabaseHelper db = new DatabaseHelper(this);
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__exercise__plan__gain__day__exercise);
        setTitle("");
        toolBar();
        textToSpeech();
        database();
        initialization();
        buttonFunction();
    }

    public void initialization() {
        volume = findViewById(R.id.exercise_plan_gain_exercise_volume);
        rest_layout = findViewById(R.id.exercise_plan_gain_day__exercise_rest_layout);
        rest_time = findViewById(R.id.exercise_plan_gain_day__exercise_rest_time);
        rest_skip = findViewById(R.id.exercise_plan_gain_day__exercise_rest_skip);
        rest_time_text = findViewById(R.id.exercise_plan_gain_day__exercise_rest_nexttime);
        rest_next_title = findViewById(R.id.exercise_plan_gain_day__exercise_rest_next);
        rest_image = findViewById(R.id.exercise_plan_gain_day__exercise_rest_image);
        rest_title = findViewById(R.id.exercise_plan_gain_day__exercise_rest_title);
        rest_animation = (AnimationDrawable) rest_image.getBackground();
        rest_animation.start();
        exercise_image = findViewById(R.id.exercise_plan_gain_day__exercise_exercise_image);
        exercise_layout = findViewById(R.id.exercise_plan_gain_day__exercise_exercise_layout);
        exercise_button = findViewById(R.id.exercise_plan_gain_day__exercise_exercise_button);
        exercise_next = findViewById(R.id.exercise_plan_gain_day__exercise_exercise_next);
        exercise_prev = findViewById(R.id.exercise_plan_gain_day__exercise_exercise_prev);
        exercise_time_text = findViewById(R.id.exercise_plan_gain_day__exercise_exercise_time);
        exercise_title = findViewById(R.id.exercise_plan_gain_day__exercise_exercise_title);
        exercise_progress = findViewById(R.id.exercise_plan_gain_day__exercise_exercise_progressbar);
        readytogo_text = findViewById(R.id.exercise_plan_gain_day__exercise_readytogo_time);
        readytogo_skip = findViewById(R.id.exercise_plan_gain_day__exercise_readytogo_skip);
        readytogo_layout = findViewById(R.id.exercise_plan_gain_day__exercise_readytogo_layout);
        readytogo_image = findViewById(R.id.exercise_plan_gain_day__exercise_readytogo_image);
        readytogo_animation = (AnimationDrawable) readytogo_image.getBackground();
        readytogo_image.setBackgroundResource(drawable[0]);
        exercise_image.setBackgroundResource(drawable[0]);
        readytogo_animation.start();
        exercise_animation = (AnimationDrawable) exercise_image.getBackground();
        exercise_animation.start();
        exercise_title.setText(exercises[0]);
        readytogo_exercise = findViewById(R.id.gain_day_ready_to_go_exercise);
        readytogo_exercise.setText(exercises[0]);
        motivationalQuotes();
    }

    public void motivationalQuotes() {
        Cursor cursor = db.motivationQuotes();
        int o = 0;
        while (cursor.moveToNext()) {
            motivationalQuotes[o] = cursor.getString(0);
            o++;
        }
    }

    public void database() {
        int id;
        Cursor cursor = db.viewGain(getIntent().getIntExtra("EXTRA_DAY", 1));
        exercises = new String[cursor.getCount()];
        drawable = new int[cursor.getCount()];
        exercise_calories = new int[cursor.getCount()];
        exercise_seconds = new int[cursor.getCount()];
        progress_initial = (double) 100 / cursor.getCount();
        current_day = getIntent().getIntExtra("EXTRA_DAY", 1);
        next_day = current_day + 1;
        total_exercise = cursor.getCount();
        int o = 0;
        while (cursor.moveToNext()) {
            exercises[o] = cursor.getString(0);
            id = getResources().getIdentifier(getPackageName() + ":drawable/exercise_icon_gain_" + cursor.getString(1), null, null);
            drawable[o] = id;
            exercise_calories[o] = cursor.getInt(2);
            exercise_seconds[o] = cursor.getInt(3);
            o++;
        }

        exerciseTime = exercise_seconds[0];
    }

    public void buttonFunction() {
        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volume.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_volume_up_black_24dp).getConstantState()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0f, 0f);
                    }
                    volume.setImageResource(R.drawable.ic_volume_off_black_24dp);
                } else if (volume.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0.5f, 0.5f);
                    }
                    volume.setImageResource(R.drawable.ic_volume_up_black_24dp);
                }
            }
        });

        readytogo_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializingMediaPlayer();
                mediaPlayer.start();
                readytogo_timer.cancel();
                ExerciseCountdown(exerciseTime * 1000);
                exercise_progress.setMax(exerciseTime);
                readytogo_layout.setVisibility(View.GONE);
                exercise_layout.setVisibility(View.VISIBLE);
                textToSpeech.speak("Start", TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        rest_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rest_time_left = 0;
                rest_timer.cancel();
                ExerciseCountdown(exerciseTime * 1000);
                rest_layout.setVisibility(View.GONE);
                exercise_layout.setVisibility(View.VISIBLE);

                if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0.5f, 0.5f);
                        textToSpeech.speak("Start", TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }
        });

        exercise_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exercise_button.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp).getConstantState()) {
                    exercise_timer.cancel();
                    exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
                } else if (exercise_button.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp).getConstantState()) {
                    ExerciseCountdown(exercise_time_left);
                    exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                }
            }
        });

        exercise_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exerciseIsRunning = false;
                progress = (double) progress_initial * exerciseNo;
                calories += current_calories;
                if (db.viewDayActiveWorkoutPlan() == current_day) {
                    db.updateWorkOutPlan(current_day, "Active", calories, (int) progress);
                } else if (db.viewDayActiveWorkoutPlan() > current_day) {
                    db.updateWorkOutPlan(current_day, "Done", calories, (int) progress);
                }
                exercise_timer.cancel();
                exerciseNo++;
                ExerciseChange(exerciseNo);
                rest_layout.setVisibility(View.VISIBLE);
                exercise_layout.setVisibility(View.GONE);
                RestCountdown(30 * 1000);
                if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                    textToSpeech.speak("Take a Rest", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });

        exercise_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercise_timer.cancel();
                exerciseNo--;
                ExerciseChange(exerciseNo);
                rest_layout.setVisibility(View.VISIBLE);
                exercise_layout.setVisibility(View.GONE);
                RestCountdown(30 * 1000);
                if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                    textToSpeech.speak("Take a Rest", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        ReadyToGoCountdown();
    }

    public void ReadyToGoCountdown() {
        readytogo_timer = new CountDownTimer(10 * 1000, 1000) {
            int seconds;
            String time;

            @Override
            public void onTick(long millisUntilFinished) {
                readytogo_time = millisUntilFinished;
                seconds = (int) readytogo_time / 1000;
                time = "" + (seconds + 1);
                if (seconds <= 10) {
                    if (seconds == 5) {
                        textToSpeech.speak("Ready to go!", TextToSpeech.QUEUE_FLUSH, null);
                    }

                    if (seconds != 0) {
                        readytogo_text.setText(seconds + "'");
                    }
                }
            }

            @Override
            public void onFinish() {
                initializingMediaPlayer();
                mediaPlayer.start();
                textToSpeech.speak("Start", TextToSpeech.QUEUE_FLUSH, null);
                ExerciseCountdown(exerciseTime * 1000);
                readytogo_layout.setVisibility(View.GONE);
                exercise_layout.setVisibility(View.VISIBLE);
                exercise_progress.setMax(exerciseTime);
            }
        }.start();
    }

    public void ExerciseCountdown(long time) {
        exercise_timer = new CountDownTimer(time, 1000) {
            int seconds;
            String texttime;
            int halfTime = exerciseTime / 2;

            @Override
            public void onTick(long millisUntilFinished) {
                exercise_time_left = millisUntilFinished;
                seconds = (int) exercise_time_left / 1000;
                texttime = "" + (seconds + 1);
                current_calories = exercise_calories[exerciseNo - 1] / (exerciseTime / (exerciseTime - seconds));
                if (seconds <= exerciseTime) {
                    if (seconds == halfTime) {
                        if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                            textToSpeech.speak("Half The Time", TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }

                    exerciseIsRunning = true;
                    if (seconds <= 3) {
                        textToSpeech.speak(texttime, TextToSpeech.QUEUE_FLUSH, null);
                    }
                    exercise_time_text.setText(texttime + "/" + exerciseTime);
                    exercise_progress.setProgress(exerciseTime - seconds);
                }
            }

            @Override
            public void onFinish() {
                progress = (double) progress_initial * exerciseNo;
                calories += current_calories;
                if (db.viewDayActiveWorkoutPlan() == current_day) {
                    db.updateWorkOutPlan(current_day, "Active", calories, (int) progress);
                } else if (db.viewDayActiveWorkoutPlan() > current_day) {
                    db.updateWorkOutPlan(current_day, "Done", calories, (int) progress);
                }
                if (exerciseNo == total_exercise) {
                    mediaPlayer.stop();
                }
                exerciseIsRunning = true;
                if (exerciseNo == total_exercise) {
                    db.addWorkoutHistory(calories);
                    if (db.viewDayActiveWorkoutPlan() < next_day) {
                        db.updateWorkOutPlan(current_day, "Done", calories, (int) progress);
                        db.updateWorkOutPlan(next_day, "Active", 0, (int) 0);
                    }
                    intent = new Intent(Activity_Exercise_Plan_Gain_Day_Exercise.this, Activity_Exercise_Congratulations.class);
                    intent.putExtra("EXTRA_DAY", current_day);
                    intent.putExtra("EXTRA_CALORIES", calories);
                    intent.putExtra("EXTRA_EXERCISE", exerciseNo);
                    startActivity(intent);
                } else {
                    if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                        textToSpeech.speak("Take a Rest", TextToSpeech.QUEUE_FLUSH, null);
                    }
                    exerciseNo++;
                    ExerciseChange(exerciseNo);
                    rest_layout.setVisibility(View.VISIBLE);
                    exercise_layout.setVisibility(View.GONE);
                    RestCountdown(30 * 1000);
                }
            }
        }.start();
    }

    public void RestCountdown(long time) {
        rest_timer = new CountDownTimer(time, 1000) {
            int seconds;
            String texttime;
            String title = rest_title.getText().toString();

            @Override
            public void onTick(long millisUntilFinished) {
                rest_time_left = millisUntilFinished;
                seconds = (int) millisUntilFinished / 1000;
                texttime = "" + (seconds + 1);
                restIsRunning = true;

                rest_time.setText(texttime);

                if (seconds == 29) {
                    if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                        if (mediaPlayer != null) {
                            mediaPlayer.setVolume(0.2f, 0.2f);
                        }
                    }
                } else if (seconds == 25) {
                    if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                        textToSpeech.speak("Next Up " + title, TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else if (seconds == 20) {
                    int random = new Random().nextInt((50 - 1) + 1) + 1;
                    if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                        textToSpeech.speak(motivationalQuotes[random], TextToSpeech.QUEUE_FLUSH, null);
                    }
                } else if (seconds <= 3) {
                    if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                        textToSpeech.speak(texttime, TextToSpeech.QUEUE_FLUSH, null);
                    }
                }
            }

            @Override
            public void onFinish() {
                rest_time_left = 0;
                if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                    if (mediaPlayer != null) {
                        mediaPlayer.setVolume(0.5f, 0.5f);
                    }
                }
                restIsRunning = false;
                ExerciseCountdown(exerciseTime * 1000);
                rest_layout.setVisibility(View.GONE);
                exercise_layout.setVisibility(View.VISIBLE);
                if (volume.getDrawable().getConstantState() != getResources().getDrawable(R.drawable.ic_volume_off_black_24dp).getConstantState()) {
                    textToSpeech.speak("Start", TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        }.start();
    }

    public void ExerciseChange(int exerciseNumber) {
        int currentExercise = exerciseNumber;

        exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
        exercise_progress.setProgress(0);

        for (int i = 0; i < total_exercise; i++) {
            if (currentExercise == (i + 1)) {
                exercise_title.setText(exercises[i]);
                exerciseTime = exercise_seconds[i];
                exercise_progress.setMax(exercise_seconds[i]);
                rest_title.setText(exercises[i]);
                rest_next_title.setText("NEXT " + currentExercise + "/" + total_exercise);
                rest_time_text.setText(exercise_seconds[i] + " s");
                exercise_image.setBackgroundResource(drawable[i]);
                rest_image.setBackgroundResource(drawable[i]);
                if (drawable[i] != R.drawable.exercise_icon_gain_invertedboard && drawable[i] != R.drawable.exercise_icon_gain_unstablesitups) {
                    exercise_animation = (AnimationDrawable) exercise_image.getBackground();
                    exercise_animation.start();
                    rest_animation = (AnimationDrawable) rest_image.getBackground();
                    rest_animation.start();
                }
                if (currentExercise == 1) {
                    exercise_prev.setVisibility(View.INVISIBLE);
                    exercise_next.setVisibility(View.VISIBLE);
                } else if (currentExercise == total_exercise) {
                    exercise_prev.setVisibility(View.VISIBLE);
                    exercise_next.setVisibility(View.INVISIBLE);
                } else {
                    exercise_prev.setVisibility(View.VISIBLE);
                    exercise_next.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void toolBar() {
        Toolbar toolbar = findViewById(R.id.toolBar_exercise_plan_gain_day_exercise);
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

    public void initializingMediaPlayer() {
        Cursor cursor = db.viewPlaylist();
        while (cursor.moveToNext()) {
            int id = getResources().getIdentifier(getPackageName() + ":raw/" + cursor.getString(0), null, null);
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(getApplicationContext(), id);
            }
        }
    }

    @Override
    protected void onPause() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }

        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaplayer_length = mediaPlayer.getCurrentPosition();
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
            mediaplayer_length = mediaPlayer.getCurrentPosition();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        if (textToSpeech != null) {
            textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status != TextToSpeech.ERROR) {
                        textToSpeech.setLanguage(Locale.US);
                    }
                }
            });
        }

        if (mediaPlayer != null) {
            mediaPlayer.seekTo(mediaplayer_length);
            mediaPlayer.start();
        }

        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (exerciseIsRunning == true) {
            exercise_timer.cancel();
            exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_play_arrow_black_24dp));
        }
        if (restIsRunning == true) {
            rest_timer.cancel();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Quit or Continue?");
        builder.setMessage("Do you really want to quit? ");
        builder.setPositiveButton("Quit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                mediaPlayer.stop();
                if (db.viewDayActiveWorkoutPlan() == current_day) {
                    db.updateWorkOutPlan(current_day, "Active", calories, (int) progress); //CHANGE
                } else if (db.viewDayActiveWorkoutPlan() > current_day) {
                    db.updateWorkOutPlan(current_day, "Done", calories, (int) progress); //CHANGE
                }
                db.addWorkoutHistory(calories);
                Activity_Exercise_Plan_Gain_Day_Exercise.super.onBackPressed();
            }
        });
        builder.setNegativeButton("Continue", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (exerciseIsRunning == true) {
                    ExerciseCountdown(exercise_time_left);
                    exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                }
                if (restIsRunning == true) {
                    RestCountdown(rest_time_left);
                }
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (exerciseIsRunning == true) {
                    ExerciseCountdown(exercise_time_left);
                    exercise_button.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause_circle_filled_black_24dp));
                }
                if (restIsRunning == true) {
                    RestCountdown(rest_time_left);
                }

            }
        });
        builder.show();
    }
}
