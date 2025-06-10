package com.example.workoutbuddy;

import android.app.ActionBar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.Calendar;


public class IntroActivity extends AppCompatActivity {

    private ImageView logo;
    private static int introTime = 9000;
    Intent intent;
    DatabaseHelper db;
    AlarmManager alarmManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        threeDaysNotOpen();
        db = new DatabaseHelper(this);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);


        //ANIMATION BACKGROUND
        ConstraintLayout constraintLayout = findViewById(R.id.layout);
        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(3000);
        animationDrawable.start();

        logo = findViewById(R.id.imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Cursor cursor1, cursor2;
                cursor1 = db.viewProfile();
                cursor2 = db.knowIfWeeklyProgressNeedsInput();

                //CURSOR 2
                int weekNo = 0;
                while (cursor2.moveToNext()) {
                    weekNo = cursor2.getInt(0);
                }

                intent = new Intent(IntroActivity.this, Login.class);

                startActivity(intent);
                finish();
            }
        }, introTime);

        Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.intro);
        logo.startAnimation(myAnim);
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    public void threeDaysNotOpen() {
        Intent intent = new Intent(IntroActivity.this, ReminderHadNotOpenBroadcast.class);
        PendingIntent pendingIntent;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        pendingIntent = PendingIntent.getBroadcast(IntroActivity.this, 777, intent, 0);
        alarmManager.cancel(pendingIntent);

        pendingIntent = PendingIntent.getBroadcast(IntroActivity.this, 777, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), alarmManager.INTERVAL_DAY * 3, pendingIntent);


    }
}
