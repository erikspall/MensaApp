package de.erikspall.mensaapp.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.Role

class SettingsState {
    var settingsInitialized by mutableStateOf(false)
    var role by mutableStateOf(Role.STUDENT)
    var location by mutableStateOf(Location.WUERZBURG)
    var warningsActivated by mutableStateOf(false)
}