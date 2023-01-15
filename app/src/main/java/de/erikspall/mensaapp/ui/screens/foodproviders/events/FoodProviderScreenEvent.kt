package de.erikspall.mensaapp.ui.screens.foodproviders.events

import de.erikspall.mensaapp.ui.state.UiState

sealed class FoodProviderScreenEvent {
    object Init: FoodProviderScreenEvent()
    data class SetUiState(val uiState: UiState): FoodProviderScreenEvent()
    object GetLatest: FoodProviderScreenEvent()
    object UpdateOpeningHours : FoodProviderScreenEvent()
}