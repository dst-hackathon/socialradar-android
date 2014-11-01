package com.hackathon.hackathon2014.webservice.task;

import android.os.AsyncTask;
import android.util.Log;

import com.hackathon.hackathon2014.model.BaseResponse;
import com.hackathon.hackathon2014.model.Category;
import com.hackathon.hackathon2014.model.Option;
import com.hackathon.hackathon2014.model.Question;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class SubmitAnswerRequestTask extends AsyncTask<Question,Void,Boolean> {

    private final String url = "http://api.radar.codedeck.com/users/2/answer";
    private RestTemplate restTemplate;
    private PostRequestHandler<Boolean> handler;

    public SubmitAnswerRequestTask(RestTemplate restTemplate, PostRequestHandler<Boolean> handler) {
        this.restTemplate = restTemplate;
        this.handler = handler;
    }

    @Override
    protected Boolean doInBackground(Question... questions) {

        Question question = questions[0];

        Map<String,Map<String,Long[]>> requestObject = new HashMap<String, Map<String, Long[]>>();
        Map<String,Long[]> map = new HashMap<String, Long[]>();

        requestObject.put(question.getId().toString(),map);

        for (Category category : question.getCategories()) {
            if( category.isOptionChecked() ){

                List<Long> optionsId = new ArrayList<Long>();

                for (Option option : category.getOptions()) {
                    if( option.isChecked() )
                    {
                        optionsId.add(option.getId());
                    }
                }

                Long[] longs = new Long[optionsId.size()];
                map.put( category.getId().toString(), optionsId.toArray(longs));
            }
        }
        
        Log.e(this.getClass().getName(), "Make request to " + url);

        try {
            BaseResponse response = restTemplate.postForObject(url,requestObject, BaseResponse.class);
            Log.e(this.getClass().getName(), "Response " + response);
            return true;
        } catch (Exception e) {
            String error = "Fail !!!!!!" + e.getMessage();
            Log.e(this.getClass().getName(), error);
        }

        return false;
    }

    @Override
    protected void onPostExecute(Boolean isSuccess) {
        handler.handle(isSuccess);
    }
}
