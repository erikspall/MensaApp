package de.erikspall.mensaapp.ui.settings.allergenic.event

import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum
import de.erikspall.mensaapp.ui.settings.event.SettingsEvent

sealed class AllergenicEvent {
    data class OnWarningsChanged(val warningsActivated: Boolean): AllergenicEvent()
}