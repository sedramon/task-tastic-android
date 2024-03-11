package rs.ac.tasktastic.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rs.ac.tasktastic.R;
import rs.ac.tasktastic.databinding.ActivityMainBinding;
import rs.ac.tasktastic.dto.Task;
import rs.ac.tasktastic.service.CustomDateDeserializer;
import rs.ac.tasktastic.service.TaskApiService;
import rs.ac.tasktastic.ui.home.HomeViewModel;
import rs.ac.tasktastic.ui.tasks.TaskFragment;
import rs.ac.tasktastic.ui.tasks.TaskViewModel;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    private HomeViewModel homeViewModel;
    private TaskViewModel taskViewModel;


    private List<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);


        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_task)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        Intent intent = getIntent();
        View navHeaderView = binding.navView.getHeaderView(0);

        retrieveAndUpdateTasks();



        if (intent != null) {
            if(intent.hasExtra("USERNAME")) {
                String username = intent.getStringExtra("USERNAME");
                TextView textViewNavHeaderUsername = navHeaderView.findViewById(R.id.navHeaderUsername);
                textViewNavHeaderUsername.setText("Welcome, " + username.toUpperCase());
            }
            if(intent.hasExtra("EMAIL")) {
                String email = intent.getStringExtra("EMAIL");
                TextView textViewNavHeaderEmail = navHeaderView.findViewById(R.id.navHeaderEmail);
                textViewNavHeaderEmail.setText(email);
            }
            String userId = intent.getStringExtra("ID");
            taskViewModel.setUserId(userId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void retrieveAndUpdateTasks() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new CustomDateDeserializer())
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.2:8080")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        TaskApiService taskApiService = retrofit.create(TaskApiService.class);

        Call<List<Task>> call = taskApiService.getTasksByUserId(getIntent().getStringExtra("ID"));

        call.enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                if(response.isSuccessful()) {
                    taskList = response.body();
                    if (taskList != null && !taskList.isEmpty()) {
                        homeViewModel.updateTaskList(taskList);
                        taskViewModel.updateTaskList(taskList);

                    } else {
                        Log.d("RetrofitTest", "Task list is empty or null");
                    }
                    for (Task task : taskList) {
                        Log.d("RetrofitTest", "Title : " + task.getTitle());
                        Log.d("RetrofitTest", "Description : " + task.getDescription());
                    }
                } else {
                    Log.d("RetrofitTest", "Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                Log.e("RetrofitTest", "Network Failure: " + t);

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_profile) {
            // Handle profile item click
            Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
            String emailProfile = getIntent().getStringExtra("EMAIL");
            String usernameProfile = getIntent().getStringExtra("USERNAME");
            intent.putExtra("EMAIL", emailProfile);
            intent.putExtra("USERNAME", usernameProfile);
            startActivity(intent);
            return true;
        }

        // Handle other menu item clicks if needed

        return super.onOptionsItemSelected(item);
    }
}