package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import java.time.LocalDateTime

class SetLocalDateTime(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(key: String, value: LocalDateTime) {
        with(sharedPreferences.edit()) {
            putString(key, value.toString())
            commit()
        }
    }

    operator fun invoke(@StringRes key: Int, value: LocalDateTime, param: Int) {
        with(sharedPreferences.edit()) {
            putString(context.getString(key, param), value.toString())
            commit()
        }
    }
}