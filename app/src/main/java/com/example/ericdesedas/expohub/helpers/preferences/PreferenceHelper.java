package com.example.ericdesedas.expohub.helpers.preferences;

import android.content.SharedPreferences;

public class PreferenceHelper {

    public static final String FIRST_VISIT  = "first-visit";
    public static final String SESSION_DATA = "session-data";

    private SharedPreferences preferences;

    public PreferenceHelper(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    // Write

    public void writeStringPreference(String key) {
        this.writeStringPref(key, "");
    }

    public void writeBoolPref(String key) {
        this.writeBoolPref(key, false);
    }

    public void writeIntPref(String key) {
        this.writeIntPref(key, 0);
    }

    public void writeStringPref(String key, String defaultValue) {
        preferences.edit()
                .putString(key, defaultValue)
                .apply();
    }

    public void writeBoolPref(String key, boolean defaultValue) {
        preferences.edit()
                .putBoolean(key, defaultValue)
                .apply();
    }

    public void writeIntPref(String key, int defaultValue) {
        preferences.edit()
                .putInt(key, defaultValue)
                .apply();
    }

    // Read

    public String readStringPref(String key) {
        return readStringPref(key, "");
    }

    public boolean readBooleanPref(String key) {
        return readBooleanPref(key, false);
    }

    public int readIntPref(String key) {
        return readIntPref(key, 0);
    }

    public String readStringPref(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public boolean readBooleanPref(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public int readIntPref(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }
}
