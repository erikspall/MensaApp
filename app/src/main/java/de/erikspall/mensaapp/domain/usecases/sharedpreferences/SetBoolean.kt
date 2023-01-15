package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.SharedPreferences

data class SetBoolean (
        private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(key: String, value: Boolean) {
        with (sharedPreferences.edit()) {
            putBoolean(key,value)
            commit()
        }
    }
}