package com.example.android.voteme.settings

import android.app.AlertDialog
import android.os.Bundle
import android.preference.ListPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceScreen

import com.example.android.voteme.R
import com.example.android.voteme.data.UserRepository

/**
 * Created by Valentin on 10.09.2017.
 */
class SettingsFragment : PreferenceFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
        var emailPref = findPreference("user_email") as EmailVerificationDialog
        emailPref.summary = UserRepository.getInstance().getCurrentUser()?.email
    }


}