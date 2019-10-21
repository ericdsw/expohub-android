package com.example.ericdesedas.expohub.presentation.activities;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.example.ericdesedas.expohub.R;
import com.example.ericdesedas.expohub.helpers.preferences.PreferenceHelper;
import com.example.ericdesedas.expohub.presentation.fragments.SettingsFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;

    @Inject PreferenceHelper preferenceHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.title_settings));

        getActivityComponent().inject(this);

        SettingsFragment settingsFragment = SettingsFragment.newInstance(preferenceHelper);

        getFragmentManager().beginTransaction()
                .replace(R.id.base_view, settingsFragment)
                .commit();
    }
}
