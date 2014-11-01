package com.hackathon.hackathon2014.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class Option implements Serializable{

    static long i = 0;

    private Long id;
    private String text;
    private List<Option> options = new ArrayList<Option>();
    private boolean checked;

    public Option(String text, String... answers) {
        id = i++;
        this.text = text;

        for (String answer : answers) {
            this.options.add(new Option(answer));
        }
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Option option = (Option) o;

        if (!id.equals(option.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
