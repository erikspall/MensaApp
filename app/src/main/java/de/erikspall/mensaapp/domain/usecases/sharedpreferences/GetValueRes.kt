package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.SharedPreferences
import androidx.annotation.StringRes

data class GetValueRes(
    private val sharedPreferences: SharedPreferences
) {
    @StringRes
    operator fun invoke(key: String, @StringRes defaultValue: Int): Int {
        return sharedPreferences.getInt(
            key,
            defaultValue
        )
    }
}
