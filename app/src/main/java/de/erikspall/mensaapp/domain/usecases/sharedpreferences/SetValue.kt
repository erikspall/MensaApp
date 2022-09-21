package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes

data class SetValue(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(@StringRes key: Int, @StringRes value: Int) {
        with (sharedPreferences.edit()) {
            putInt(context.getString(key),value)
            commit()
        }
    }
}
