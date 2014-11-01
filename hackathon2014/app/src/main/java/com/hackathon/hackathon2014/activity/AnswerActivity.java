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
import com.hackathon.hackathon2014.adapter.OptionListAdapter;
import com.hackathon.hackathon2014.model.Option;
import com.hackathon.hackathon2014.model.Question;

import org.springframework.util.CollectionUtils;

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

            List<Option> options = getAnswers();

            for (Option option : options) {
                if (AnswerHolder.contains(option)) {
                    int index = AnswerHolder.indexOf(option);
                    option.setChecked(AnswerHolder.get(index).isChecked());
                }
            }

            OptionListAdapter optionListAdapter = new OptionListAdapter(this.getActivity(), options);

            ListView listView = (ListView) rootView.findViewById(R.id.answerListView);
            listView.setAdapter(optionListAdapter);

            listView.setOnItemClickListener(new OpenNestedAnswerEvent(options));

            return rootView;
        }

        private List<Option> getAnswers() {
            List<Option> options;
            if(getArguments()!=null){
                Option selectedOption = (Option) getArguments().getSerializable("answer");
                options = selectedOption.getOptions();
            }else{
                Question question = (Question) getActivity().getIntent().getSerializableExtra("question");
                options = question.getOptions();
            }
            return options;
        }

        private class OpenNestedAnswerEvent implements android.widget.AdapterView.OnItemClickListener {

            private List<Option> options;

            private OpenNestedAnswerEvent(List<Option> options) {
                this.options = options;
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Option option = options.get(i);

                if( !CollectionUtils.isEmpty(option.getOptions()) ){
                    displayFragment(option);
                }
            }

        }

        private void displayFragment(Option option) {
            final AnswerListFragment fragment = new AnswerListFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("answer", option);

            fragment.setArguments(bundle);

            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.answerContainer, fragment, "AnswerFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
