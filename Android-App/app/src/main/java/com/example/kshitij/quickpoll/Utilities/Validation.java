package com.example.kshitij.quickpoll.Utilities;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {

    public static boolean isEmailValid(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password){
        return password.length() >= 5;
    }

    public static boolean isNullorEmpty(String str){
        if (str == null) {
            return true;
        }
        if(str.trim().isEmpty()){
            return true;
        }
        return false;
    }


}
