package de.erikspall.mensaapp.ui.settings.allergenic.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.settings.allergenic.event.AllergenicEvent
import de.erikspall.mensaapp.ui.settings.allergenic.viewmodel.state.AllergenicState
import javax.inject.Inject

@HiltViewModel
class AllergenicViewModel @Inject constructor(
        @ApplicationContext private val context: Context,
        private val sharedPreferences: SharedPreferenceUseCases
) : ViewModel() {
    val state = AllergenicState(
            warningsActivated = MutableLiveData(sharedPreferences.getBoolean(R.string.setting_warnings_enabled, false))
    )
    fun onEvent(event: AllergenicEvent) {
        when (event) {
            is AllergenicEvent.OnWarningsChanged -> {
                sharedPreferences.setBoolean(R.string.setting_warnings_enabled, event.warningsActivated)
                state.warningsActivated.postValue(event.warningsActivated) // TODO change to sharedPreferences listener
            }
        }
    }
}