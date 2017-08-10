package com.example.monitor.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.lotr.steammonitor.app.R;

/**
 * In process
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}