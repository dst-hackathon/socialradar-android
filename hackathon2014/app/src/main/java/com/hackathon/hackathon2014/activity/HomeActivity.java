package com.hackathon.hackathon2014.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.utility.ExitDialog;


public class HomeActivity extends Activity implements ProfileFragment.ControlInterface {

	FragmentManager fm = getFragmentManager();
    FragmentTransaction ft = fm.beginTransaction();
    ExitDialog extDialog;
	
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        if (extDialog != null && extDialog.isShow() == true ) {
            extDialog.dismiss();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            openExitDialog( "Do you wish to log out." ,"logout" );
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.logout) {
            openExitDialog( "Do you wish to log out." ,"logout" );
            return true;
        } else if ( id == R.id.exit ) {
            openExitDialog( "Do you wish to exit the program.", "exit" );
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openExitDialog( String message, String type ) {
        extDialog = new ExitDialog(this, getApplicationContext(), message, type);
        extDialog.open();
    }
}
