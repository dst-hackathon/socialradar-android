package com.hackathon.hackathon2014.activity;

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
import android.widget.Toast;

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.adapter.QuestionListAdapter;
import com.hackathon.hackathon2014.model.Option;
import com.hackathon.hackathon2014.model.Question;

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
            private final String url = "http://api.radar.codedeck.com/questions";

            private Activity activity;
            private View view;

            public QuestionRequestTask(Activity activity, View view) {
                this.activity = activity;
                this.view = view;
            }

            @Override
            protected void onPreExecute() {
                Toast.makeText(getActivity().getBaseContext(),"Loading...", Toast.LENGTH_SHORT ).show();

            }

            @Override
            protected List<Question> doInBackground(Void... voids) {
                List<Question> questions = new ArrayList<Question>();

                try {
                    RestTemplate restTemplate = new RestTemplate();
                    restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                    questions.addAll(Arrays.asList(restTemplate.getForObject(url, Question[].class)));
                } catch (Exception e) {
                    String error = "Fail !!!!!!" + e.getMessage();
                    Log.e(this.getClass().getName(), error);
                }

                //MOCK
                questions.add(
                     new Question("อาหาร" + "ที่คุณชอบ"
                         ,new Option("ญี่ปุ่น","เทปันยากิ","ซูชิ")
                         ,new Option("จีน","พระกระโดดกำแพง","หูฉลามน้ำแดง","ติ่มซำ","กระเพาะปลาผัดแห้ง","กระเพาะปลาน้ำแดง","ขาหมูหมั่นโถว")
                         ,new Option("ไทย","กระเพราไข่ดาว", "แกงป้า")
                         ,new Option("อินเดีย")
                         ,new Option("เกาหลี")
                         ,new Option("อิตาเลี่ยน" ,"พิซซ่า","สปาเกตตี้")
                     )
                );
                questions.add(new Question("สี" + "ที่คุณชอบ"));
                questions.add(new Question("สัตว์" + "ที่คุณชอบ"));
                questions.add(new Question("Mobile" + "ที่คุณชอบ"));
                questions.add(new Question("เพศ" + "ที่คุณชอบ"
                     ,new Option("ชาย")
                     ,new Option("หญิง")
                     )
                );
                questions.add(new Question("ประเภทหนัง" + "ที่คุณชอบ"
                        ,new Option("action","เลือดสาด")
                        ,new Option("ตลก")
                        ,new Option("อินดี้")
                ));
                questions.add(new Question("กีฬา" + "ที่คุณชอบ"));
                questions.add(new Question("เพลง" + "ที่คุณชอบ"));
                questions.add(new Question("เครื่องดื่ม" + "ที่คุณชอบ"));
                questions.add(new Question("Notebook" + "ที่คุณชอบ"));
                questions.add(new Question("ขนม" + "ที่คุณชอบ"));
                questions.add(new Question("ศิลปิน" + "ที่คุณชอบ"));
                questions.add(new Question("หนังสือ" + "ที่คุณชอบ"));
                questions.add(new Question("วิชาเรียน" + "ที่คุณชอบ"));
                questions.add(new Question("Brand เสื้อผ้า" + "ที่คุณชอบ"));
                questions.add(new Question("งานอดิเรก"));
                questions.add(new Question("สถานที่เที่ยว" + "ที่คุณชอบ"));

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

                if(!question.hasAnswer()){
                    return;
                }

                Intent intent = new Intent(activity,AnswerActivity.class);
                intent.putExtra("question",question);
                activity.startActivity(intent);
            }
        }
    }
}
