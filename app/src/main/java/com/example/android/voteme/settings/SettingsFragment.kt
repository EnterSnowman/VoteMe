package com.example.android.voteme.settings

import android.os.Bundle
import android.preference.PreferenceFragment
import com.example.android.voteme.R

/**
 * Created by Valentin on 10.09.2017.
 */
class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }
}