package de.erikspall.mensaapp.ui.settings.allergenic.event

import de.erikspall.mensaapp.data.sources.local.database.entities.MealComponent
import de.erikspall.mensaapp.domain.enums.StringResEnum
import de.erikspall.mensaapp.ui.settings.event.SettingsEvent

sealed class AllergenicEvent {
    data class OnWarningsChanged(val warningsActivated: Boolean): AllergenicEvent()
    data class OnAllergenicChecked(val name: String, val checked: Boolean): AllergenicEvent()
    data class OnIngredientChecked(val name: String, val checked: Boolean): AllergenicEvent()
}