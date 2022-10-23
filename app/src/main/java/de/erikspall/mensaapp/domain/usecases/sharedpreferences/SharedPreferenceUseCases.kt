package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.SharedPreferences

data class SharedPreferenceUseCases(
    val setValue: SetValue,
    val getValue: GetValue,
    val getValueRes: GetValueRes,
    val registerListener: RegisterListener,
    val getBoolean: GetBoolean,
    val setBoolean: SetBoolean,
    val getLocalDateTime: GetLocalDateTime,
    val setLocalDateTime: SetLocalDateTime
)
