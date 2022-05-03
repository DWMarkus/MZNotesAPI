package com.amgap.mznotesapi.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.widget.Toolbar;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.amgap.mznotesapi.R;
import com.amgap.mznotesapi.utils.ActivityHelper;
import com.amgap.mznotesapi.TaskListFragment;
import com.amgap.mznotesapi.utils.Preferences;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

@SuppressWarnings({"java:S110", "java:S2696"})
public class MainActivity extends LocalizationActivity {
    private static MainActivity instance;
    private static boolean firstRun = false;

    private static final String HIDE_TASKS_PREFERENCES = "PrefersToHideTasks";

    public static MainActivity getInstance() {
        return instance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        boolean hideUnfinishedTasks = Preferences.getBoolean(getApplicationContext(),
                HIDE_TASKS_PREFERENCES, false);

        // dernier langage choisi au startup
        if (!firstRun) {
            setLanguage(Locale.getDefault());
            TaskListFragment.setHideUnfinishedTasks(hideUnfinishedTasks);
            firstRun = true;
        }

        setContentView(R.layout.activity_main);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> ActivityHelper.changeActivity(this, CreateTaskActivity.class));

        final Switch hideUnfinished = findViewById(R.id.hide_switch);
        hideUnfinishedTasks |= TaskListFragment.isHidingUnfinishedTasks();
        hideUnfinished.setChecked(hideUnfinishedTasks);
        hideUnfinished.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (TaskListFragment.isHidingUnfinishedTasks() != isChecked) {
                TaskListFragment.setHideUnfinishedTasks(isChecked);
                Preferences.saveBoolean(getApplicationContext(), HIDE_TASKS_PREFERENCES, isChecked);
                ActivityHelper.refresh(instance);
            }
        });

        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                ActivityHelper.changeActivity(this, ChangeLangActivity.class);
                return true;
            case R.id.action_weather:
                ActivityHelper.changeActivity(this, WeatherActivity.class);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

