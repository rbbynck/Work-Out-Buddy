package com.example.workoutbuddy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Schedule extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    TextView textView;
    TimePickerDialog timePickerDialog;
    ImageView add;
    int hour, minute;
    int dayFinal, monthFinal, yearFinal, hourFinal, minuteFinal;
    LinearLayout[] schedules = new LinearLayout[10];
    TextView[] schedule_text = new TextView[10];
    TextView[] schedules_days = new TextView[10];
    int[] scheds = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int[] status = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    ImageView[] delete = new ImageView[10];
    Switch[] schedule_switch = new Switch[10];

    DatabaseHelper db;
    Cursor cursor;

    //ADD
    String[] add_schedule_days = {"", "", "", "", "", "", ""};
    int[] add_schedule_days_int = {0, 0, 0, 0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        db = new DatabaseHelper(this);

        createNotificationChannel();
        bottomNav();
        schedules();


        add = findViewById(R.id.schedule_AddButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addSchedule();
            }
        });


    }


    public void schedules() {
        Intent intent = new Intent(Schedule.this, ReminderBroadcast.class);
        Calendar calendar = Calendar.getInstance();
        PendingIntent pendingIntent;
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int i = 0;

        schedules[0] = findViewById(R.id.schedule_schedule1);
        schedules[1] = findViewById(R.id.schedule_schedule2);
        schedules[2] = findViewById(R.id.schedule_schedule3);
        schedules[3] = findViewById(R.id.schedule_schedule4);
        schedules[4] = findViewById(R.id.schedule_schedule5);
        schedules[5] = findViewById(R.id.schedule_schedule6);
        schedules[6] = findViewById(R.id.schedule_schedule7);
        schedules[7] = findViewById(R.id.schedule_schedule8);
        schedules[8] = findViewById(R.id.schedule_schedule9);
        schedules[9] = findViewById(R.id.schedule_schedule10);

        schedule_switch[0] = findViewById(R.id.schedule_schedule1Switch);
        schedule_switch[1] = findViewById(R.id.schedule_schedule2Switch);
        schedule_switch[2] = findViewById(R.id.schedule_schedule3Switch);
        schedule_switch[3] = findViewById(R.id.schedule_schedule4Switch);
        schedule_switch[4] = findViewById(R.id.schedule_schedule5Switch);
        schedule_switch[5] = findViewById(R.id.schedule_schedule6Switch);
        schedule_switch[6] = findViewById(R.id.schedule_schedule7Switch);
        schedule_switch[7] = findViewById(R.id.schedule_schedule8Switch);
        schedule_switch[8] = findViewById(R.id.schedule_schedule9Switch);
        schedule_switch[9] = findViewById(R.id.schedule_schedule10Switch);

        schedule_text[0] = findViewById(R.id.schedule_schedule1Time);
        schedule_text[1] = findViewById(R.id.schedule_schedule2Time);
        schedule_text[2] = findViewById(R.id.schedule_schedule3Time);
        schedule_text[3] = findViewById(R.id.schedule_schedule4Time);
        schedule_text[4] = findViewById(R.id.schedule_schedule5Time);
        schedule_text[5] = findViewById(R.id.schedule_schedule6Time);
        schedule_text[6] = findViewById(R.id.schedule_schedule7Time);
        schedule_text[7] = findViewById(R.id.schedule_schedule8Time);
        schedule_text[8] = findViewById(R.id.schedule_schedule9Time);
        schedule_text[9] = findViewById(R.id.schedule_schedule10Time);

        schedules_days[0] = findViewById(R.id.schedule_schedule1Days);
        schedules_days[1] = findViewById(R.id.schedule_schedule2Days);
        schedules_days[2] = findViewById(R.id.schedule_schedule3Days);
        schedules_days[3] = findViewById(R.id.schedule_schedule4Days);
        schedules_days[4] = findViewById(R.id.schedule_schedule5Days);
        schedules_days[5] = findViewById(R.id.schedule_schedule6Days);
        schedules_days[6] = findViewById(R.id.schedule_schedule7Days);
        schedules_days[7] = findViewById(R.id.schedule_schedule8Days);
        schedules_days[8] = findViewById(R.id.schedule_schedule9Days);
        schedules_days[9] = findViewById(R.id.schedule_schedule10Days);

        delete[0] = findViewById(R.id.schedule_schedule1Delete);
        delete[1] = findViewById(R.id.schedule_schedule2Delete);
        delete[2] = findViewById(R.id.schedule_schedule3Delete);
        delete[3] = findViewById(R.id.schedule_schedule4Delete);
        delete[4] = findViewById(R.id.schedule_schedule5Delete);
        delete[5] = findViewById(R.id.schedule_schedule6Delete);
        delete[6] = findViewById(R.id.schedule_schedule7Delete);
        delete[7] = findViewById(R.id.schedule_schedule8Delete);
        delete[8] = findViewById(R.id.schedule_schedule9Delete);
        delete[9] = findViewById(R.id.schedule_schedule10Delete);


        for (int c = 0; c < 10; c++) {
            schedules[c].setVisibility(View.GONE);
        }

        //GET THE SCHEDULES

        cursor = db.viewSchedule();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                scheds[cursor.getInt(0) - 1] = 1;
            }
        }

        cursor = db.viewSchedule_Status();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                status[cursor.getInt(0) - 1] = 1;
            }
        }

        //SCHEDULE
        for (int c = 0; c < 10; c++) {
            if (scheds[c] == 1) {
                schedules[c].setVisibility(View.VISIBLE);
                schedule_text[c].setText((db.viewSchedule_Hour(c + 1) <= 9 ? ("0" + db.viewSchedule_Hour(c + 1)) : db.viewSchedule_Hour(c + 1)) +
                        ":" + (db.viewSchedule_Minute(c + 1) <= 9 ? ("0" + db.viewSchedule_Minute(c + 1)) : db.viewSchedule_Minute(c + 1)));
                schedules_days[c].setText(db.viewSchedule_Days(c + 1));
                if (status[c] == 1) {
                    schedule_switch[c].setChecked(true);
                }

                if (schedule_switch[c].isChecked()) {
                    int[] days = db.viewSchedule_DaysInt(c + 1);
                    for (int op = 0; op < 7; op++) {
                        if (days[op] == 1) {
                            if (op == 0) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            } else if (op == 1) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            } else if (op == 2) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                            } else if (op == 3) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                            } else if (op == 4) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                            } else if (op == 5) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            } else if (op == 6) {
                                calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            }

                            calendar.set(Calendar.HOUR_OF_DAY, db.viewSchedule_Hour(c + 1));
                            calendar.set(Calendar.MINUTE, db.viewSchedule_Minute(c + 1));
                            pendingIntent = PendingIntent.getBroadcast(Schedule.this, c + op, intent, 0);
                            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent);
                        }
                    }
                } else {
                    int[] days = db.viewSchedule_DaysInt(c + 1);
                    for (int op = 0; op < 7; op++) {
                        pendingIntent = PendingIntent.getBroadcast(Schedule.this, c + op, intent, 0);
                        alarmManager.cancel(pendingIntent);
                    }
                }

            }

        }

        //SWITCH
        for (int c = 0; c < 10; c++) {
            int ss = c;
            schedule_switch[c].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        if (schedule_switch[ss].isChecked()) {
                            int[] days = db.viewSchedule_DaysInt(ss + 1);
                            for (int op = 0; op < 7; op++) {
                                if (days[op] == 1) {
                                    if (op == 0) {
                                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                                    } else if (op == 1) {
                                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                                    } else if (op == 2) {
                                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                                    } else if (op == 3) {
                                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                                    } else if (op == 4) {
                                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                                    } else if (op == 5) {
                                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                                    } else if (op == 6) {
                                        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                                    }
                                    PendingIntent pendingIntent;
                                    calendar.set(Calendar.HOUR_OF_DAY, db.viewSchedule_Hour(ss + 1));
                                    calendar.set(Calendar.MINUTE, db.viewSchedule_Minute(ss + 1));
                                    pendingIntent = PendingIntent.getBroadcast(Schedule.this, ss + op, intent, 0);
                                    alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent);
                                    db.updateSchedule_Status(ss + 1, 1);
                                }
                            }
                        }
                    } else {
                        int[] days = db.viewSchedule_DaysInt(ss + 1);
                        for (int op = 0; op < 7; op++) {
                            PendingIntent pendingIntent;
                            pendingIntent = PendingIntent.getBroadcast(Schedule.this, ss + op, intent, 0);
                            alarmManager.cancel(pendingIntent);
                            db.updateSchedule_Status(ss + 1, 0);
                        }
                    }
                }
            });
        }

        //DELETE
        for (int c = 0; c < 10; c++) {
            int d = c;
            delete[c].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Schedule.this);
                    builder.setTitle("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    schedules[d].setVisibility(View.GONE);
                                    db.deleteSchedule(d + 1);
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
        }
    }

    public void addSchedule() {
        ArrayList<String> lists = new ArrayList<>();

        AlertDialog.Builder builder = new AlertDialog.Builder(Schedule.this);
        builder.setTitle("Select days");

        String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
        boolean[] checkedItems = {false, false, false, false, false, false, false};

        builder.setMultiChoiceItems(days, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if (checkedItems[0] == true) {
                    add_schedule_days[0] = "Monday";
                    add_schedule_days_int[0] = 1;
                } else {
                    add_schedule_days[0] = "";
                    add_schedule_days_int[0] = 0;
                }

                if (checkedItems[1] == true) {
                    add_schedule_days[1] = "Tuesday";
                    add_schedule_days_int[1] = 1;
                } else {
                    add_schedule_days[1] = "";
                    add_schedule_days_int[1] = 0;
                }

                if (checkedItems[2] == true) {
                    add_schedule_days[2] = "Wednesday";
                    add_schedule_days_int[2] = 1;
                } else {
                    add_schedule_days[2] = "";
                    add_schedule_days_int[2] = 0;
                }

                if (checkedItems[3] == true) {
                    add_schedule_days[3] = "Thursday";
                    add_schedule_days_int[3] = 1;
                } else {
                    add_schedule_days[3] = "";
                    add_schedule_days_int[3] = 0;
                }

                if (checkedItems[4] == true) {
                    add_schedule_days[4] = "Friday";
                    add_schedule_days_int[4] = 1;
                } else {
                    add_schedule_days[4] = "";
                    add_schedule_days_int[4] = 0;
                }

                if (checkedItems[5] == true) {
                    add_schedule_days[5] = "Saturday";
                    add_schedule_days_int[5] = 1;
                } else {
                    add_schedule_days[5] = "";
                    add_schedule_days_int[5] = 0;
                }

                if (checkedItems[6] == true) {
                    add_schedule_days[6] = "Sunday";
                    add_schedule_days_int[6] = 1;
                } else {
                    add_schedule_days[6] = "";
                    add_schedule_days_int[6] = 0;
                }
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (add_schedule_days[0] == "" && add_schedule_days[1] == "" && add_schedule_days[2] == "" && add_schedule_days[3] == "" && add_schedule_days[4] == ""
                        && add_schedule_days[5] == "" && add_schedule_days[6] == "") {
                    Toast.makeText(getApplicationContext(), "Please Enter A Day", Toast.LENGTH_LONG).show();
                } else {
                    timePickerDialog = new TimePickerDialog(Schedule.this, Schedule.this, hour, minute, DateFormat.is24HourFormat(getApplicationContext()));
                    timePickerDialog.show();
                }

            }
        });
        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Intent intent = new Intent(Schedule.this, ReminderBroadcast.class);
        Calendar[] calendar = new Calendar[7];

        for (int i = 0; i < 7; i++) {
            calendar[i] = Calendar.getInstance();
        }

        PendingIntent[] pendingIntent = new PendingIntent[7];
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);


        hourFinal = hourOfDay;
        minuteFinal = minute;

        if (minuteFinal == 0) {
            minuteFinal = 00;
        }

        if (hourFinal == 0) {
            hourFinal = 00;
        }

        for (int i = 0; i < 10; i++) {
            if (schedules[i].getVisibility() == View.GONE) {
                String d = "";
                schedules[i].setVisibility(View.VISIBLE);
                schedule_text[i].setText((hourFinal <= 9 ? ("0" + hourFinal) : hourFinal) + ":" + (minuteFinal <= 9 ? ("0" + minuteFinal) : minuteFinal));
                schedule_switch[i].setChecked(true);


                for (int c = 0; c < 7; c++) {
                    int dd = 0;
                    if (add_schedule_days[c].length() > 0) {
                        if (d == "") {
                            d = add_schedule_days[c].substring(0, 3);
                        } else {
                            d = d + ", " + add_schedule_days[c].substring(0, 3);
                        }

                        if (c == 0) {
                            calendar[dd].set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            calendar[dd].set(Calendar.HOUR_OF_DAY, hourFinal);
                            calendar[dd].set(Calendar.MINUTE, minuteFinal);
                            pendingIntent[dd] = PendingIntent.getBroadcast(Schedule.this, i + 1, intent, 0);
                            alarmManager.setRepeating(AlarmManager.RTC, calendar[dd].getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent[dd]);
                        } else if (c == 1) {
                            calendar[dd].set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            calendar[dd].set(Calendar.HOUR_OF_DAY, hourFinal);
                            calendar[dd].set(Calendar.MINUTE, minuteFinal);
                            pendingIntent[dd] = PendingIntent.getBroadcast(Schedule.this, i + 2, intent, 0);
                            alarmManager.setRepeating(AlarmManager.RTC, calendar[dd].getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent[dd]);
                        } else if (c == 2) {
                            calendar[dd].set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                            calendar[dd].set(Calendar.HOUR_OF_DAY, hourFinal);
                            calendar[dd].set(Calendar.MINUTE, minuteFinal);
                            pendingIntent[dd] = PendingIntent.getBroadcast(Schedule.this, i + 3, intent, 0);
                            alarmManager.setRepeating(AlarmManager.RTC, calendar[dd].getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent[dd]);
                        } else if (c == 3) {
                            calendar[dd].set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                            calendar[dd].set(Calendar.HOUR_OF_DAY, hourFinal);
                            calendar[dd].set(Calendar.MINUTE, minuteFinal);
                            pendingIntent[dd] = PendingIntent.getBroadcast(Schedule.this, i + 4, intent, 0);
                            alarmManager.setRepeating(AlarmManager.RTC, calendar[dd].getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent[dd]);
                        } else if (c == 4) {
                            calendar[dd].set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                            calendar[dd].set(Calendar.HOUR_OF_DAY, hourFinal);
                            calendar[dd].set(Calendar.MINUTE, minuteFinal);
                            pendingIntent[dd] = PendingIntent.getBroadcast(Schedule.this, i + 5, intent, 0);
                            alarmManager.setRepeating(AlarmManager.RTC, calendar[dd].getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent[dd]);
                        } else if (c == 5) {
                            calendar[dd].set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            calendar[dd].set(Calendar.HOUR_OF_DAY, hourFinal);
                            calendar[dd].set(Calendar.MINUTE, minuteFinal);
                            pendingIntent[dd] = PendingIntent.getBroadcast(Schedule.this, i + 6, intent, 0);
                            alarmManager.setRepeating(AlarmManager.RTC, calendar[dd].getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent[dd]);
                        } else if (c == 6) {
                            calendar[dd].set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            calendar[dd].set(Calendar.HOUR_OF_DAY, hourFinal);
                            calendar[dd].set(Calendar.MINUTE, minuteFinal);
                            pendingIntent[dd] = PendingIntent.getBroadcast(Schedule.this, i + 7, intent, 0);
                            alarmManager.setRepeating(AlarmManager.RTC, calendar[dd].getTimeInMillis(), 7 * 24 * 3600 * 1000, pendingIntent[dd]);
                        }

                        db.addSchedule(hourFinal, minuteFinal, add_schedule_days_int[0], add_schedule_days_int[1], add_schedule_days_int[2], add_schedule_days_int[3],
                                add_schedule_days_int[4], add_schedule_days_int[5], add_schedule_days_int[6], 1);

                        dd++;

                    }

                    add_schedule_days[c] = "";
                    add_schedule_days_int[c] = 0;

                }

                schedules_days[i].setText(d);
                break;
            }
        }


    }

    private void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Workout Buddy Channel";
            String description = "Channel For Workout Buddy";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("notifyWorkoutBuddy", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }
    }


    public void bottomNav() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.schedule);
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

