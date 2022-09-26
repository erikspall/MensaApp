package de.erikspall.mensaapp.ui.settings.viewmodel.state

import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Location
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum

data class SettingsState (
    val role: MutableLiveData<Role>  = MutableLiveData(Role.STUDENT),
    val location: MutableLiveData<Location> = MutableLiveData(Location.WUERZBURG),
    val warningsActivated: MutableLiveData<Boolean> = MutableLiveData(false)
)