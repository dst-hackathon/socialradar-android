package com.hackathon.hackathon2014.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.model.Answer;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class AnswerListAdapter extends BaseAdapter {

    private List<Answer> answers;
    private Activity activity;

    public AnswerListAdapter(Activity activity, List<Answer> answers) {
        this.answers = answers;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return CollectionUtils.isEmpty(answers) ? 0 : answers.size();
    }

    @Override
    public Answer getItem(int i) {
        return answers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Answer item = getItem(i);

        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.answer_row, null);
        }

        TextView answerText = (TextView) view.findViewById(R.id.answerText);
        answerText.setText(item.getText());

        return view;
    }
}
