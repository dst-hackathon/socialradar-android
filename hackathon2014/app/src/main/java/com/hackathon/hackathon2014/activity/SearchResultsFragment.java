package com.hackathon.hackathon2014.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
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
import com.hackathon.hackathon2014.webservice.PostRequestHandler;
import com.hackathon.hackathon2014.webservice.RestProvider;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment {
	View view;
    Activity activity;
    private final String url1 = "http://api.radar.codedeck.com/users/";
    private final  String url2 = "/friendsuggestions";
    List<FriendModel> friendLists;
    ProgressDialog dialog;

    @Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
    	view = inflater.inflate(R.layout.search_results_fragment, container, false);
        activity = this.getActivity();
        //getList();
		return view ;
	}
    
    public void getList() {
    	friendLists = new ArrayList<FriendModel>();
        dialog = ProgressDialog.show( activity, "",
                "Loading. Please wait...", true);
        //ListView lv = ( ListView ) view.findViewById(R.id.search_result_list);
        RestProvider.getFriendList("", new PostRequestHandler<List<FriendModel>>() {
            @Override
            public void handle(List<FriendModel> friendModels) {
                upDateFriendList(friendModels);
            }
        });
    }

    public void upDateFriendList( List<FriendModel> fl) {
        friendLists = fl;
        ListView lv = ( ListView ) view.findViewById(R.id.search_result_list);
        FriendAdapter adapter = new FriendAdapter(activity, friendLists);
        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getActivity(), "myPos "+ position , Toast.LENGTH_SHORT).show();
            }

        });

        lv.setAdapter(adapter);
        dialog.cancel();
    }


}
