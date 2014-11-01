package com.hackathon.hackathon2014;

import com.hackathon.hackathon2014.model.RegisterInfo;

/**
 * Created by keerati on 11/1/14 AD.
 */
public class LoginUser {

    private static RegisterInfo loginUser;

    public RegisterInfo getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(RegisterInfo loginUser) {
        LoginUser.loginUser = loginUser;
    }

    public static boolean isLogin(){
        return loginUser!=null;
    }

    public void logout(){
        loginUser = null;
    }

    public void login(String username,String password){
        loginUser = new RegisterInfo();
        loginUser.setUsername(username);
        loginUser.setPassword(password);

        //TODO
    }
}
