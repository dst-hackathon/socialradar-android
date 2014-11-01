package com.hackathon.hackathon2014.webservice.task;

import android.os.AsyncTask;
import android.util.Log;

import com.hackathon.hackathon2014.model.FriendModel;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;

import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class FriendListRequestTask extends AsyncTask<String,Void,List<FriendModel>> {

    private final String url1 = "http://api.radar.codedeck.com/users/";
    private final String url2 = "/friendsuggestions";

    private RestTemplate restTemplate;

    private PostRequestHandler<List<FriendModel>> handler;

    public FriendListRequestTask(RestTemplate restTemplate, PostRequestHandler<List<FriendModel>> handler) {
        this.restTemplate = restTemplate;
        this.handler = handler;
    }

    @Override
    protected List<FriendModel> doInBackground(String... id) {
        List<FriendModel> friends = new ArrayList<FriendModel>();
        try {
            friends.addAll(Arrays.asList(restTemplate.getForObject(url1 + "2" + url2 , FriendModel[].class)));
        } catch (Exception e) {
            String error = "Fail !!!!!!" + e.getMessage();
            Log.e(this.getClass().getName(), error);
        }
        return friends;
    }

    @Override
    protected void onPostExecute(List<FriendModel> friends) {
        handler.handle(friends);
    }
}
