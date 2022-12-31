package de.erikspall.mensaapp.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider

class MensaAppState {
    var location by mutableStateOf(Location.WUERZBURG)
    var role by mutableStateOf(Role.STUDENT)
    var warningsActivated by mutableStateOf(false)
    //var canteenUiState by mutableStateOf(UiState.NORMAL)
    var foodProviders: SnapshotStateList<FoodProvider> = mutableStateListOf()
    var additives: SnapshotStateList<Additive> = mutableStateListOf()
    var settingsInitialized by mutableStateOf(false)
}