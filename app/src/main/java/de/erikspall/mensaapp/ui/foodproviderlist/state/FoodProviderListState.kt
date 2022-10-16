package de.erikspall.mensaapp.ui.foodproviderlist.state

import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.ui.state.UiState

class FoodProviderListState {
    var location: Location = Location.WUERZBURG
    val uiState: MutableLiveData<UiState> = MutableLiveData(UiState.NORMAL)
    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false)
}