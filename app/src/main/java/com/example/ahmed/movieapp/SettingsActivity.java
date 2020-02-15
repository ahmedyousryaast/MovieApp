package com.example.ahmed.movieapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

/**
 * Created by Ahmed on 11/4/2016.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    public static class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{


        @Override
        public void onCreate(final Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);
            PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
        }


        /**
         * i had a problem that the summary value was not showing
         * and i was using a deprecated method so i extended PreferenceFragment
         * sources that helped me : http://stackoverflow.com/questions/26115042/setting-summary-to-describe-the-current-value
         * @param sharedPreferences
         * @param key
         */
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(R.string.sort_by)) {
                ListPreference listPreference = (ListPreference)findPreference(key);
                int prefIndex = listPreference.findIndexOfValue(key);
                if (prefIndex >= 0) {
                    final String summary = (String)listPreference.getEntries()[prefIndex];
                    listPreference.setSummary(summary);
                }
            }

        }


    }
}
