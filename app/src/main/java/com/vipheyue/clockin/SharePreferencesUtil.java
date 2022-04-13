package com.vipheyue.clockin;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by heyue on 2015/10/31.
 */
public class SharePreferencesUtil {
    private static SharePreferencesUtil SPUtil =null;
    private SharePreferencesUtil(){}

    public static SharedPreferences getSP(Context context) {
        SharedPreferences sp = context.getSharedPreferences(context.getPackageName()+"sharePreferencesConfig", Context.MODE_PRIVATE);
        return sp;
    }
    public  static void putSpBoolean(String info , Boolean flag, Context context) {
        SharedPreferences sp=  getSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(info, flag);
        editor.commit();
    }
    public static void putSpString (String title , String info, Context context) {
        SharedPreferences sp=  getSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(title, info);
        editor.commit();
    }

    public static String  getSpString(String title, String defaultVal, Context context) {
        SharedPreferences sp=  getSP(context);
      return   sp.getString(title, defaultVal);
    }
    public static Boolean getSpBoolean(String title, Boolean defaultVal, Context context) {
        SharedPreferences sp=  getSP(context);
      return   sp.getBoolean(title, defaultVal);
    }
}
