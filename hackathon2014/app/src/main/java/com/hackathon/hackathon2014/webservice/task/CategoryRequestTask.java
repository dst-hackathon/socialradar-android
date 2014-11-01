package com.hackathon.hackathon2014.webservice.task;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.hackathon.hackathon2014.model.Category;
import com.hackathon.hackathon2014.model.Question;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class CategoryRequestTask extends AsyncTask<Question,Void,Question> {

    private final String url = "http://api.radar.codedeck.com/questions/";
    private final String answerUrl = "http://api.radar.codedeck.com/users/2/answer";
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
            Question responseQuestion = restTemplate.getForObject(url + question.getId().toString(), Question.class);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(new ArrayList<MediaType>(Arrays.asList(MediaType.APPLICATION_JSON)));

            HttpEntity httpEntity = new HttpEntity(headers);
            ResponseEntity<Map> responseEntity = restTemplate.exchange(answerUrl, HttpMethod.GET,httpEntity,Map.class);

            Map<String,Map<String,List<Integer>>> map = (Map<String,Map<String,List<Integer>>>)responseEntity.getBody();

            if( map.containsKey(responseQuestion.getId().toString())){

                Map<String,List<Integer>> checkedCategory = map.get(responseQuestion.getId().toString());

                for (String categoryId : checkedCategory.keySet()) {
                    List<Integer> selectedOptionIds = checkedCategory.get(categoryId);

                    if( selectedOptionIds.size() > 0 ){
                        Category category = responseQuestion.getCategory(Long.valueOf(categoryId));
                        category.checkOptions(selectedOptionIds);
                        category.setOptionChecked(true);
                    }
                }
            }

            return responseQuestion;
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
