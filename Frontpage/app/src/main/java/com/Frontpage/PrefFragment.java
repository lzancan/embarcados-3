package com.Frontpage;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceFragment;


/**
 * Created by root on 06/12/16.
 */
@TargetApi(11)
public class PrefFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

    }
}