package com.hackathon.hackathon2014.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hackathon.hackathon2014.AnswerHolder;
import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.adapter.AnswerListAdapter;
import com.hackathon.hackathon2014.model.Answer;
import com.hackathon.hackathon2014.model.Question;

import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;


public class AnswerActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.answerContainer, new AnswerListFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.answer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class AnswerListFragment extends Fragment
    {
        public AnswerListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer, container, false);

            List<Answer> answers = getAnswers();

            for (Answer answer : answers) {
                if (AnswerHolder.contains(answer)) {
                    int index = AnswerHolder.indexOf(answer);
                    answer.setChecked(AnswerHolder.get(index).isChecked());
                }
            }

            AnswerListAdapter answerListAdapter = new AnswerListAdapter(this.getActivity(),answers);

            ListView listView = (ListView) rootView.findViewById(R.id.answerListView);
            listView.setAdapter(answerListAdapter);

            listView.setOnItemClickListener(new OpenNestedAnswerEvent(answers));

            return rootView;
        }

        private List<Answer> getAnswers() {
            List<Answer> answers;
            if(getArguments()!=null){
                Answer selectedAnswer = (Answer) getArguments().getSerializable("answer");
                answers = selectedAnswer.getAnswers();
            }else{
                Question question = (Question) getActivity().getIntent().getSerializableExtra("question");
                answers = question.getAnswers();
            }
            return answers;
        }

        private class OpenNestedAnswerEvent implements android.widget.AdapterView.OnItemClickListener {

            private List<Answer> answers;

            private OpenNestedAnswerEvent(List<Answer> answers) {
                this.answers = answers;
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Answer answer = answers.get(i);

                if( !CollectionUtils.isEmpty(answer.getAnswers()) ){
                    displayFragment(answer);
                }
            }

        }

        private void displayFragment(Answer answer) {
            final AnswerListFragment fragment = new AnswerListFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("answer",answer);

            fragment.setArguments(bundle);

            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.answerContainer, fragment, "AnswerFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
