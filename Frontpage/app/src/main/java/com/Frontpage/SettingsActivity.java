package com.Frontpage;

import android.preference.PreferenceActivity;

import android.os.Bundle;

/**
 * Created by root on 05/12/16.
 */

public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
