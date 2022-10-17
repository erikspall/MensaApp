package de.erikspall.mensaapp.ui.foodproviderlist.event

import android.content.Context
import de.erikspall.mensaapp.ui.foodproviderlist.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.state.UiState

sealed class FoodProviderListEvent {
    data class Init(val adapter: FoodProviderCardAdapter): FoodProviderListEvent()
    //data class NewUiState(val uiState: UiState): FoodProviderListEvent()
    // ConstructQueryIfNecessary: FoodProviderListEvent()
}