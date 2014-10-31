package com.hackathon.hackathon2014.model;

import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class Question implements Serializable{

    private Long id;

    private Long order;

    private String text;

    private List<Answer> answers;

    public Question() {
    }

    public Question(String text) {
        this.text = text;
    }

    public Question(String text, Answer... answers) {
        this.text = text;
        this.answers = Arrays.asList(answers);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
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

    public boolean hasAnswer() {
        return !CollectionUtils.isEmpty(answers);
    }
}