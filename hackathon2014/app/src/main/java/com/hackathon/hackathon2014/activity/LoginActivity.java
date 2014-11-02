package com.hackathon.hackathon2014.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.hackathon.hackathon2014.LoginUser;
import com.hackathon.hackathon2014.R;
import com.hackathon.hackathon2014.model.RegisterInfo;
import com.hackathon.hackathon2014.utility.AlertMessageDialogue;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;
import com.hackathon.hackathon2014.webservice.RestProvider;

public class LoginActivity extends Activity {

    private Button loginButton;
    private EditText emailObj;
    private EditText passwordObj;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isLoggedIn = LoginUser.isLogin();

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
        getMenuInflater().inflate(R.menu.login, menu);
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

        emailObj = (EditText) findViewById(R.id.emailTxt);
        passwordObj = (EditText) findViewById(R.id.passwordTxt);

        loginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                final String email = emailObj.getText().toString();
                String password = passwordObj.getText().toString();

                if (validateLogin(email, password)) {

                    RestProvider.loginUser(email, password, new PostRequestHandler<RegisterInfo>() {
                        @Override
                        public void handle(RegisterInfo registerInfo) {
                            if ((registerInfo != null) &&
                                    (registerInfo.getSuccess() != null) && ("success".equalsIgnoreCase(registerInfo.getSuccess()))) {
                                registerInfo.setEmail(email);
                                LoginUser.setLoginUser(registerInfo);
                                goToHomePage();
                            } else {
                                new AlertMessageDialogue(context, "Login Failed", registerInfo.getError());
                            }
                        }
                    });
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

        if (loginFailed) {
            new AlertMessageDialogue(context, "Login Failed", errorMessage);
        }
        return !loginFailed;
    }

}
