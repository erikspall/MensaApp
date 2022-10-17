package de.erikspall.mensaapp.ui.foodproviderlist.event

import android.content.Context
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.ui.foodproviderlist.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.state.UiState

sealed class FoodProviderListEvent {
    object Init: FoodProviderListEvent()
    data class SetUiState(val uiState: UiState): FoodProviderListEvent()
    object GetLatest: FoodProviderListEvent()
}