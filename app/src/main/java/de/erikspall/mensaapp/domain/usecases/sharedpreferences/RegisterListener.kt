package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.SharedPreferences

data class RegisterListener(
    private val sharedPreferences: SharedPreferences
) {

    private var listener: SharedPreferences.OnSharedPreferenceChangeListener? = null

    operator fun invoke(listener: (SharedPreferences, String) -> Unit) {
        if (this.listener != null) {
            sharedPreferences.unregisterOnSharedPreferenceChangeListener(this.listener)
            this.listener = null
        }

        this.listener = SharedPreferences.OnSharedPreferenceChangeListener(listener)
        sharedPreferences.registerOnSharedPreferenceChangeListener(this.listener)
    }
}

