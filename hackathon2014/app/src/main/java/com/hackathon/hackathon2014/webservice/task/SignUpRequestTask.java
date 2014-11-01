package com.hackathon.hackathon2014.webservice.task;

import android.os.AsyncTask;

import com.hackathon.hackathon2014.model.RegisterInfo;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by smileyOpal on 11/1/14.
 */
public class SignUpRequestTask extends AsyncTask<RegisterInfo, Void, String> {

    private final String url = "http://api.radar.codedeck.com/signup";
    private RestTemplate restTemplate;
    private PostRequestHandler<String> handler;

    public SignUpRequestTask(RestTemplate restTemplate, PostRequestHandler<String> handler) {
        this.restTemplate = restTemplate;
        this.handler = handler;
    }

    @Override
    protected String doInBackground(RegisterInfo... registerInfos) {
        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
        try {
            entity.addPart("email", new StringBody(registerInfos[0].getEmail()));
            entity.addPart("password", new StringBody(registerInfos[0].getPassword()));
            entity.addPart("file", new FileBody(registerInfos[0].getFile()));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        post.addHeader( "Content-Type", "multipart/form-data; ");
        post.setEntity(entity);
        try {
            HttpResponse response = client.execute(post);
            System.out.println(response.getStatusLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }

    @Override
    protected void onPostExecute(String result){
        handler.handle(result);
    }
}
