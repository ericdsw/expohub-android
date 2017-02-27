package com.example.ericdesedas.expohub.presentation.fragments;


import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;

public class SettingsFragment extends PreferenceFragment {

    private PreferenceHelper preferenceHelper;

    public SettingsFragment() {
        //
    }

    public static SettingsFragment newInstance(PreferenceHelper preferenceHelper) {
        SettingsFragment settingsFragment = new SettingsFragment();
        settingsFragment.preferenceHelper = preferenceHelper;
        return settingsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        getPreferenceScreen().findPreference("pref_key_reset_warning").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                preferenceHelper.writeBoolPref(PreferenceHelper.DO_NOT_SHOW_DISCLAIMER, false);
                Toast.makeText(getActivity(), getString(R.string.messages_reset), Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }
}
