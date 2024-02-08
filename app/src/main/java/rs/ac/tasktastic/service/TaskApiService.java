package rs.ac.tasktastic.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rs.ac.tasktastic.dto.Task;

public interface TaskApiService {

    @GET("/api/tasks/users/{id}")
    Call<List<Task>> getTasksByUserId(@Path("id") String userId);
}
