package com.hackathon.hackathon2014.utility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hackathon.hackathon2014.LoginUser;
import com.hackathon.hackathon2014.R;

public class ExitDialog extends AlertDialog.Builder  {

	private Activity activity;
	private boolean isShow = false;
	private AlertDialog obj;
    private String message = "";
    private String type;
	
	public ExitDialog(final Activity activity, Context context, String message, String type) {
		super(activity);
		this.activity = activity;
        this.message = message;
        this.type = type;
		
		this.setPositiveButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				isShow = false;
				dialog.dismiss();
			}
		});

		this.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				action();
			}
		});

		this.setOnCancelListener(new DialogInterface.OnCancelListener() {	
			@Override
			public void onCancel(DialogInterface dialog) {
				isShow = false;
			}
		});
	}
	
	public boolean isShow() {
		return this.isShow;
	}
	
	private void finish(){
		this.activity.finish();
	}
	
	public void open() {
		obj = this.create();
		LayoutInflater layout = activity.getLayoutInflater();
		View view = layout.inflate(R.layout.exit_dialog, null);
        ((TextView) view.findViewById(R.id.dialog_massage)).setText(message);
		obj.setView(view, 0, 0, 0, 0);
		obj.show();
		this.isShow = true;
	}

	public void dismiss() {
		obj.dismiss();
	}

    private void action() {
        if( type.equals("exit") ) {
            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(startMain);
            finish();
        }else if( type.equals("logout") ) {
            LoginUser.logout();
            finish();
        }
    }

}
