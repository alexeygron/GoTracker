package com.example.monitor.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.Toolbar;

import com.lotr.steammonitor.app.R;

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}