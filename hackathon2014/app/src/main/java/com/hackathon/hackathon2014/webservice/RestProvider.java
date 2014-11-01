package com.hackathon.hackathon2014.webservice;

import com.hackathon.hackathon2014.model.Question;
import com.hackathon.hackathon2014.utility.PostRequestHandler;
import com.hackathon.hackathon2014.webservice.task.QuestionRequestTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class RestProvider {

    private static RestTemplate restTemplate;

    private static RestTemplate getInstance(){
        if( restTemplate == null ){
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }
        return restTemplate;
    }

    public static void getQuestions(PostRequestHandler<List<Question>> handler)
    {
        new QuestionRequestTask(getInstance(),handler).execute();
    }

}
