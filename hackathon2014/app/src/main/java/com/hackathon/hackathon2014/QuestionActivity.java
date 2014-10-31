package com.hackathon.hackathon2014;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class QuestionActivity extends Activity {

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
            View rootView = inflater.inflate(R.layout.fragment_question, container, false);

            new QuestionRequestTask(getActivity(),rootView).execute();

            return rootView;
        }

        private class QuestionRequestTask extends AsyncTask<Void,Void,List<Question>> {
            private final String url = "http://172.22.1.81:8080/hackaton/questions";

            private Activity activity;
            private View view;

            public QuestionRequestTask(Activity activity, View view) {
                this.activity = activity;
                this.view = view;
            }

            @Override
            protected List<Question> doInBackground(Void... voids) {
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpHeaders httpHeaders = new HttpHeaders();
                List<MediaType> mediaTypes = new ArrayList<MediaType>();
                mediaTypes.add(MediaType.APPLICATION_JSON);
                httpHeaders.setAccept(mediaTypes);
                HttpEntity<String> httpEntity = new HttpEntity<String>(null,httpHeaders);
                restTemplate.exchange(url, HttpMethod.GET, httpEntity,Question[].class);

                List<Question> questions = Arrays.asList(restTemplate.getForObject(url, Question[].class));
                Log.e(this.getClass().getName(), questions.toString());

                return questions;
            }

            @Override
            protected void onPostExecute(List<Question> questions) {
                QuestionListAdapter questionListAdapter = new QuestionListAdapter(activity, questions);

                ListView listView = (ListView) view.findViewById(R.id.questionListView);
                listView.setAdapter(questionListAdapter);

                listView.setOnItemClickListener(new OpenAnswerEvent(activity, questions));
            }
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

                Intent intent = new Intent(activity,AnswerActivity.class);
                intent.putExtra("question",question);
                activity.startActivity(intent);
            }
        }
    }
}
