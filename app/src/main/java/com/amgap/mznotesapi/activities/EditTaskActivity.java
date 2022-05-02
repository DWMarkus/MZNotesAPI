package com.amgap.mznotesapi.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amgap.mznotesapi.DatePickerListener;
import com.amgap.mznotesapi.R;
import com.amgap.mznotesapi.models.TaskItem;
import com.amgap.mznotesapi.utils.ActivityHelper;
import com.amgap.mznotesapi.utils.DatabaseHandler;
import com.amgap.mznotesapi.utils.DateFormatter;
import com.google.android.material.snackbar.Snackbar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

// Sonarlint java:S110 - Sans ça l'appli crash
@SuppressWarnings("java:S110")
public class EditTaskActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText findEditText(int id) {
        return (EditText) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);

        final AppCompatActivity activity = this;

        final TextView dateText = findViewById(R.id.fieldTaskEnd);
        final DatePickerListener datePickerListener = new DatePickerListener(dateText);
        findViewById(R.id.fieldTaskEnd).setOnClickListener(v ->
                ActivityHelper.showDatePickerDialog(activity, datePickerListener));

        ActivityHelper.addBackButton(getSupportActionBar());

        final TaskItem item = (TaskItem) getIntent().getSerializableExtra("taskData");
        try (DatabaseHandler db = DatabaseHandler.get(this.getApplicationContext())) {
            final EditText nameField = findEditText(R.id.fieldTaskName);
            final EditText descriptionField = findEditText(R.id.fieldTaskDescription);
            final EditText deadlineField = findEditText(R.id.fieldTaskEnd);
            final CheckBox isFinishedField = findViewById(R.id.fieldIsTaskFinished);
            final SimpleDateFormat formatter = DateFormatter.newFormatter();

            nameField.setText(item.getName());
            descriptionField.setText(item.getDescription());
            deadlineField.setText(formatter.format(item.getDeadline()));
            isFinishedField.setChecked(item.isFinished());

            findViewById(R.id.buttonEdit).setOnClickListener(listener -> {
                Snackbar.make(listener, "To implement", 500).show();
                Date newDeadline = null;
                try {
                    newDeadline = formatter.parse(deadlineField.getText().toString());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final boolean succeed = db.updateTask(item.getId(), nameField.getText().toString(),
                        descriptionField.getText().toString(),
                        newDeadline, isFinishedField.isChecked());
                final int statusCode = succeed
                        ? R.string.status_update_ok
                        : R.string.status_update_error;
                final String status = getString(statusCode);

                Snackbar.make(listener, status, 500).addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        ActivityHelper.changeActivity(activity, MainActivity.class);
                    }
                }).show();
            });

            findViewById(R.id.buttonDelete).setOnClickListener(listener -> {
                final boolean succeed = db.deleteTask(item.getId());
                final int statusCode = succeed
                        ? R.string.status_delete_ok
                        : R.string.status_delete_error;
                final String status = getString(statusCode);

                Snackbar.make(listener, status, 500).addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        super.onDismissed(snackbar, event);
                        ActivityHelper.changeActivity(activity, MainActivity.class);
                    }
                }).show();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) { }
}
