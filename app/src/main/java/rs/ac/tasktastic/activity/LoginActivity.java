package rs.ac.tasktastic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.tasktastic.R;
import rs.ac.tasktastic.dto.User;
import rs.ac.tasktastic.service.UserApiService;

public class LoginActivity extends AppCompatActivity {

    private Button loginButton;
    private EditText editTextUsername, editTextPassword;
    private TextView textViewError;
    private List<User> userList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.2:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        UserApiService userApiService = retrofit.create(UserApiService.class);

        Call<List<User>> call = userApiService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()) {
                    userList = response.body();
                    for ( User user : userList) {
                        Log.d("RetrofitTest", "id : " + user.getId());
                        Log.d("RetrofitTest", "username : " + user.getUsername());
                        Log.d("RetrofitTest", "email : " + user.getEmail());
                        Log.d("RetrofitTest", "password : " + user.getPassword());
                    }
                } else {
                    Log.e("RetrofitTest", "Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("RetrofitTest", "Network Failure: " + t.getMessage());
            }
        });

        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onLoginButtonClicked();
            }
        });

        editTextUsername = findViewById(R.id.usernameEditText);
        editTextPassword = findViewById(R.id.passwordEditText);
        textViewError = findViewById(R.id.textViewLoginError);
    }

    private void onLoginButtonClicked() {
        String username = String.valueOf(editTextUsername.getText());
        String password = String.valueOf(editTextPassword.getText());

        boolean isUserValid = false;

        for (User user : userList) {
            if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                isUserValid = true;
                break;
            }
        }

        if(isUserValid) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            textViewError.setText("Invalid username or password");
            textViewError.setVisibility(View.VISIBLE);
        }
    }
}