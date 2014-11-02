package com.hackathon.hackathon2014;

import android.widget.ListView;

import com.hackathon.hackathon2014.adapter.QuestionListAdapter;
import com.hackathon.hackathon2014.model.Question;
import com.hackathon.hackathon2014.model.RegisterInfo;
import com.hackathon.hackathon2014.webservice.PostRequestHandler;
import com.hackathon.hackathon2014.webservice.RestProvider;

import java.util.List;

/**
 * Created by keerati on 11/1/14 AD.
 */
public class LoginUser {

    private static RegisterInfo loginUser;

    public static RegisterInfo getLoginUser() {
        return loginUser;
    }

    public static void setLoginUser(RegisterInfo loginUser) {
        LoginUser.loginUser = loginUser;
    }

    public static boolean isLogin(){
        return loginUser!=null;
    }

    public static void logout(){
        loginUser = null;
    }

}
