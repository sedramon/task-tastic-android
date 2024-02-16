package rs.ac.tasktastic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import rs.ac.tasktastic.R;
import rs.ac.tasktastic.dto.User;

public class UserProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);

        Intent intent = getIntent();

        String email = intent.getStringExtra("EMAIL");
        String username = intent.getStringExtra("USERNAME");

        TextView textViewEmail = findViewById(R.id.textViewEmail);
        TextView textViewUsername = findViewById(R.id.textViewUsername);

        // Check if the values are not null before setting them to TextViews
        if (email != null && username != null) {
            textViewEmail.setText(email);
            textViewUsername.setText(username);
        } else {
            textViewEmail.setText("ERROR");
            textViewUsername.setText("ERROR");
            // Handle the case where values are null or not present in the Intent
            // You can display a default value or show an error message.
        }


        Button btnGoBack = findViewById(R.id.goBackButtonUserProfile);
        btnGoBack.setOnClickListener(v -> {
            finish();
        });
    }
}
