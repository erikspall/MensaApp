package de.erikspall.mensaapp.ui.settings.viewmodel.state

import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum

data class SettingsState (
    val role: MutableLiveData<StringResEnum>  = MutableLiveData(Role.STUDENT),
    val location: MutableLiveData<String> = MutableLiveData(""),
    val warningsActivated: MutableLiveData<Boolean> = MutableLiveData(false)
)