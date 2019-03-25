package com.example.trivia;

import android.content.Intent;
import android.graphics.Color;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Handler;

public class GameActivity extends AppCompatActivity implements TriviaRequest.Callback {

    int questionCount = 1;
    ArrayList<QuestionItem> question = new ArrayList<>();
    QuestionItem currentQuestion;
    int correctCounter = 0;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        TextView number = findViewById(R.id.question);

        number.setText("Question " + questionCount + ":");

        TriviaRequest x =  new TriviaRequest(this);
        x.getQuestion(this);
    }

    @Override
    public void gotQuestion(ArrayList<QuestionItem> questionInitial) {

        // Display question
        displayQuestion(questionInitial);

        question = questionInitial;

        // Count the questions
        questionCount += 1;
    }

    @Override
    public void gotQuestionError(String message) {
        // If there was a problem loading the categories display the error
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    public void onAnswer(View view) {
        Button button = (Button) view;

        if (correctAnswer(button.getText().toString())) {
            correctCounter += 1;
        }

        if (!gameOver()) {

            // Count the questions
            questionCount += 1;

            // Display question
            displayQuestion(question);
        }
        else {
            HighscoreItem highscore = new HighscoreItem(username, correctCounter);
            HighscoreDatabase db = HighscoreDatabase.getInstance(this);

            db.insert(highscore);
            db.selectAll();

            Intent intent = new Intent(GameActivity.this, HighScoreActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }
    }

    public boolean correctAnswer(String answer) {
        if (answer.equals(currentQuestion.getCorrect_answer())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean gameOver() {
        if (questionCount >= 10) {
            return true;
        } else {
            return false;
        }
    }

    public void displayQuestion(ArrayList<QuestionItem> question) {

        // Choose random QuestionItem
        Random rand = new Random();
        int questionNum = rand.nextInt(question.size());
        currentQuestion = question.get(questionNum);

        // Remove questionItem from the Arraylist
        question.remove(questionNum);

        TextView questionText = findViewById(R.id.question_current);
        Button answer1 = findViewById(R.id.answer1);
        Button answer2 = findViewById(R.id.answer2);
        Button answer3 = findViewById(R.id.answer3);
        Button answer4 = findViewById(R.id.answer4);
        TextView number = findViewById(R.id.question);

        questionText.setText(Html.fromHtml(currentQuestion.getQuestion(), Html.FROM_HTML_MODE_LEGACY));
        number.setText("Question " + questionCount + ":");

        int max = 3;
        int min = 0;

        int value = rand.nextInt((max - min) + 1) + min;
        List<Integer> places = new ArrayList<>();
        places.add(1);
        places.add(2);
        places.add(3);
        places.add(4);

        String correct_answer = Html.fromHtml(currentQuestion.getCorrect_answer(),Html.FROM_HTML_MODE_LEGACY).toString();

        if (places.get(value) == 1) {
            answer1.setText(correct_answer);
        } else if (places.get(value) == 2) {
            answer2.setText(correct_answer);
        } else if (places.get(value) == 3) {
            answer3.setText(correct_answer);
        } else if (places.get(value) == 4) {
            answer4.setText(correct_answer);
        }

        places.remove(places.get(value));
        max = max - 1;

        for (int j = 0; j < 3; j++) {

            value = rand.nextInt((max - min) + 1) + min;
            String incorrect_answer = Html.fromHtml(currentQuestion.getIncorrect_answers().get(j), Html.FROM_HTML_MODE_LEGACY).toString();

            if (places.get(value) == 1) {
                answer1.setText(incorrect_answer);
            } else if (places.get(value) == 2) {
                answer2.setText(incorrect_answer);
            } else if (places.get(value) == 3) {
                answer3.setText(incorrect_answer);
            } else if (places.get(value) == 4) {
                answer4.setText(incorrect_answer);
            }

            places.remove(places.indexOf(places.get(value)));

            max = max - 1;
        }
    }
}
