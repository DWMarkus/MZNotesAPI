package com.amgap.mznotesapi.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.appcompat.widget.Toolbar;

public class MainActivity {

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
    }
}
