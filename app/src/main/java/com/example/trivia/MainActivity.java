package com.example.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // Initialise variables
    String username;
    EditText nameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // This method is started when the user presses one of the two buttons in the MainActivity
    public void onStartGame(View view) {
        // Get the username that was filled in
        nameText = findViewById(R.id.username);
        username = nameText.getText().toString();

        TextView textview = (TextView) view;

        // If the username (EditText) is empty let user know it needs to be filled in
        if (username.equals("")) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
        }
        // Start the GameActivity or HighscoreActivity if username is filled in
        else {
            Intent intent;
            if (textview.getText().toString().equals("Start game")) {
                intent = new Intent(MainActivity.this, GameActivity.class);
            } else {
                intent = new Intent(MainActivity.this,GameActivity.class);
            }
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }
    }
}
