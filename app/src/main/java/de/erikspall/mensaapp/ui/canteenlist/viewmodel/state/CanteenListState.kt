package de.erikspall.mensaapp.ui.canteenlist.viewmodel.state

import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Location
import de.erikspall.mensaapp.ui.state.UiState

class CanteenListState {
    var showingLocation: Location = Location.WUERZBURG
    val uiState: MutableLiveData<UiState> = MutableLiveData(UiState.NORMAL)
    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)
}