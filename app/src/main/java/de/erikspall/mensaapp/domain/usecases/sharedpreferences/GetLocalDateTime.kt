package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import java.time.LocalDateTime

data class GetLocalDateTime(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {
    operator fun invoke(@StringRes key: Int, defaultValue: LocalDateTime): LocalDateTime {
        val rawDate = sharedPreferences.getString(context.getString(key), "")
        return if (rawDate == "")
            defaultValue
        else {
            LocalDateTime.parse(rawDate)
        }
    }

    operator fun invoke(@StringRes key: Int, defaultValue: LocalDateTime, param: Int): LocalDateTime {
        val rawDate = sharedPreferences.getString(context.getString(key, param), "")
        return if (rawDate == "")
            defaultValue
        else {
            LocalDateTime.parse(rawDate)
        }
    }
}