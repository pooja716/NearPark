package aksharproject.np;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by acer on 9/5/2017.
 */
public class mySharedPreference {
    public static final String SHARED_PREFERENCE_FILE_NAME = "shared_preference_file_name";
    public static final String KEY_NAME="UserName";
    public static final String KEY_Role="role";
    public static final String KEY_Id="id";

    public boolean addSharedPreferenceString(Context context, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
        return true;
    }

    public boolean addSharedPreferenceInt(Context context,String key,int value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
        return true;
    }

    public String getSharedPreferenceString(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,null);
    }

    public int getSharedPreferenceInt(Context context,String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME,Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key,0);
    }

    public String removeSharedPreferenceString(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
        return key;
    }

    public void removeSharedPreferenceInt(Context context, String key){
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }
}
