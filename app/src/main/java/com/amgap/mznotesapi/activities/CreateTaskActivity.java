package com.amgap.mznotesapi.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amgap.mznotesapi.DatePickerListener;
import com.amgap.mznotesapi.R;
import com.amgap.mznotesapi.utils.ActivityHelper;
import com.amgap.mznotesapi.utils.DatabaseHandler;
import com.amgap.mznotesapi.utils.DateFormatter;

import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

// Sonarlint java:S110 - Sans ça l'appli crash
@SuppressWarnings("java:S110")
public class CreateTaskActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        final AppCompatActivity activity = this;

        final TextView dateText = findViewById(R.id.fieldTaskEnd);
        final DatePickerListener datePickerListener = new DatePickerListener(dateText);
        findViewById(R.id.fieldTaskEnd).setOnClickListener(v ->
                ActivityHelper.showDatePickerDialog(activity, datePickerListener));

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

    enum TaskCreationStatus {
        OK(R.string.status_create_ok),
        EMPTY_FIELDS(R.string.status_create_empty_fields),
        DATE_FORMAT(R.string.status_create_date_format),
        ERROR(R.string.status_create_error);

        private final int statusId;
        TaskCreationStatus(int statusId) {
            this.statusId = statusId;
        }
        public int getStatusId() {
            return statusId;
        }
    }

    private TaskCreationStatus createTask() {
        try (DatabaseHandler db = DatabaseHandler.get(this.getApplicationContext())) {
            final List<Integer> fieldsId = Arrays.asList(
                    R.id.fieldTaskName,
                    R.id.fieldTaskEnd,
                    R.id.fieldTaskDescription);
            // ordre --> nom, deadline, description
            final List<String> values = fieldsId.stream()
                    .map(this::getEditTextValue)
                    .collect(Collectors.toList());

            if (values.stream().anyMatch(String::isEmpty)) {
                return TaskCreationStatus.EMPTY_FIELDS;
            }

            final String name = values.get(0);
            final String deadline = values.get(1);
            final String description = values.get(2);

            if (!isDeadlineCorrect(deadline)) {
                return TaskCreationStatus.DATE_FORMAT;
            }

            final boolean succeed = db.createTask(name, description, DateFormatter.parse(deadline)) != -1;

            return succeed ? TaskCreationStatus.OK : TaskCreationStatus.ERROR;
        } catch (Exception e) {
            return TaskCreationStatus.ERROR;
        }
    }

    private boolean isDeadlineCorrect(String deadline) {
        final String[] split = deadline.split("/");
        if (split.length != 3) {
            return false;
        }

        final int day = Integer.parseInt(split[0]);
        final int month = Integer.parseInt(split[1]);

        return split[0].length() == 2
                && split[1].length() == 2
                && split[2].length() == 4
                && day > 0
                && day <= 31
                && month > 0
                && month <= 12;
    }

    @Override
    public void onClick(View v) {}
}
