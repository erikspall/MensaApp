package de.erikspall.mensaapp.ui.settings.viewmodel.state

import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.enums.StringResEnum

data class SettingsState (
    val role: MutableLiveData<Role>  = MutableLiveData(Role.STUDENT),
    val location: MutableLiveData<Location> = MutableLiveData(Location.WUERZBURG),
    val warningsActivated: MutableLiveData<Boolean> = MutableLiveData(false)
)