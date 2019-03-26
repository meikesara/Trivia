package com.example.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class HighScoreActivity extends AppCompatActivity {

    // Initialise variables
    HighscoreAdapter adapter;
    HighscoreDatabase db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // Get the intent and the username
        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        // Get the database
        db = HighscoreDatabase.getInstance(getApplicationContext());

        // Create the adapter and set to the ListView
        adapter = new HighscoreAdapter(this, db.selectAll());
        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    // This method is called when the start new game button is pressed
    public void onNew(View view) {
        Intent intent = new Intent(HighScoreActivity.this, GameActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    // This method removes all data from the database and renews the screen
    public void onReset(View view) {
        db.delete();
        updateData();
    }

    private void updateData() {
        adapter.swapCursor(db.selectAll());
    }
}
