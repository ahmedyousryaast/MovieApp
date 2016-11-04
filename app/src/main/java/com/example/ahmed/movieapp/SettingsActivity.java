package com.example.ahmed.movieapp;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by Ahmed on 11/4/2016.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);
    }
}
