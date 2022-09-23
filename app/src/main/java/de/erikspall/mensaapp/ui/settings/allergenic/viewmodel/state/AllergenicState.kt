package de.erikspall.mensaapp.ui.settings.allergenic.viewmodel.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.Allergenic
import de.erikspall.mensaapp.data.sources.local.database.entities.Ingredient
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Location
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum

data class AllergenicState (
        val warningsActivated: MutableLiveData<Boolean> = MutableLiveData(false),
        val ingredients: LiveData<List<Ingredient>>,
        val allergenic: LiveData<List<Allergenic>>
)