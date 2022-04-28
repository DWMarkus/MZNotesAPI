package com.amgap.mznotesapi.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.amgap.mznotesapi.models.TaskItem;

import java.util.Calendar;
import java.util.Objects;

public class ActivityHelper {
    private ActivityHelper() {
        // hidden
    }

    public static void changeActivity(AppCompatActivity origin, Class<? extends Activity> dest) {
        Intent i = new Intent(origin, dest);
        origin.startActivity(i);
    }

    public static void changeActivity(AppCompatActivity origin, Class<? extends Activity> dest, TaskItem task) {
        Intent i = new Intent(origin, dest);
        i.putExtra("taskData", task);
        origin.startActivity(i);
    }

    // boutton retour
    public static void addBackButton(ActionBar actionBar) {
        Objects.requireNonNull(actionBar);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    // rafraichir l'application
    public static void refresh(AppCompatActivity activity) {
        activity.finish();
        activity.startActivity(activity.getIntent());
        activity.overridePendingTransition(0, 0);
    }

    // choisir la date
    public static void showDatePickerDialog(Context context, DatePickerDialog.OnDateSetListener listener) {
        final Calendar calendar = Calendar.getInstance();
        final DatePickerDialog datePickerDialog = new DatePickerDialog(
                context,
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }
}
