package com.hackathon.hackathon2014.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.model.FriendModel;

import java.util.List;

public class FriendAdapter extends BaseAdapter {

	private Activity activity; 
	private List<FriendModel> data;
	private static LayoutInflater inflater = null;

	public FriendAdapter(Activity activity, List<FriendModel> friendLists) {
		this.activity = activity;
		this.data = friendLists;
	}
    
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int position) {
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if ( inflater == null )
			inflater = (LayoutInflater) activity
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
		    convertView = inflater.inflate(R.layout.search_results_fragment_item, null);
		
		TextView secondLine = ( TextView ) convertView.findViewById(R.id.secondLine);
		FriendModel f = data.get(position);
		
		secondLine.setText(f.getName());
		
		return convertView;
	}

}
