package com.hackathon.hackathon2014.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
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
import com.hackathon.hackathon2014.webservice.PostRequestHandler;
import com.hackathon.hackathon2014.webservice.RestProvider;

import org.springframework.util.CollectionUtils;

import java.util.List;


public class CategoryActivity extends Activity {

    public static String BUNDLE_CATEGORY = "category";
    public static String BUNDLE_QUESTION = "question";

    public static String FRAGMENT_OPTION = "optionFragment";
    public static String FRAGMENT_CATEGORY = "categoryFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);

        Question question = (Question)getIntent().getSerializableExtra(QuestionActivity.EXTRA_QUESTION);

        setTitle(question.getText());

        Bundle bundle = new Bundle();
        bundle.putSerializable(BUNDLE_QUESTION,question);

        if (savedInstanceState == null) {
            CategoryListFragment categoryListFragment = new CategoryListFragment();
            categoryListFragment.setArguments(bundle);
            getFragmentManager().beginTransaction()
                    .add(R.id.answerContainer, categoryListFragment,FRAGMENT_CATEGORY)
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
        private Question question;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_answer, container, false);

            if( question == null ){
                question = (Question)getArguments().getSerializable(BUNDLE_QUESTION);
            }

            if( question.getCategories() == null ){
                RestProvider.getCategories(question,new PostRequestHandler<Question>() {
                    @Override
                    public void handle(Question question) {
                        renderListView(rootView,question);
                        setQuestion(question);
                    }
                });
            }else{
                renderListView(rootView,question);
            }

            return rootView;
        }

        private void renderListView(View view,Question question){
            CategoryListAdapter categoryListAdapter = new CategoryListAdapter(getActivity(), question.getCategories());

            ListView listView = (ListView) view.findViewById(R.id.answerListView);
            listView.setAdapter(categoryListAdapter);

            listView.setOnItemClickListener(new OpenNestedAnswerEvent(question.getCategories()));
        }

        public Question getQuestion() {
            return question;
        }

        private void setQuestion(Question question){
            this.question = question;
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

            final OptionListFragment fragment = new OptionListFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable(BUNDLE_CATEGORY, category);

            fragment.setArguments(bundle);

            final FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.answerContainer, fragment, FRAGMENT_OPTION);
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
                return (Category) getArguments().getSerializable(BUNDLE_CATEGORY);
            }
            return null;
        }
    }

    @Override
    public void onBackPressed() {

        if(isOptionFragment()){
            OptionListFragment fragment = (OptionListFragment)getFragmentManager().findFragmentByTag(FRAGMENT_OPTION);

            Category category = fragment.getCategory();
            category.setOptionChecked(atLeastOneOptionChecked(category.getOptions()));
        }else if(isCategoryFragment()){
            CategoryListFragment fragment = (CategoryListFragment)getFragmentManager().findFragmentByTag(FRAGMENT_CATEGORY);
            RestProvider.postAnswer(fragment.getQuestion(),new PostRequestHandler<Boolean>() {
                @Override
                public void handle(Boolean isSuccess) {
                    Log.e(this.getClass().getName(), "Result of Submit answer " + isSuccess);
                }
            });
        }

        super.onBackPressed();
    }

    private boolean isOptionFragment(){
        return getFragmentManager().findFragmentByTag(FRAGMENT_OPTION) != null;
    }
    private boolean isCategoryFragment(){
        return getFragmentManager().findFragmentByTag(FRAGMENT_CATEGORY) != null;
    }

    private boolean atLeastOneOptionChecked(List<Option> options)
    {

        for(Option option : options)
        {
            if( option.isChecked() ){
                return true;
            }
        }

        return false;
    }
}
