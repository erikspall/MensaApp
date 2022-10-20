package de.erikspall.mensaapp.ui.settings.mealcomponents.viewmodel

import android.content.Context
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.usecases.mealcomponents.MealComponentUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.settings.mealcomponents.event.AllergenicEvent
import de.erikspall.mensaapp.ui.settings.mealcomponents.viewmodel.state.MealComponentState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MealComponentViewModel @Inject constructor(
        @ApplicationContext private val context: Context,
        private val sharedPreferences: SharedPreferenceUseCases,
        private val mealComponentUseCases: MealComponentUseCases
) : ViewModel() {
    val state = MealComponentState(
            warningsActivated = MutableLiveData(sharedPreferences.getBoolean(R.string.setting_warnings_enabled, false)),
            allergens = mealComponentUseCases.getAllergens().asLiveData(),
            ingredients = mealComponentUseCases.getIngredients().asLiveData()
    )
    fun onEvent(event: AllergenicEvent) {
        when (event) {
            is AllergenicEvent.OnWarningsChanged -> {
                sharedPreferences.setBoolean(R.string.setting_warnings_enabled, event.warningsActivated)
                state.warningsActivated.postValue(event.warningsActivated) // TODO change to sharedPreferences listener
            }
            is AllergenicEvent.OnAllergenicChecked -> {
                viewModelScope.launch {
                    mealComponentUseCases.setAllergenLikeStatus(event.name, event.checked)
                }
            }
            is AllergenicEvent.OnIngredientChecked -> {
                viewModelScope.launch {
                    mealComponentUseCases.setIngredientLikeStatus(event.name, event.checked)
                }
            }
        }
    }
}