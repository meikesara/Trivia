package com.example.trivia;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;

public class HighscoreAdapter extends ResourceCursorAdapter {

    int count = 1;

    public HighscoreAdapter(Context context, Cursor cursor) {
        super(context, R.layout.entry_row, cursor, true);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView username = view.findViewById(R.id.username);
        TextView score = view.findViewById(R.id.score);
        TextView number = view.findViewById(R.id.number);

        int columnIndex = cursor.getColumnIndex("username");
        username.setText(cursor.getString(columnIndex));

        columnIndex = cursor.getColumnIndex("score");
        score.setText(cursor.getString(columnIndex));

        number.setText(Integer.toString(count) + ". " );
        count += 1;
    }
}
