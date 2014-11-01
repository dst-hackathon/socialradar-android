package com.hackathon.hackathon2014.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;

import com.hackathon.hackathon2014.R;


public class HomeActivity extends Activity implements ProfileFragment.ControlInterface {

	FragmentManager fm = getFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.home_activity);
    }
    
    @Override
    protected void onStart() {
    	super.onStart();
    	if( getResources().getConfiguration().ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation  ) {
			SearchResultsFragment frag = (SearchResultsFragment) getFragmentManager().findFragmentById(R.id.search_result);
			frag.getList();
		}
    }

	@Override
	public void buttonClicked(View v) {
		switch (v.getId()) {
		case R.id.search:
			searchFriends();
			break;
		case R.id.edit_profile:
            startActivity(new Intent(this, SignUpActivity.class));
			break;
		case R.id.edit_favorite:
            startActivity(new Intent(this, QuestionActivity.class));
			break;
		}
	}
	
	public void searchFriends() {
		
		if( getResources().getConfiguration().ORIENTATION_LANDSCAPE == getResources().getConfiguration().orientation  ) {
			SearchResultsFragment frag = (SearchResultsFragment) getFragmentManager().findFragmentById(R.id.search_result);
			frag.getList();
		} else {
	      Intent intent = new Intent(this, SearchFriendsActivity.class);
          startActivity(intent);
		}

	}
/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
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
    }*/
}
