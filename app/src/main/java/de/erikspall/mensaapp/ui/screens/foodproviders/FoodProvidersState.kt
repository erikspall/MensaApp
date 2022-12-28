package de.erikspall.mensaapp.ui.screens.foodproviders

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.ui.state.UiState

class FoodProvidersState {
    var location: Location = Location.WUERZBURG
    var uiState by mutableStateOf(UiState.NORMAL)
    var foodProviders: SnapshotStateList<FoodProvider> = mutableStateListOf()
}