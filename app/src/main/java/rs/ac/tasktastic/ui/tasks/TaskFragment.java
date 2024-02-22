package rs.ac.tasktastic.ui.tasks;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.tasktastic.R;
import rs.ac.tasktastic.activity.MainActivity;
import rs.ac.tasktastic.adapters.TaskAdapter;
import rs.ac.tasktastic.databinding.FragmentTaskBinding;
import rs.ac.tasktastic.dto.Task;
import rs.ac.tasktastic.dto.User;
import rs.ac.tasktastic.service.CustomDateDeserializer;
import rs.ac.tasktastic.service.TaskApiService;

public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private TaskViewModel taskViewModel;
    private Calendar calendar;

    private RecyclerView recyclerViewTask;
    private TaskAdapter taskAdapter;
    private SearchView searchView;
    private Task task = new Task();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        taskViewModel =
                new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        final TextView textView = binding.textViewTask;
        taskViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        searchView = binding.searchViewTaskFragment;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                taskAdapter.getFilter().filter(newText);
                return false;
            }
        });



        taskViewModel.getUserId().observe(getViewLifecycleOwner(), userId -> {
            User user = new User();
            user.setId(userId);
            task.setUser(user);
        });

        taskViewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            // Update the adapter's dataset and notify changes
            taskAdapter.setTaskList(tasks);
            taskAdapter.notifyDataSetChanged();
        });

        recyclerViewTask = binding.recyclerViewTask;
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));

        taskAdapter = new TaskAdapter(new ArrayList<>());
        recyclerViewTask.setAdapter(taskAdapter);


        Button buttonAddNewTask = binding.addTaskButton;
        buttonAddNewTask.setOnClickListener(v -> {
            try {
                showAddTaskDialog();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });


        calendar = Calendar.getInstance();



        return root;
    }

    private void showAddTaskDialog() throws ParseException {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        View view = requireActivity().getLayoutInflater().inflate(R.layout.dialog_add_task, null);
        builder.setView(view);


        EditText editTextTitle = view.findViewById(R.id.editTextTitleDialog);
        EditText editTextDescription = view.findViewById(R.id.editTextDescriptionDialog);
        Button buttonAddTask = view.findViewById(R.id.buttonConfirmTask);
        Button pickEndDate = view.findViewById(R.id.pickEndDate);

        pickEndDate.setOnClickListener(v -> {
            try {
                showDatePickerDialog();
                Log.d("MyApp", "pickEndDateButton clicked");
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });


        // DATE FORMATING
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date startDate = new Date();
        String formattedCreatedDate = simpleDateFormat.format(startDate);
        Date parseDate = simpleDateFormat.parse(formattedCreatedDate);

        // CREATING TASK AND SENDING IT TO THE API
        AlertDialog dialog = builder.create();

        buttonAddTask.setOnClickListener(v -> {
            Gson gson = new GsonBuilder()
                    .setDateFormat("dd-MM-yyyy HH:mm:ss")
                    .registerTypeAdapter(Date.class, new CustomDateDeserializer())
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.53:8080")
                            .addConverterFactory(GsonConverterFactory.create(gson))
                                    .build();

            TaskApiService taskApiService = retrofit.create(TaskApiService.class);

            task.setTitle(editTextTitle.getText().toString());
            task.setDescription(editTextDescription.getText().toString());
            task.setCreatedDate(parseDate);

            Call<Task> call = taskApiService.addTask(task);
            call.enqueue(new Callback<Task>() {
                @Override
                public void onResponse(Call<Task> call, Response<Task> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(requireContext(), "Task added succesfully!", Toast.LENGTH_SHORT).show();
                        List<Task> updatedTaskList = new ArrayList<>(taskViewModel.getTaskList().getValue());
                        updatedTaskList.add(response.body());
                        taskViewModel.updateTaskList(updatedTaskList);
                    } else {
                        Toast.makeText(requireContext(), "Failed to add task...", Toast.LENGTH_SHORT).show();
                        Log.d("RETORFIT", response.message());
                    }
                }

                @Override
                public void onFailure(Call<Task> call, Throwable t) {
                    Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("ADDINGTASKTHREAD", "Error : " + t.getMessage());
                }
            });

            Log.d("RETROFIT", task.getTitle());
            Log.d("RETROFIT", task.getDescription());
            Log.d("RETROFIT", task.getCreatedDate().toString());
            Log.d("RETROFIT", task.getEndDate().toString());
            Log.d("RETROFIT", task.getUser().getId());

            dialog.dismiss();
        });

        dialog.show();

    }

    public void showDatePickerDialog() throws ParseException{
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_add_task, null);

        getActivity().runOnUiThread(() -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, month);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                            // Format the selected date
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US);
                            String formatedEndDate = dateFormat.format(calendar.getTime());
                            try {
                                Date formatedEndDateParse = dateFormat.parse(formatedEndDate);
                                task.setEndDate(formatedEndDateParse);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }

                            Toast.makeText(getActivity(), "End date selected: " + formatedEndDate, Toast.LENGTH_SHORT).show();

                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );
            datePickerDialog.show();
        });

    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observe the taskList from the TaskViewModel
        taskViewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            // Update the adapter's dataset and notify changes
            taskAdapter.setTaskList(tasks);
            taskAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addNewTask() {

    }
}