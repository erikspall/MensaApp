package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.SharedPreferences

data class GetBoolean(
        private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(
                key,
                defaultValue
        )

    }
}
