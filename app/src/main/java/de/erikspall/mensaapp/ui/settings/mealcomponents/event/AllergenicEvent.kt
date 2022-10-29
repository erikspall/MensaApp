package de.erikspall.mensaapp.ui.settings.mealcomponents.event

import de.erikspall.mensaapp.domain.enums.AdditiveType

sealed class AllergenicEvent {
    object Init: AllergenicEvent()
    data class OnWarningsChanged(val warningsActivated: Boolean): AllergenicEvent()
    data class OnAdditiveChecked(val name: String, val type: AdditiveType, val checked: Boolean): AllergenicEvent()
}