package com.hackathon.hackathon2014.webservice.task;

import android.os.AsyncTask;

import com.hackathon.hackathon2014.model.RegisterInfo;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;

import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

/**
 * Created by user on 01/11/2014.
 */
public class LoginRequestTask extends AsyncTask<String,Void,RegisterInfo> {

    private final String LOGIN_SERVICE_URI = "http://api.radar.codedeck.com/login";
    private RestTemplate restTemplate;
    private PostRequestHandler<RegisterInfo> handler;

    public LoginRequestTask(RestTemplate restTemplate, PostRequestHandler<RegisterInfo> handler) {
        this.restTemplate = restTemplate;
        this.handler = handler;
    }

    @Override
    protected RegisterInfo doInBackground(String... loginParams) {

        String email = loginParams[0];
        String password = loginParams[1];
        RegisterInfo user = new RegisterInfo();
        user.setEmail(email);
        user.setPassword(password);
        HashMap<String, String> loginParamsMap = new HashMap<String, String>();
        loginParamsMap.put("email", email);
        loginParamsMap.put("password", password);

        RegisterInfo res = new RegisterInfo();
        try {
            res = restTemplate.postForObject(LOGIN_SERVICE_URI, user, RegisterInfo.class, loginParamsMap);
        }
        catch (Exception e )
        {
            e.printStackTrace();
            res.setError(e.getMessage());
        }

        return res;
    }

    @Override
    protected void onPostExecute(RegisterInfo user) {

        handler.handle(user);
    }

}
