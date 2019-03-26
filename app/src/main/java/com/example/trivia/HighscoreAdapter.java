package com.example.trivia;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class HighscoreAdapter extends ResourceCursorAdapter {

    // Initialise the count
    int count = 1;

    // Create constructor
    public HighscoreAdapter(Context context, Cursor cursor) {
        super(context, R.layout.entry_row, cursor, true);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        // Find the TextViews
        TextView username = view.findViewById(R.id.username);
        TextView score = view.findViewById(R.id.score);
        TextView number = view.findViewById(R.id.number);

        // Set the username textview
        int columnIndex = cursor.getColumnIndex("username");
        username.setText(cursor.getString(columnIndex));

        // Set the score TextView
        columnIndex = cursor.getColumnIndex("score");
        score.setText(cursor.getString(columnIndex));

        number.setText(Integer.toString(count) + ". " );
        count += 1;
    }
}
