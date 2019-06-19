package in.ac.spsu.placement.placementspsu.Helper;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {
    private static final String PREF_NAME = "SPSU";
    private static final String USER_NAME = "USER_NAME";
    private static final String USER_ID = "USER_ID";
    private static final String USER_TYPE = "USER_TYPE";
    private static final String FORGET = "FORGET";

    public static String getFORGET() {
        return FORGET;
    }

    public static boolean setFORGET(Context context, String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(FORGET, data);
        return editor.commit();
    }

    public static String getUserName(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(USER_NAME, "");
    }

    public static int getUserId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(USER_ID, 9);
    }

    public static int getUserType(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(USER_TYPE, 9);
    }

    public static boolean setUserName(Context context, String data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(USER_NAME, data);
        return editor.commit();
    }

    public static boolean setUserId(Context context, int data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_ID, data);
        return editor.commit();
    }

    public static boolean setUserType(Context context, int data){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(USER_TYPE, data);
        return editor.commit();
    }
}
