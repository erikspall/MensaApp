package de.erikspall.mensaapp.ui.foodproviderlist.event

import de.erikspall.mensaapp.ui.state.UiState

sealed class FoodProviderListEvent {
    object CheckIfNewLocationSet: FoodProviderListEvent()
    data class NewUiState(val uiState: UiState): FoodProviderListEvent()
    object GetLatestInfo: FoodProviderListEvent()
}