package rs.ac.tasktastic.service;

import com.google.gson.annotations.SerializedName;

public class TaskRequestBody {
    @SerializedName("finished")
    private boolean isFinished;

    public TaskRequestBody(boolean isFinished) {
        this.isFinished = isFinished;
    }
}
