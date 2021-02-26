package com.app.tryitat.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    SharedPreferences sharedPref;
    public SharedPref(Context context){
        sharedPref = context.getSharedPreferences("tryitatPref", Context.MODE_PRIVATE);
    }

    public void setUserType(String userType){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("user_type", userType);
        editor.apply();
    }

    public String getUserType(){
        return sharedPref.getString("user_type", "");
    }

    public void setUserEmail(String userEmail){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("at_email", userEmail);
        editor.apply();
    }

    public String getUserEmail(){
        return sharedPref.getString("at_email", "");
    }

    public void setUserPassword(String password){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("at_password", password);
        editor.apply();
    }

    public String getUserPassword(){
        return sharedPref.getString("at_password", "");
    }

}
