package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.SharedPreferences
import androidx.annotation.StringRes

data class SetValue(
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(key: String, @StringRes value: Int) {
        with (sharedPreferences.edit()) {
            putInt(key,value)
            commit()
        }
    }
}
