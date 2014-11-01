package com.hackathon.hackathon2014.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.adapter.FriendAdapter;
import com.hackathon.hackathon2014.model.FriendModel;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment {
	View view;
    @Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
    	view = inflater.inflate(R.layout.search_results_fragment, container, false);
        getList();
		return view ;
	}
    
    public void getList() {
    	ListView lv = ( ListView ) view.findViewById(R.id.search_result_list);
    	List<FriendModel> friendLists = new ArrayList<FriendModel>();
    	friendLists.add(new FriendModel("Jeed", "1"));
    	friendLists.add(new FriendModel("Ja", "2"));
    	friendLists.add(new FriendModel("Jaja", "3"));
    	
    	FriendAdapter adapter = new FriendAdapter(this.getActivity(), friendLists);
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
//                android.R.layout.simple_list_item_1, AndroidOS);

		lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				Toast.makeText(getActivity(), "myPos "+ position , Toast.LENGTH_SHORT).show();
			}
			
		});
		
		lv.setAdapter(adapter);
		
		
    }
    
    
}
