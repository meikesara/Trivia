package com.example.trivia;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TriviaRequest implements Response.Listener<JSONObject>, Response.ErrorListener{

    // Initialise variables
    private Callback activity;
    private Context context;
    private ArrayList<QuestionItem> listQuestionItem = new ArrayList<>();

    // Create callback
    public interface Callback {
        void gotQuestion(ArrayList<QuestionItem> question);
        void gotQuestionError(String message);
    }

    // Create constructor
    public TriviaRequest(Context context) {
        this.context = context;
    }

    public void getQuestion(Callback activity) {
        // Create a RequestQueue
        RequestQueue queue = Volley.newRequestQueue(context);

        // Create jsonObjectRequests
        String url = "https://opentdb.com/api.php?amount=50&category=9&type=multiple";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, this, this);
        queue.add(jsonObjectRequest);

        // Set the activity
        this.activity =  activity;
    }

    // This method is called when there is an error with the volley
    @Override
    public void onErrorResponse(VolleyError error) {
        activity.gotQuestionError(error.getMessage());
        error.printStackTrace();
    }

    // This method is called when there is no error with the volley
    @Override
    public void onResponse(JSONObject response) {

        try {
            // Get a JSONArray from the response
            JSONArray QuestionsArray = response.getJSONArray("results");

            // Extract the QuestionItems from the JSONArray an add to the list
            for (int i = 0; i < QuestionsArray.length(); i++) {
                JSONObject item = QuestionsArray.getJSONObject(i);

                ArrayList<String> incorrect_answers = new ArrayList<>();

                JSONArray incorrect = item.getJSONArray("incorrect_answers");
                for (int j = 0; j < incorrect.length(); j++) {
                    incorrect_answers.add(incorrect.get(j).toString());
                }
                QuestionItem questionItem = new QuestionItem(item.getString("question"), item.getString("correct_answer"),
                        incorrect_answers);

                listQuestionItem.add(questionItem);
            }
            activity.gotQuestion(listQuestionItem);
        } catch (JSONException e){
            e.printStackTrace();
        }
    }
}