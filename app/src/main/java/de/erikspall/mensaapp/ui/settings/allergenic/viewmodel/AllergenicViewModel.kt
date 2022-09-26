package de.erikspall.mensaapp.ui.settings.allergenic.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.usecases.mealcomponents.MealComponentsUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.settings.allergenic.event.AllergenicEvent
import de.erikspall.mensaapp.ui.settings.allergenic.viewmodel.state.AllergenicState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllergenicViewModel @Inject constructor(
        @ApplicationContext private val context: Context,
        private val sharedPreferences: SharedPreferenceUseCases,
        private val mealComponentsUseCases: MealComponentsUseCases
) : ViewModel() {
    val state = AllergenicState(
            warningsActivated = MutableLiveData(sharedPreferences.getBoolean(R.string.setting_warnings_enabled, false)),
            allergenic = mealComponentsUseCases.getAllergenic().asLiveData(),
            ingredients = mealComponentsUseCases.getIngredients().asLiveData()
    )
    fun onEvent(event: AllergenicEvent) {
        when (event) {
            is AllergenicEvent.OnWarningsChanged -> {
                sharedPreferences.setBoolean(R.string.setting_warnings_enabled, event.warningsActivated)
                state.warningsActivated.postValue(event.warningsActivated) // TODO change to sharedPreferences listener
            }
            is AllergenicEvent.OnAllergenicChecked -> {
                viewModelScope.launch {
                    mealComponentsUseCases.setAllergenicLikeStatus(event.name, event.checked)
                }
            }
            is AllergenicEvent.OnIngredientChecked -> {
                viewModelScope.launch {
                    mealComponentsUseCases.setIngredientLikeStatus(event.name, event.checked)
                }
            }
        }
    }
}