package com.hackathon.hackathon2014.utility;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class AlertMessageDialogue {

	public AlertMessageDialogue(Context context, String title, String errorMessage) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder
				.setMessage(errorMessage)
				.setCancelable(false)
				.setNegativeButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

}
