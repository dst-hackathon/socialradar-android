package com.hackathon.hackathon2014.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;
import com.hackathon.hackathon2014.webservice.RestProvider;
import com.hackathon.hackathon2014.adapter.QuestionListAdapter;
import com.hackathon.hackathon2014.model.Question;

import java.util.List;


public class QuestionActivity extends Activity {

    public static String EXTRA_QUESTION = "question";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_question, container, false);

            Toast.makeText(rootView.getContext(),"Loading...",Toast.LENGTH_LONG).show();
            RestProvider.getQuestions(new PostRequestHandler<List<Question>>() {
                @Override
                public void handle(List<Question> questions) {
                    QuestionListAdapter questionListAdapter = new QuestionListAdapter(getActivity(), questions);

                    ListView listView = (ListView) rootView.findViewById(R.id.questionListView);
                    listView.setAdapter(questionListAdapter);

                    listView.setOnItemClickListener(new OpenAnswerEvent(getActivity(), questions));
                }
            });
            return rootView;
        }

        private class OpenAnswerEvent implements android.widget.AdapterView.OnItemClickListener {

            private List<Question> questions;
            private Activity activity;

            private OpenAnswerEvent(Activity activity, List<Question> questions) {
                this.activity = activity;
                this.questions = questions;
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Question question = questions.get(i);
                question.setSelected(true);

                Intent intent = new Intent(activity,CategoryActivity.class);
                intent.putExtra(EXTRA_QUESTION, question);
                activity.startActivity(intent);

                final ImageView questionIcon = (ImageView) view.findViewById(R.id.questionIcon);
                QuestionListAdapter.renderChecked(question.isSelected(), questionIcon, R.drawable.ok_enable, R.drawable.ok_disable);
            }
        }
    }
}
