package gpsutils.wcshxx.com.gps.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Set;

import gpsutils.wcshxx.com.gps.R;
import gpsutils.wcshxx.com.gps.base.context.App;

public class SharePreUtils {

    private static final SharedPreferences DEFAULT_SP = App.getApplication().getSharedPreferences("wcshxx_base", Context.MODE_PRIVATE);
    private static final SharedPreferences.Editor DEFAULT_EDITOR = DEFAULT_SP.edit();

    public static final int DEFAULT_INT = -1;
    public static final long DEFAULT_LONG = -1L;
    public static final float DEFAULT_FLOAT = -1f;


    public static void saveBoolean(String key,boolean b){
        DEFAULT_EDITOR.putBoolean(key,b);
        DEFAULT_EDITOR.commit();
    }

    public static void saveString(String key,String s){
        DEFAULT_EDITOR.putString(key,s);
        DEFAULT_EDITOR.commit();
    }

    public static void saveInt(String key,int i){
        DEFAULT_EDITOR.putInt(key,i);
        DEFAULT_EDITOR.commit();
    }

    public static void saveLong(String key,long l){
        DEFAULT_EDITOR.putLong(key,l);
        DEFAULT_EDITOR.commit();
    }

    public static void saveFloat(String key,float f){
        DEFAULT_EDITOR.putFloat(key,f);
        DEFAULT_EDITOR.commit();
    }

    public static void saveSet(String key,Set<String> set){
        DEFAULT_EDITOR.putStringSet(key,set);
        DEFAULT_EDITOR.commit();
    }

    /********************************/

    public static void saveBoolean(String name,String key,boolean b){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean(key,b);
        editor.commit();
    }

    public static void saveString(String name,String key,String s){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putString(key,s);
        editor.commit();
    }

    public static void saveInt(String name,String key,int i){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key,i);
        editor.commit();
    }

    public static void saveLong(String name,String key,long l){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putLong(key,l);
        editor.commit();
    }

    public static void saveFloat(String name,String key,float f){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putFloat(key,f);
        editor.commit();
    }

    public static void saveSet(String name,String key,Set<String> set){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sp.edit();
        editor.putStringSet(key,set);
        editor.commit();
    }

    /**************************/

    public static boolean getBoolean(String key){
        return DEFAULT_SP.getBoolean(key,false);
    }

    public static int getInt(String key){
        return DEFAULT_SP.getInt(key,DEFAULT_INT);
    }

    public static long getLong(String key){
        return DEFAULT_SP.getLong(key,DEFAULT_LONG);
    }

    public static String getString(String key){
        return DEFAULT_SP.getString(key,null);
    }

    public static float getFloat(String key){
        return DEFAULT_SP.getFloat(key,DEFAULT_FLOAT);
    }

    public static Set<String> getSet(String key){
        return DEFAULT_SP.getStringSet(key,null);
    }

    /****************************************/

    public static boolean getBoolean(String name,String key){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getBoolean(key,false);
    }

    public static int getInt(String name,String key){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getInt(key,DEFAULT_INT);
    }

    public static long getLong(String name,String key){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getLong(key,DEFAULT_LONG);
    }

    public static String getString(String name,String key){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getString(key,null);
    }

    public static float getFloat(String name,String key){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getFloat(key,DEFAULT_FLOAT);
    }

    public static Set<String> getSet(String name,String key){
        final SharedPreferences sp = App.getApplication().getSharedPreferences(name, Context.MODE_PRIVATE);
        return sp.getStringSet(key,null);
    }

}
