package com.hackathon.hackathon2014.webservice.task;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.hackathon.hackathon2014.model.Question;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class QuestionRequestTask extends AsyncTask<Void,Void,List<Question>> {

    private final String url = "http://api.radar.codedeck.com/questions";
    private RestTemplate restTemplate;
    private PostRequestHandler<List<Question>> handler;

    public QuestionRequestTask(RestTemplate restTemplate ,PostRequestHandler<List<Question>> handler) {
        this.restTemplate = restTemplate;
        this.handler = handler;
    }

    @Override
    protected List<Question> doInBackground(Void... voids) {
        List<Question> questions = new ArrayList<Question>();

        try {
            questions.addAll(Arrays.asList(restTemplate.getForObject(url, Question[].class)));
        } catch (Exception e) {
            String error = "Fail !!!!!!" + e.getMessage();
            Log.e(this.getClass().getName(), error);
        }

        return questions;
    }

    @Override
    protected void onPostExecute(List<Question> questions) {
        handler.handle(questions);
    }
}
