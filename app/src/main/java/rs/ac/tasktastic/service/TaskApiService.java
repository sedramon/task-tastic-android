package rs.ac.tasktastic.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rs.ac.tasktastic.dto.Task;

public interface TaskApiService {

    @GET("/api/tasks/users/{id}")
    Call<List<Task>> getTasksByUserId(@Path("id") String userId);

    @POST("/api/tasks")
    Call<Task> addTask(@Body Task task);

    @DELETE("/api/tasks/{id}")
    Call<Void> deleteTask(@Path("id") String taskId);

    @PUT("/api/tasks/{id}")
    Call<Void> updateTaskIsFinished(@Path("id") String taskId, @Body TaskRequestBody taskRequestBody);
}
