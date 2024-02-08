package rs.ac.tasktastic.ui.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import rs.ac.tasktastic.adapters.TaskAdapter;
import rs.ac.tasktastic.databinding.FragmentTaskBinding;
import rs.ac.tasktastic.dto.Task;

public class TaskFragment extends Fragment {

    private FragmentTaskBinding binding;
    private TaskViewModel taskViewModel;

    private RecyclerView recyclerViewTask;
    private TaskAdapter taskAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        taskViewModel =
                new ViewModelProvider(requireActivity()).get(TaskViewModel.class);

        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textViewTask;
        taskViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        recyclerViewTask = binding.recyclerViewTask;
        recyclerViewTask.setLayoutManager(new LinearLayoutManager(getActivity()));

        taskAdapter = new TaskAdapter(new ArrayList<>());
        recyclerViewTask.setAdapter(taskAdapter);
        return root;
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
}