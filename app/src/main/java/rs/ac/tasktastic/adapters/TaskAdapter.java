package rs.ac.tasktastic.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.tasktastic.R;
import rs.ac.tasktastic.dto.Task;
import rs.ac.tasktastic.service.TaskApiService;
import rs.ac.tasktastic.service.TaskRequestBody;

// TaskAdapter.java
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> implements Filterable {

    private List<Task> taskList;
    private List<Task> filteredTasks;

    public TaskAdapter(List<Task> taskList) {
        this.taskList = taskList;
        this.filteredTasks = taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
        this.filteredTasks = taskList;
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
        Task task = filteredTasks.get(position);


        // Populate the ViewHolder with data from the Task object
        holder.textViewTitle.setText(task.getTitle());
        holder.textViewDescription.setText(task.getDescription());
        if (!task.isFinished()) {
            holder.textViewIsFinished.setText("Status : Not completed...");
        } else {
            holder.textViewIsFinished.setText("Status : Completed!");
        }

        holder.deleteButton.setOnClickListener(view -> {
            String taskId = task.getId();
            deleteTask(taskId, position);
        });

        holder.setNotFinished.setOnClickListener(view -> {
            setIsFinished(task.getId(), false);
        });

        holder.setFinished.setOnClickListener(view -> {
            setIsFinished(task.getId(), true);
        });
        // Add more fields as needed
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        String formattedEndDate = dateFormat.format(task.getEndDate());
        holder.textViewEndDate.setText("Deadline : " + formattedEndDate);

        // You can also set click listeners or perform other actions here if needed
    }

    @Override
    public int getItemCount() {
        return filteredTasks.size();
    }



    // ViewHolder class to hold the views for each item
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewDescription, textViewEndDate, textViewIsFinished;
        // Add more views as needed
        // Your ViewHolder views and delete button
        Button deleteButton, setNotFinished, setFinished;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDate);
            textViewIsFinished = itemView.findViewById(R.id.textViewIsFinished);
            deleteButton = itemView.findViewById(R.id.buttonDeleteTask);
            setNotFinished = itemView.findViewById(R.id.buttonSetNotCompleted);
            setFinished = itemView.findViewById(R.id.buttonSetFinished);
            // Initialize more views here if needed
        }
    }

    private void deleteTask(String taskId, int position) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.53:8080")
                .build();
        TaskApiService taskApiService = retrofit.create(TaskApiService.class);

        Call<Void> call = taskApiService.deleteTask(taskId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                taskList.remove(position);
                // Notify the adapter about the item removal
                notifyItemRemoved(position);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    private void setIsFinished(String taskId, boolean status) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.53:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TaskApiService taskApiService = retrofit.create(TaskApiService.class);


        TaskRequestBody requestBody = new TaskRequestBody(status);
        Call<Void> call = taskApiService.updateTaskIsFinished(taskId, requestBody);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    for (Task task : taskList) {
                        if (task.getId() == taskId) {
                            task.setFinished(status);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    // Handle unsuccessful response
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().toLowerCase();

                if (query.isEmpty()) {
                    filteredTasks = taskList;
                } else {
                    List<Task> filteredList = new ArrayList<>();
                    for (Task task : taskList) {
                        // Implement your custom filtering logic here
                        if (task.getTitle().toLowerCase().contains(query) || task.getDescription().toLowerCase().contains(query)) {
                            filteredList.add(task);
                        }
                    }
                    filteredTasks = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredTasks;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredTasks = (List<Task>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
