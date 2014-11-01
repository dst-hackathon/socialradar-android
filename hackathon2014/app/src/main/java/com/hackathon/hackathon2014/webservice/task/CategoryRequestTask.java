package com.hackathon.hackathon2014.webservice.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.hackathon.hackathon2014.model.Category;
import com.hackathon.hackathon2014.model.Question;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class CategoryRequestTask extends AsyncTask<Question,Void,Question> {

    private final String url = "http://api.radar.codedeck.com/questions/";

    private RestTemplate restTemplate;

    private PostRequestHandler<Question> handler;

    public CategoryRequestTask(RestTemplate restTemplate ,PostRequestHandler<Question> handler) {
        this.restTemplate = restTemplate;
        this.handler = handler;
    }

    @Override
    protected Question doInBackground(Question... questions) {

        Question question = questions[0];

        List<Category> categories = new ArrayList<Category>();

        Log.e(this.getClass().getName(), "Make request to " + url + question.getId().toString());

        try {
            return restTemplate.getForObject(url+question.getId().toString(), Question.class);
        } catch (Exception e) {
            String error = "Fail !!!!!!" + e.getMessage();
            Log.e(this.getClass().getName(), error);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Question question) {
        handler.handle(question);
    }
}
