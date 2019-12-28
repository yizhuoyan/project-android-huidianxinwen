package com.jason.hdxw.utils;

public abstract class Strings {
    private Strings(){}

    public static String trim(CharSequence s,String defaultValue){

        if(s==null||(s=s.toString().trim()).length()==0){
            return defaultValue;
        }
        return s.toString();
    }
    public static String trim(CharSequence s){
        return trim(s,null);
    }
}
