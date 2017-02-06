package com.team.istiqomah.istiqomahcontroller.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

/**
 * Created by alde.asprilla on 31/10/2016.
 */

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    public static final String KEY_LOKASI = "Lokasi";
    public static final String KEY_LONGITUDE = "Longitude";
    public static final String KEY_LATITUDE = "Latitude";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences("IstiqomahController", PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setActiveInformation(String Lokasi, String Longitude, String Latitude) {
        // Storing name in pref
        editor.putString(KEY_LOKASI, Lokasi);
        editor.putString(KEY_LATITUDE, Latitude);
        editor.putString(KEY_LONGITUDE, Longitude);
        // commit changes
        editor.commit();
    }

    /* Get stored session data */
    public HashMap<String, String> getActiveInformation() {
        HashMap<String, String> activeUser = new HashMap<String, String>();
        // user id
        activeUser.put(KEY_LOKASI, pref.getString(KEY_LOKASI, ""));
        activeUser.put(KEY_LONGITUDE, pref.getString(KEY_LONGITUDE, ""));
        activeUser.put(KEY_LATITUDE, pref.getString(KEY_LATITUDE, ""));
        return activeUser;
    }

    public boolean isFistOpen(){
        return pref.getString(KEY_LOKASI, "") == "" ? true : false;
    }
}