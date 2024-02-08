package rs.ac.tasktastic.ui.tasks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import rs.ac.tasktastic.dto.Task;

public class TaskViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<List<Task>> taskListLiveData;

    public TaskViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is where you can see all your tasks!");
        taskListLiveData = new MutableLiveData<>();
    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<List<Task>> getTaskList() {
        return taskListLiveData;
    }

    public void updateTaskList(List<Task> tasks) {
        taskListLiveData.setValue(tasks);
    }


}