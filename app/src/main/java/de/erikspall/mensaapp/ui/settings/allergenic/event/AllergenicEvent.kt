package de.erikspall.mensaapp.ui.settings.allergenic.event

sealed class AllergenicEvent {
    data class OnWarningsChanged(val warningsActivated: Boolean): AllergenicEvent()
    data class OnAllergenicChecked(val name: String, val checked: Boolean): AllergenicEvent()
    data class OnIngredientChecked(val name: String, val checked: Boolean): AllergenicEvent()
}