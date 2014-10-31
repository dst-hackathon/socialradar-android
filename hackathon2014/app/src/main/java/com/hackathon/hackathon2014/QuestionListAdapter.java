package com.hackathon.hackathon2014;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class QuestionListAdapter extends BaseAdapter {

    private List<Question> questions;
    private Activity activity;

    public QuestionListAdapter(Activity activity, List<Question> questions) {
        this.questions = questions;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public Question getItem(int i) {
        return questions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Question item = getItem(i);

        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.question_row, null);
        }

        TextView questionText = (TextView) view.findViewById(R.id.questionText);
        questionText.setText(item.getText());

        return view;
    }
}
