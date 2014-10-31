package com.hackathon.hackathon2014;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
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


            List<Question> list = getQuestions();

            QuestionListAdapter questionListAdapter = new QuestionListAdapter(this.getActivity(), list);

//                    new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1, list);


            ListView listView = (ListView) rootView.findViewById(R.id.questionListView);
            listView.setAdapter(questionListAdapter);

            return rootView;
        }
    }

    private static List<Question> getQuestions() {
        List<Question> list = new ArrayList<Question>();

        list.add(new Question("อาหาร" + "ที่คุณชอบ"));
        list.add(new Question("สี" + "ที่คุณชอบ"));
        list.add(new Question("สัตว์" + "ที่คุณชอบ"));
        list.add(new Question("Mobile" + "ที่คุณชอบ"));
        list.add(new Question("เพศ" + "ที่คุณชอบ"));
        list.add(new Question("หนัง" + "ที่คุณชอบ"));
        list.add(new Question("กีฬา" + "ที่คุณชอบ"));
        list.add(new Question("เพลง" + "ที่คุณชอบ"));
        list.add(new Question("เครื่องดื่ม" + "ที่คุณชอบ"));
        list.add(new Question("Notebook" + "ที่คุณชอบ"));
        list.add(new Question("ขนม" + "ที่คุณชอบ"));
        list.add(new Question("ศิลปิน" + "ที่คุณชอบ"));
        list.add(new Question("หนังสือ" + "ที่คุณชอบ"));
        list.add(new Question("วิชาเรียน" + "ที่คุณชอบ"));
        list.add(new Question("Brand เสื้อผ้า" + "ที่คุณชอบ"));
        list.add(new Question("งานอดิเรก"));
        list.add(new Question("สถานที่เที่ยว" + "ที่คุณชอบ"));

        return list;
    }
}
