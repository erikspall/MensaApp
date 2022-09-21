package de.erikspall.mensaapp.domain.usecases.sharedpreferences

import android.content.SharedPreferences

data class SharedPreferenceUseCases(
    val setValue: SetValue,
    val getValue: GetValue,
    val getValueRes: GetValueRes,
    val registerListener: RegisterListener
)
