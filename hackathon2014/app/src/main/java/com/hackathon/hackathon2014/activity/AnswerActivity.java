package com.hackathon.hackathon2014.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.adapter.AnswerListAdapter;
import com.hackathon.hackathon2014.model.Question;


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
        private Question question;

        public AnswerListFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer, container, false);

            question = (Question)getActivity().getIntent().getSerializableExtra("question");

            AnswerListAdapter answerListAdapter = new AnswerListAdapter(this.getActivity(),question.getAnswers());

            ListView listView = (ListView) rootView.findViewById(R.id.answerListView);
            listView.setAdapter(answerListAdapter);

            return rootView;
        }
    }
}
