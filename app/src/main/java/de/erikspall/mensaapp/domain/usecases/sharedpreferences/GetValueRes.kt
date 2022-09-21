package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.StringRes

data class GetValueRes(
    private val context: Context,
    private val sharedPreferences: SharedPreferences
) {
    @StringRes
    operator fun invoke(@StringRes key: Int, @StringRes defaultValue: Int): Int {
        return sharedPreferences.getInt(
            context.getString(key),
            defaultValue
        )
    }
}
