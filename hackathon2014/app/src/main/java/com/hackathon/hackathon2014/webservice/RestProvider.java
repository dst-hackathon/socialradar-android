package com.hackathon.hackathon2014.webservice;

import com.hackathon.hackathon2014.model.Category;
import com.hackathon.hackathon2014.model.FriendModel;
import com.hackathon.hackathon2014.model.Question;
import com.hackathon.hackathon2014.model.RegisterInfo;
import com.hackathon.hackathon2014.webservice.task.CategoryRequestTask;
import com.hackathon.hackathon2014.webservice.task.FriendListRequestTask;
import com.hackathon.hackathon2014.webservice.task.QuestionRequestTask;
import com.hackathon.hackathon2014.webservice.task.SignUpRequestTask;
import com.hackathon.hackathon2014.webservice.task.SubmitAnswerRequestTask;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class RestProvider {

    private static RestTemplate restTemplate;

    private static RestTemplate getInstance() {
        if (restTemplate == null) {
            restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        }
        return restTemplate;
    }

    public static void getQuestions(PostRequestHandler<List<Question>> handler) {
        new QuestionRequestTask(getInstance(), handler).execute();
    }

    public static void getCategories(Question question, PostRequestHandler<Question> handler) {
        new CategoryRequestTask(getInstance(), handler).execute(question);
    }

    public static void requestSignUp(RegisterInfo registerInfo, PostRequestHandler<String> handler) {
        new SignUpRequestTask(getInstance(), handler).execute(registerInfo);
    }

    public static void postAnswer(Question question, PostRequestHandler<Boolean> handler) {
        new SubmitAnswerRequestTask(getInstance(), handler).execute(question);
    }

    public static void getFriendList(String id, PostRequestHandler<List<FriendModel>> handler) {
        new FriendListRequestTask(getInstance(), handler).execute(id);
    }

}
