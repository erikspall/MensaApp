package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes
import de.erikspall.mensaapp.R

data class GetValue(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    operator fun invoke(@StringRes key: Int, @StringRes defaultValue: Int): String {
        return context.getString(
            sharedPreferences.getInt(
                context.getString(key),
                defaultValue
            )
        )
    }
}
