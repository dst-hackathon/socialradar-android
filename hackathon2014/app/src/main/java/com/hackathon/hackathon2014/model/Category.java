package com.hackathon.hackathon2014.model;

import android.util.Log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krai on 11/1/14 AD.
 */
public class Category implements Serializable {


    private Long id;

    private Long order;

    private String text;

    private boolean optionChecked;

    List<Option> options;

    public Category() {
    }

    public Category(String text, String... options) {
        this.text = text;
        this.options = new ArrayList<Option>();
        for (String option : options) {
            this.options.add(new Option(option));
        }
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

    public List<Option> getOptions() {
        return options;
    }

    public void setOptions(List<Option> options) {
        this.options = options;
    }

    public boolean isOptionChecked() {
        return optionChecked;
    }

    public void setOptionChecked(boolean optionChecked) {
        this.optionChecked = optionChecked;
    }

    public void checkOptions(List<Integer> optionIds){
        for (Integer optionId : optionIds) {
            checkOption(optionId);
        }
    }

    private void checkOption(Integer id){
        for (Option option : options) {
            if( option.getId().longValue() == id.intValue() ){
                option.setChecked(true);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        if (!id.equals(category.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
