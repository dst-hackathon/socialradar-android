package com.hackathon.hackathon2014.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends ActionBarActivity {

	private Button loginButton;
	private EditText userObj;
	private EditText passwordObj;
	final Context context = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boolean isLoggedIn = false;

		// Check if already login
		if (isLoggedIn) {
			goToHomePage();
		} else {
			// If not logged in yet, login page displays.
			setContentView(R.layout.activity_login);

			addListenerOnLoginButton();
			// addListenerOnFacebookLoginButton();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	public void addListenerOnLoginButton() {

		loginButton = (Button) findViewById(R.id.loginBtn);

		userObj = (EditText) findViewById(R.id.usernameTxt);
		passwordObj = (EditText) findViewById(R.id.passwordTxt);

		loginButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				String user = userObj.getText().toString();
				String password = passwordObj.getText().toString();

				if ( validateLogin(user, password) )
				{
					goToHomePage();
				}

			}

		});

	}

	public void onClickSignup(View v) {
		Intent intent = new Intent(this, SignUpActivity.class);
		startActivity(intent);

	}

	public void goToHomePage() {
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
	}

	public boolean validateLogin(String user, String password) {
		boolean loginFailed = false;
		String errorMessage = "";
		if ((user == null || "".equals(user))
				|| (password == null || "".equals(password))) {
			errorMessage = "Please enter User and Password.";
			loginFailed = true;
		}

		if ( loginFailed ) {
			showErrorMessage("Login Failed", errorMessage);
		}
		return !loginFailed;
	}

	private void showErrorMessage(String title, String errorMessage) {
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
