package de.erikspall.mensaapp.ui.settings.event

import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Location
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum

sealed class SettingsEvent {
    object OnInit: SettingsEvent()
    data class OnNewRole(val role: StringResEnum): SettingsEvent()
    data class OnNewLocation(val location: StringResEnum): SettingsEvent()
}
