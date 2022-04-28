package com.amgap.mznotesapi.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amgap.mznotesapi.DatePickerListener;
import com.amgap.mznotesapi.R;
import com.amgap.mznotesapi.utils.ActivityHelper;
import com.google.android.material.snackbar.Snackbar;

public class CreateTaskActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        final AppCompatActivity activity = this;
        final DatePickerListener datePickerListener = new DatePickerListener(dataText);
        findViewById(R.id.fieldTaskEnd).setOnClickListener(v -> ActivityHelper.showDatePickerDialog(activity, datePickerListener));

        ActivityHelper.addBackButton(getSupportActionBar());

        findViewById(R.id.buttonCreate).setOnClickListener(listener -> {
            final TaskCreationStatus statusCode = createTask();
            final String status = getString(statusCode.getStatusId());
            final Snackbar snack = Snackbar.make(listener, status, 350);
            if (statusCode == TaskCreationStatus.OK) {
                snack.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        ActivityHelper.changeActivity(activity, MainActivity.class);
                    }
                });
            }
            snack.show();
        });
    }

    private String getEditTextValue(int id) {
        return ((EditText) findViewById(id)).getText().toString();
    }
}
