package com.amgap.mznotesapi.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;

import androidx.appcompat.widget.Toolbar;

import com.akexorcist.localizationactivity.ui.LocalizationActivity;
import com.amgap.mznotesapi.utils.ActivityHelper;
import com.amgap.mznotesapi.TaskListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.amgap.mznotesapi.utils.Preferences;
import com.amgap.mznotesapi.R;

import java.util.Locale;

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
    }

}
