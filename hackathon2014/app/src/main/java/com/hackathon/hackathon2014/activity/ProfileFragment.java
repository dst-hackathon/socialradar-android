package com.hackathon.hackathon2014.activity;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.hackathon.hackathon2014.R;

public class ProfileFragment extends Fragment implements OnClickListener {
	
	ControlInterface interface1;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.profile_fragment, container, false);
		Button searchButton = ( Button ) view.findViewById(R.id.search);
		Button editProfile = ( Button ) view.findViewById(R.id.edit_profile);
		Button favorite = ( Button ) view.findViewById(R.id.edit_favorite);
		
		searchButton.setOnClickListener(this);
		editProfile.setOnClickListener(this);
		favorite.setOnClickListener(this);
		
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    try {
	    	interface1 = (ControlInterface) activity;
	    } catch (ClassCastException e) {
	        throw new ClassCastException(activity.toString() + " must implement onViewSelected");
	    }
	}
	
	public interface ControlInterface {
	    public void buttonClicked(View v);
	}

	@Override
	public void onClick(View v) {
		interface1.buttonClicked(v);
	}

}
