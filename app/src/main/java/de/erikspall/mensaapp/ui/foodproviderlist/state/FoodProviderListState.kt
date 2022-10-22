package de.erikspall.mensaapp.ui.foodproviderlist.state

import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.ui.state.UiState

data class FoodProviderListState (
    var location: Location = Location.WUERZBURG,
    val uiState: MutableLiveData<UiState> = MutableLiveData(UiState.NORMAL),
    val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false),
    //val receivedData: MutableLiveData<Boolean> = MutableLiveData(false)
    val foodProviders: MutableLiveData<List<FoodProvider>> = MutableLiveData(emptyList())
)