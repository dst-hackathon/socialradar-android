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

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.adapter.CategoryListAdapter;
import com.hackathon.hackathon2014.adapter.OptionListAdapter;
import com.hackathon.hackathon2014.model.Category;
import com.hackathon.hackathon2014.model.Option;
import com.hackathon.hackathon2014.model.Question;

import org.springframework.util.CollectionUtils;

import java.util.List;


public class CategoryActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.answerContainer, new CategoryListFragment())
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

    public static class CategoryListFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer, container, false);

            Question question = getQuestion();

            CategoryListAdapter categoryListAdapter = new CategoryListAdapter(this.getActivity(), question.getCategories());

            ListView listView = (ListView) rootView.findViewById(R.id.answerListView);
            listView.setAdapter(categoryListAdapter);

            listView.setOnItemClickListener(new OpenNestedAnswerEvent(question.getCategories()));

            return rootView;
        }

        private Question getQuestion() {
            return (Question) getActivity().getIntent().getSerializableExtra("question");
        }

        private class OpenNestedAnswerEvent implements android.widget.AdapterView.OnItemClickListener {

            private List<Category> categories;

            private OpenNestedAnswerEvent(List<Category> categories) {
                this.categories = categories;
            }

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Category category = categories.get(i);

                if( !CollectionUtils.isEmpty(category.getOptions()) ){
                    displayOption(category);
                }
            }

        }

        private void displayOption(Category category) {

//            getFragmentManager().beginTransaction()
//                    .add(R.id.answerContainer, new CategoryListFragment())
//                    .commit();

            final OptionListFragment fragment = new OptionListFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("category", category);

            fragment.setArguments(bundle);

            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.answerContainer, fragment, "AnswerFragment");
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    public static class OptionListFragment extends Fragment
    {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_answer, container, false);

            Category category = getCategory();

            OptionListAdapter optionListAdapter = new OptionListAdapter(this.getActivity(), category.getOptions());

            ListView listView = (ListView) rootView.findViewById(R.id.answerListView);
            listView.setAdapter(optionListAdapter);

            return rootView;
        }

        private Category getCategory(){
            if( getArguments() != null ){
                return (Category) getArguments().getSerializable("category");
            }
            return null;
        }
    }

}
