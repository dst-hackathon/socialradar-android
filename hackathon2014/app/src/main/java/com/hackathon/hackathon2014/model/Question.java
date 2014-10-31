package com.hackathon.hackathon2014.model;

import com.hackathon.hackathon2014.model.Answer;

import java.io.Serializable;
import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class Question implements Serializable{
    private String text;

    private List<Answer> answers;

    public Question() {
    }

    public Question(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
}
