package rs.ac.tasktastic.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import rs.ac.tasktastic.dto.User;

public interface UserApiService {

    @GET("/api/users")
    Call<List<User>> getUsers();
}
