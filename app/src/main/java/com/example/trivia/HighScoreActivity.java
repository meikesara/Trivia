package com.example.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

public class HighScoreActivity extends AppCompatActivity {

    HighscoreAdapter adapter;
    HighscoreDatabase db;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");

        db = HighscoreDatabase.getInstance(getApplicationContext());

        adapter = new HighscoreAdapter(this, db.selectAll());

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    public void onNew(View view) {
        Intent intent = new Intent(HighScoreActivity.this, GameActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
    }

    public void onReset(View view) {
        db.delete();
        updateData();
    }

    private void updateData() {
        adapter.swapCursor(db.selectAll());
    }
}
