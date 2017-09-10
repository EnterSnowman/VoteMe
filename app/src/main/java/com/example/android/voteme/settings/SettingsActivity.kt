package com.example.android.voteme.settings

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.android.voteme.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        fragmentManager.beginTransaction()
                .replace(R.id.container, SettingsFragment()).commit()
    }
}
