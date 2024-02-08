package rs.ac.tasktastic.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import rs.ac.tasktastic.R;
import rs.ac.tasktastic.dto.Task;

// TaskAdapter.java
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private List<Task> taskList;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        notifyDataSetChanged(); // Ensure to notify the adapter of data changes
        Log.d("AdapterDebug", "Task List Size: " + taskList.size());
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = taskList.get(position);

        // Populate the ViewHolder with data from the Task object
        holder.textViewTitle.setText(task.getTitle());
        holder.textViewDescription.setText(task.getDescription());
        if (!task.isFinished()) {
            holder.textViewIsFinished.setText("Status : Not completed...");
        }
        // Add more fields as needed

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String formattedEndDate = dateFormat.format(task.getEndDate());
        holder.textViewEndDate.setText("Deadline : " + formattedEndDate);

        // You can also set click listeners or perform other actions here if needed
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }

    // ViewHolder class to hold the views for each item
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription, textViewEndDate, textViewIsFinished;
        // Add more views as needed

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDate);
            textViewIsFinished = itemView.findViewById(R.id.textViewIsFinished);
            // Initialize more views here if needed
        }
    }
}
