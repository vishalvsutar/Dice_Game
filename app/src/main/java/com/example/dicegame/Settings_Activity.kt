package com.example.dicegame

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate.NightMode
import androidx.appcompat.widget.SwitchCompat

class Settings_Activity : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var saveData: SwitchCompat
    lateinit var nightMode: SwitchCompat
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        saveData = findViewById(R.id.saveData)
        nightMode = findViewById(R.id.nightMode)
        sharedPreferences = getSharedPreferences(Constants.SHOULD_SAVE_SCORE, MODE_PRIVATE)
        //to get the previously set value on settings page
        saveData.isChecked = sharedPreferences.getBoolean(Constants.SWITCH_KEY, false)
        nightMode.isChecked = sharedPreferences.getBoolean(Constants.NIGHT_KEY, false)
        saveData.setOnCheckedChangeListener { _, _ ->
            var editor = sharedPreferences.edit()
            editor.putBoolean(Constants.SWITCH_KEY, saveData.isChecked)

            editor.apply()
        }
        nightMode.setOnCheckedChangeListener { _, _ ->
            var editor = sharedPreferences.edit()
            editor.putBoolean(Constants.NIGHT_KEY, nightMode.isChecked)
            editor.apply()
        }

    }
}