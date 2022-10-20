package de.erikspall.mensaapp.ui.settings.mealcomponents.event

sealed class AllergenicEvent {
    data class OnWarningsChanged(val warningsActivated: Boolean): AllergenicEvent()
    data class OnAllergenicChecked(val name: String, val checked: Boolean): AllergenicEvent()
    data class OnIngredientChecked(val name: String, val checked: Boolean): AllergenicEvent()
}