package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes

data class SetBoolean (
        private val context: Context,
        private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(@StringRes key: Int, value: Boolean) {
        with (sharedPreferences.edit()) {
            putBoolean(context.getString(key),value)
            commit()
        }
    }
}