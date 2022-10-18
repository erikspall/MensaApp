package de.erikspall.mensaapp.ui.settings.allergenic.viewmodel.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.AllergenicEntity
import de.erikspall.mensaapp.data.sources.local.database.entities.IngredientEntity

data class AllergenicState (
    val warningsActivated: MutableLiveData<Boolean> = MutableLiveData(false),
    val ingredients: LiveData<List<IngredientEntity>> = MutableLiveData(emptyList()),
    val allergenicEntity: LiveData<List<AllergenicEntity>> = MutableLiveData(emptyList())
)