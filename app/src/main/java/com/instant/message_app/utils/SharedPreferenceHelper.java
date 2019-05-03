package com.instant.message_app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceHelper {

    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private static final String FILE_NAME="sharedPreferences";
    private static final String USER_ID_KEY="user_id";
    private static final String USER_NAME_KEY="user_name";
    private static final String USER_SIGNATURE_KEY="user_signature";



    public SharedPreferenceHelper(Context mContext) {
        this.mContext = mContext;
        this.sharedPreferences= this.mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();
    }

    public void saveUserId(int userId) {
        editor.putInt(USER_ID_KEY, userId);
        editor.commit();
    }

    public int getUserId(){
        int userId=sharedPreferences.getInt(USER_ID_KEY,0);
        return userId;
    }

    public void saveUserName(String username) {
        editor.putString(USER_NAME_KEY, username);
        editor.commit();
    }

    public String getUserName(){
        String userId=sharedPreferences.getString(USER_NAME_KEY,"");
        return userId;
    }

    public void saveUserSignature(String signature) {
        editor.putString(USER_SIGNATURE_KEY, signature);
        editor.commit();
    }

    public String getUserSignature(){
        String userId=sharedPreferences.getString(USER_SIGNATURE_KEY,"");
        return userId;
    }

    public void clear(){
        editor.putInt(USER_ID_KEY, 0);
        editor.putString(USER_NAME_KEY, "");
        editor.putString(USER_SIGNATURE_KEY, "");
        editor.commit();
    }
}
