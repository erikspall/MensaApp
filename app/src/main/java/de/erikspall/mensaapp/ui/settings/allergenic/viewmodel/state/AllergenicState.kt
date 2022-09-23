package de.erikspall.mensaapp.ui.settings.allergenic.viewmodel.state

import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Location
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum

data class AllergenicState (
        val warningsActivated: MutableLiveData<Boolean> = MutableLiveData(false)
)