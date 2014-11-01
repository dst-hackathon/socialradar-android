package com.hackathon.hackathon2014.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.model.Option;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by keerati on 10/31/14 AD.
 */
public class OptionListAdapter extends BaseAdapter {

    private List<Option> options;
    private Activity activity;

    public OptionListAdapter(Activity activity, List<Option> options) {
        this.options = options;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return CollectionUtils.isEmpty(options) ? 0 : options.size();
    }

    @Override
    public Option getItem(int i) {
        return options.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final Option option = getItem(i);

        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.answer_row, null);
        }

        TextView answerText = (TextView) view.findViewById(R.id.answerText);
        answerText.setText(option.getText());

        final ImageView answerImage = (ImageView) view.findViewById(R.id.answerCheckButton);

        renderLike(option, answerImage);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                option.toggleChecked();
                renderLike(option, answerImage);
            }
        });

        ImageView imageView = (ImageView) view.findViewById(R.id.nextButton);
        imageView.setVisibility(View.INVISIBLE);

        return view;
    }

    private void renderLike(Option option, ImageView answerImage) {
        if(option.isChecked()){
            answerImage.setImageResource(R.drawable.like);
        }else{
            answerImage.setImageResource(R.drawable.unlike);
        }
    }
}
