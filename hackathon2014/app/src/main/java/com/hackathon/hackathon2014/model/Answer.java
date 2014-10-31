package com.hackathon.hackathon2014.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class Answer implements Serializable{

    private String text;
    private List<Answer> answers = new ArrayList<Answer>();

    public Answer(String text, String... answers) {
        this.text = text;

        for (String answer : answers) {
            this.answers.add(new Answer(answer));
        }
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
