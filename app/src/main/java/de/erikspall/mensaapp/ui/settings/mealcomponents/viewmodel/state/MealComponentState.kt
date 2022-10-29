package de.erikspall.mensaapp.ui.settings.mealcomponents.viewmodel.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.domain.model.Additive

data class MealComponentState (
    val warningsActivated: MutableLiveData<Boolean> = MutableLiveData(false),
    val ingredients: LiveData<List<Additive>> = MutableLiveData(emptyList()),
    val allergens: LiveData<List<Additive>> = MutableLiveData(emptyList())
)