package com.hackathon.hackathon2014.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathon.hackathon2014.AnswerHolder;
import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.model.Category;
import com.hackathon.hackathon2014.model.Option;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class CategoryListAdapter extends BaseAdapter {

    private List<Category> categories;
    private Activity activity;

    public CategoryListAdapter(Activity activity, List<Category> categories) {
        this.categories = categories;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return CollectionUtils.isEmpty(categories) ? 0 : categories.size();
    }

    @Override
    public Category getItem(int i) {
        return categories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Category category = getItem(i);

        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.answer_row, null);
        }

        TextView answerText = (TextView) view.findViewById(R.id.answerText);
        answerText.setText(category.getText());

        ImageView answerCheckIcon = (ImageView)view.findViewById(R.id.answerCheckButton);

        if(category.isOptionChecked()){
            answerCheckIcon.setImageResource(R.drawable.like);
        }else{
            answerCheckIcon.setImageResource(R.drawable.unlike);
        }

        return view;
    }
}
