package rs.ac.tasktastic.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import rs.ac.tasktastic.dto.Task;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    private final MutableLiveData<List<Task>> taskListLiveData;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Welcome to Task-tastic!\nLet's get productive!");
        taskListLiveData = new MutableLiveData<>();
    }

    // Method to observe the taskList
    public LiveData<List<Task>> getTaskList() {
        return taskListLiveData;
    }

    // Method to update the taskList
    public void updateTaskList(List<Task> tasks) {
        taskListLiveData.setValue(tasks);
    }
    public LiveData<String> getText() {
        return mText;
    }
}