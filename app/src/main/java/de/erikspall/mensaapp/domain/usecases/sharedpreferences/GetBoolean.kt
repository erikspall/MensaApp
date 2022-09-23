package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes

data class GetBoolean(
        private val context: Context,
        private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(@StringRes key: Int, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(
                context.getString(key),
                defaultValue
        )

    }
}
