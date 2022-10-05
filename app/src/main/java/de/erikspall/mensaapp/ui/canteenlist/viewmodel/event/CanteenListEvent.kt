package de.erikspall.mensaapp.ui.canteenlist.viewmodel.event

import de.erikspall.mensaapp.ui.state.UiState

sealed class CanteenListEvent {
    object CheckIfNewLocationSet: CanteenListEvent()
    data class NewUiState(val uiState: UiState): CanteenListEvent()
    object GetLatestInfo: CanteenListEvent()
}