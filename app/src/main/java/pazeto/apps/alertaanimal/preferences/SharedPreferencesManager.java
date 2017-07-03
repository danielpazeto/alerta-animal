package pazeto.apps.alertaanimal.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import pazeto.apps.alertaanimal.R;

/**
 * Created by pazeto on 5/22/17.
 */

class SharedPreferencesManager {

    final Context ctx;
    private final SharedPreferences sharedPref;

    public SharedPreferencesManager(Context ctx){
        this.ctx = ctx;
        sharedPref = ctx.getSharedPreferences(
                ctx.getString(R.string.shared_pref_main_key), Context.MODE_PRIVATE);
    }

    public Object get(String key, Class<? extends Object> cls){
        String json = sharedPref.getString(key, "");
        Object obj = null;
        if(!Objects.equals(json, "")){
            obj = new Gson().fromJson(json, cls);
        }
        return obj;
    }

    public void put(String key, Object object) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        Gson gson = new Gson();
        String json = gson.toJson(object, object.getClass());
        prefsEditor.putString(key, json);
        prefsEditor.apply();
    }

    public void putList(String key, List<? extends Object> list) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();

        Set<String> setStringToSave = new HashSet<>();
        for (Object obj : list) {
            Gson gson = new Gson();
            String json = gson.toJson(obj, obj.getClass());
            setStringToSave.add(json);
        }
        prefsEditor.putStringSet(key, setStringToSave);
        prefsEditor.apply();
    }

    public List<? extends Object> getList(String key, Class<? extends Object> cls) {
        Set<String> setStrings = sharedPref.getStringSet(key, null);
        List<Object> list = new ArrayList<>();
        if(setStrings != null) {
            for (String strJson : setStrings) {
                list.add(new Gson().fromJson(strJson, cls));
            }
        }
        return list;
    }

    public void remove(String key) {
        SharedPreferences.Editor prefsEditor = sharedPref.edit();
        prefsEditor.remove(key);
        prefsEditor.apply();
    }

}
