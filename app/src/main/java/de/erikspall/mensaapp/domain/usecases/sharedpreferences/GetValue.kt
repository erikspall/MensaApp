package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes

data class GetValue(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    operator fun invoke(key: String, @StringRes defaultValue: Int): String {
        return context.getString(
            sharedPreferences.getInt(
                key,
                defaultValue
            )
        )
    }
}
