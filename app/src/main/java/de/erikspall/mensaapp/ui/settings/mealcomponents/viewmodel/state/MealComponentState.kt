package de.erikspall.mensaapp.ui.settings.mealcomponents.viewmodel.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.AllergenEntity
import de.erikspall.mensaapp.data.sources.local.database.entities.IngredientEntity

data class MealComponentState (
    val warningsActivated: MutableLiveData<Boolean> = MutableLiveData(false),
    val ingredients: LiveData<List<IngredientEntity>> = MutableLiveData(emptyList()),
    val allergens: LiveData<List<AllergenEntity>> = MutableLiveData(emptyList())
)