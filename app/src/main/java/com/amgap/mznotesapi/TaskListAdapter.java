package com.amgap.mznotesapi;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.amgap.mznotesapi.models.TaskItem;
import com.amgap.mznotesapi.activities.EditTaskActivity;
import com.amgap.mznotesapi.activities.MainActivity;
import com.amgap.mznotesapi.utils.ActivityHelper;

import com.github.antoinejt.exassert.Preconditions;

import java.util.Date;
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.TaskViewHolder> {
    private final List<TaskItem> taskList;

    public TaskListAdapter(List<TaskItem> taskList) {
        this.taskList = taskList;
    }

    private static final int NO_COLOR = 0;

    private static int getColorId(TaskItem item) {
        if (item.isFinished())
            return R.color.colorTaskFinished;

        final Date deadline = item.getDeadline();
        final Date now = new Date();
        if (deadline.before(now))
            return R.color.colorTaskExceeded;
        return NO_COLOR;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Preconditions.requiresUnsigned(position);
        TaskItem item = taskList.get(position);

        holder.getTitle().setText(item.getName());
        holder.getDescription().setText(item.getDescription());
        holder.getDeadline().setText(item.getFormattedDeadline());

        final int colorId = getColorId(item);
        if (colorId != NO_COLOR) {
            final int color = ContextCompat.getColor(holder.itemView.getContext(), colorId);
            holder.itemView.setBackgroundColor(color);

            holder.getDescription().setTextColor(Color.WHITE);
            holder.getDeadline().setTextColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // Sonarlint java:S3655
    //    Doit être présent sinon l'application crash au démarrage
    @SuppressWarnings({"java:S3655"})
    class TaskViewHolder extends RecyclerView.ViewHolder {
        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(view ->
                    ActivityHelper.changeActivity(MainActivity.getInstance(), EditTaskActivity.class,
                            taskList.get(getAdapterPosition())));
        }

        protected TextView getTitle() {
            return itemView.findViewById(R.id.task_title);
        }

        protected TextView getDescription() {
            return itemView.findViewById(R.id.task_desc);
        }

        protected TextView getDeadline() {
            return itemView.findViewById(R.id.task_deadline);
        }
    }
}
