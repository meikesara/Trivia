package com.example.trivia;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements TriviaRequest.Callback {

    // Initialise variables
    int questionCount = 1;
    ArrayList<QuestionItem> question = new ArrayList<>();
    QuestionItem currentQuestion;
    int correctCounter = 0;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Get the intent and the username
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        TextView number = findViewById(R.id.question);

        // Set the number of the question
        number.setText("Question " + questionCount + ":");

        // Load in the trivia questions
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

        // check if the answer was correct and add 1 to the counter
        if (correctAnswer(button.getText().toString())) {
            correctCounter += 1;
        }

        // Check if the game is over
        if (!gameOver()) {

            // Count the questions
            questionCount += 1;

            // Display question
            displayQuestion(question);
        }
        // Go to the Highscore activity if the game is over
        else {
            // Create Highscore Item
            HighscoreItem highscore = new HighscoreItem(username, correctCounter);

            // Save the highscoreItem into the HighscoreDatabase
            HighscoreDatabase db = HighscoreDatabase.getInstance(this);
            db.insert(highscore);

            // Update the database
            db.selectAll();

            // Create the intent and put in the username
            Intent intent = new Intent(GameActivity.this, HighScoreActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
            finish();
        }
    }

    //This method checks whether the given answer is correct
    public boolean correctAnswer(String answer) {
        return (answer.equals(currentQuestion.getCorrect_answer()));
    }


    // This method checks if the game is over
    public boolean gameOver() {
        return (questionCount >= 10);
    }

    // This method handles the displaying of questions
    public void displayQuestion(ArrayList<QuestionItem> question) {

        TextView questionText;
        Button answer1;
        Button answer2;
        Button answer3;
        Button answer4;

        // Choose random QuestionItem
        Random rand = new Random();
        int questionNum = rand.nextInt(question.size());
        currentQuestion = question.get(questionNum);

        // Remove questionItem from the Arraylist
        question.remove(questionNum);

        // Initialiase variables
        questionText = findViewById(R.id.question_current);
        answer1 = findViewById(R.id.answer1);
        answer2 = findViewById(R.id.answer2);
        answer3 = findViewById(R.id.answer3);
        answer4 = findViewById(R.id.answer4);
        TextView number = findViewById(R.id.question);

        // Set the question and the number of the question
        questionText.setText(Html.fromHtml(currentQuestion.getQuestion(), Html.FROM_HTML_MODE_LEGACY));
        number.setText("Question " + questionCount + ":");

        // inititaliase the max and min
        int max = 3;
        int min = 0;

        // Get a random integer from 0 to 3
        int value = rand.nextInt((max - min) + 1) + min;

        // Create a list of places
        List<Integer> places = new ArrayList<>();
        places.add(1);
        places.add(2);
        places.add(3);
        places.add(4);

        // Create a string with the correct answer
        String correct_answer = Html.fromHtml(currentQuestion.getCorrect_answer(),Html.FROM_HTML_MODE_LEGACY).toString();

        // Randomly put the correct answer in one of the four buttons
        if (places.get(value) == 1) {
            answer1.setText(correct_answer);
        } else if (places.get(value) == 2) {
            answer2.setText(correct_answer);
        } else if (places.get(value) == 3) {
            answer3.setText(correct_answer);
        } else if (places.get(value) == 4) {
            answer4.setText(correct_answer);
        }

        // Update the available places and max
        places.remove(places.get(value));
        max = max - 1;


        for (int j = 0; j < 3; j++) {

            // Get a random integer from min to max
            value = rand.nextInt((max - min) + 1) + min;

            // Create variable with the current incorrect answer
            String incorrect_answer = Html.fromHtml(currentQuestion.getIncorrect_answers().get(j), Html.FROM_HTML_MODE_LEGACY).toString();

            // Randomly put the answer on one of the still available places
            if (places.get(value) == 1) {
                answer1.setText(incorrect_answer);
            } else if (places.get(value) == 2) {
                answer2.setText(incorrect_answer);
            } else if (places.get(value) == 3) {
                answer3.setText(incorrect_answer);
            } else if (places.get(value) == 4) {
                answer4.setText(incorrect_answer);
            }

            // Update places and max
            places.remove(places.indexOf(places.get(value)));
            max = max - 1;
        }
    }
}
