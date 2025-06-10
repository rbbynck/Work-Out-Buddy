package com.example.workoutbuddy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "workout_buddy";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public long addUser(String name, String gender, double initial_weight,
                        double initial_height, long bmi, String workoutPlan) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("gender", gender);
        contentValues.put("initial_weight", initial_weight);
        contentValues.put("initial_height", initial_height);
        contentValues.put("bmi", bmi);
        contentValues.put("workout_plan", workoutPlan);
        long res = db.insert("user_profiles", null, contentValues);
        db.close();
        return res;
    }

    public Cursor viewProfile() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM user_profiles";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor knowIfWeeklyProgressNeedsInput() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT month_week_no FROM weekly_progress";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public void addWeeklyProgress1(double weight, double height) {
        Calendar calendar = Calendar.getInstance();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("week_no", 1);
        contentValues.put("date_day", calendar.get(Calendar.DAY_OF_WEEK) + 1);
        contentValues.put("date_month", calendar.get(Calendar.MONTH) + 1);
        contentValues.put("date_year", calendar.get(Calendar.YEAR));
        contentValues.put("weight", weight);
        contentValues.put("height", height);
        contentValues.put("month_week_no", Calendar.WEEK_OF_MONTH);
        db.insert("weekly_progress", null, contentValues);
    }


    public void addWeeklyProgress(double weight, double height) {
        int weekNumber = getWeekNoInWeeklyProgress();
        Calendar calendar = Calendar.getInstance();
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("week_no", weekNumber);
        contentValues.put("date_day", calendar.get(Calendar.DAY_OF_MONTH) + 1);
        contentValues.put("date_month", calendar.get(Calendar.MONTH) + 1);
        contentValues.put("date_year", calendar.get(Calendar.YEAR));
        contentValues.put("weight", weight);
        contentValues.put("height", height);
        contentValues.put("month_week_no", Calendar.WEEK_OF_MONTH);
        db.insert("weekly_progress", null, contentValues);
    }

    public void updateWorkOutPlan(int day, String status, float calories_lose, int progress) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("status", status);
        contentValues.put("calories_lose", calories_lose);
        contentValues.put("progress", progress);
        db.update("workout_plan", contentValues, "day=" + day, null);
    }

    public String viewWorkoutPlan_Status(int day) {
        String status = "";
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT status FROM workout_plan WHERE day=" + day;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            status = cursor.getString(0);
        }

        return status;
    }

    public Cursor viewWorkout_Progress() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT progress FROM workout_plan";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public int viewWeeklyProgress_WeekNo() {
        int weekNo = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT week_no FROM weekly_progress";
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            weekNo = cursor.getInt(0);
        }

        return weekNo;
    }

    public float viewWeeklyProgress_Height(int weekNo) {
        float height = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT height FROM weekly_progress WHERE week_no=" + weekNo;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            height = cursor.getFloat(0);
        }

        return height;
    }


    public float viewWeeklyProgress_Weight(int weekNo) {
        float weight = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT weight FROM weekly_progress WHERE week_no=" + weekNo;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            weight = cursor.getFloat(0);
        }

        return weight;
    }


    public void addWorkoutHistory(float calories_lose) {
        float calories = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Calendar calendar = Calendar.getInstance();
        String query = "SELECT calories FROM workout_history WHERE date_month=" + (calendar.get(Calendar.MONTH) + 1) + " AND date_day="
                + (calendar.get(Calendar.DAY_OF_MONTH) + 1) + " AND date_year=" + calendar.get(Calendar.YEAR);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                calories = cursor.getFloat(0);
            }
            contentValues.put("calories", calories_lose + calories);
            db.update("workout_history", contentValues, "date_month=" + (calendar.get(Calendar.MONTH) + 1) + " AND date_day=" +
                    +(calendar.get(Calendar.DAY_OF_MONTH) + 1) + " AND date_year=" + calendar.get(Calendar.YEAR), null);
        } else {
            contentValues.put("date_month", calendar.get(Calendar.MONTH) + 1);
            contentValues.put("date_day", calendar.get(Calendar.DAY_OF_MONTH) + 1);
            contentValues.put("date_year", calendar.get(Calendar.YEAR));
            contentValues.put("calories", calories_lose);
            db.insert("workout_history", null, contentValues);
        }

    }


    public Cursor viewWorkoutHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String query = "SELECT date_month, date_day, date_year, calories FROM workout_history";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public int viewDayActiveWorkoutPlan() {
        int day = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT day FROM workout_plan WHERE status='Active'";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            day = cursor.getInt(0);
        }
        return day;
    }

    public Cursor viewBMI() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT bmi FROM user_profiles";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public int getWeekNoInWeeklyProgress() {
        int weekNo = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT week_no FROM weekly_progress";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            weekNo = cursor.getInt(0);
        }

        return weekNo + 1;
    }

    public Cursor viewData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM user_profiles";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


    public Cursor viewSchedule() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT schedule_no FROM schedule";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor viewSchedule_Status() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT schedule_no FROM schedule WHERE schedule_status=1";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public int viewSchedule_Minute(int schedule_no) {
        int minute = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT date_minute FROM schedule WHERE schedule_no=" + schedule_no;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                minute = cursor.getInt(0);
            }
        }

        return minute;
    }


    public int viewSchedule_Hour(int schedule_no) {
        int hour = 0;

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT date_hour FROM schedule WHERE schedule_no=" + schedule_no;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                hour = cursor.getInt(0);
            }
        }

        return hour;
    }


    public String viewSchedule_Days(int schedule_no) {
        String d = "";
        int[] days = {0, 0, 0, 0, 0, 0, 0,};
        String[] days_string = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT day_monday, day_tuesday, day_wednesday, day_thursday, day_friday, day_saturday, day_sunday FROM schedule WHERE schedule_no=" + schedule_no;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                days[0] = cursor.getInt(0);
                days[1] = cursor.getInt(1);
                days[2] = cursor.getInt(2);
                days[3] = cursor.getInt(3);
                days[4] = cursor.getInt(4);
                days[5] = cursor.getInt(5);
                days[6] = cursor.getInt(6);
            }
        }

        for (int i = 0; i < 7; i++) {
            if (days[i] == 1) {
                if (d == "") {
                    d = days_string[i];
                } else {
                    d = d + ", " + days_string[i];
                }
            }
        }

        return d;
    }


    public int[] viewSchedule_DaysInt(int schedule_no) {
        int[] days = {0, 0, 0, 0, 0, 0, 0,};

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT day_monday, day_tuesday, day_wednesday, day_thursday, day_friday, day_saturday, day_sunday FROM schedule WHERE schedule_no=" + schedule_no;
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                days[0] = cursor.getInt(0);
                days[1] = cursor.getInt(1);
                days[2] = cursor.getInt(2);
                days[3] = cursor.getInt(3);
                days[4] = cursor.getInt(4);
                days[5] = cursor.getInt(5);
                days[6] = cursor.getInt(6);
            }
        }

        return days;
    }


    public void addSchedule(int hour, int minute, int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("date_hour", hour);
        contentValues.put("date_minute", minute);
        contentValues.put("day_monday", monday);
        contentValues.put("day_tuesday", tuesday);
        contentValues.put("day_wednesday", wednesday);
        contentValues.put("day_thursday", thursday);
        contentValues.put("day_friday", friday);
        contentValues.put("day_saturday", saturday);
        contentValues.put("day_sunday", sunday);
        contentValues.put("schedule_status", status);
        db.insert("schedule", null, contentValues);
        db.close();
    }


    public void updateSchedule_Status(int schedule_no, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("schedule_status", status);
        db.update("schedule", contentValues, "schedule_no=" + schedule_no, null);
    }


    public void deleteSchedule(int schedule_no) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("schedule", "schedule_no=" + schedule_no, null);
    }


    public void addPersonalizeExercise(String title, int sets_no, int work_time, int rest_time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", title);
        contentValues.put("sets_no", sets_no);
        contentValues.put("work_time", work_time);
        contentValues.put("rest_time", rest_time);
        db.insert("personalize_exercise", null, contentValues);
    }


    public Cursor viewPersonalizeExercise() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT title FROM personalize_exercise";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public Cursor viewPersonalizeExercise(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT sets_no, work_time, rest_time FROM personalize_exercise WHERE title='" + title + "'";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public Cursor motivationQuotes() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT motivation_text FROM motivational_quotes";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public Cursor viewLose(int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT exercise_name, exercise_drawable, exercise_calories, exercise_time FROM lose WHERE day=" + day;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor viewLoseTime(int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT minute, exercise_no FROM lose_time WHERE day=" + day;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor viewWorkoutPlan() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT workout_plan_number FROM user_profiles";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public Cursor viewGain(int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT exercise_name, exercise_drawable, exercise_calories, exercise_time FROM gain WHERE day=" + day;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }

    public Cursor viewGainTime(int day) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT minute, exercise_no FROM gain_time WHERE day=" + day;
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public Cursor viewPlaylist() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT name FROM playlist";
        Cursor cursor = db.rawQuery(query, null);

        return cursor;
    }


    public void addPlaylist(String playlist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", playlist);
        db.insert("playlist", null, contentValues);
    }
}



