package com.hackathon.hackathon2014.activity;


import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import com.hackathon.hackathon2014.R;

public class SearchFriendsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			finish();
			return;
	    }
		setContentView(R.layout.search_results_activity);
	}

	@Override
	protected void onStart() {
		super.onStart();
		SearchResultsFragment frag = (SearchResultsFragment) getFragmentManager().findFragmentById(R.id.search_result);
        frag.getList();
	}
}
