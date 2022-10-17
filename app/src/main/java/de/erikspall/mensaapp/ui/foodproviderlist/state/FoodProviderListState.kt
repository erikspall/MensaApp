package de.erikspall.mensaapp.ui.foodproviderlist.state

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.Query
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.utils.queries.QueryUtils
import de.erikspall.mensaapp.ui.foodproviderlist.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.state.UiState

data class FoodProviderListState (
    var location: Location = Location.WUERZBURG,
   // val uiState: MutableLiveData<UiState> = MutableLiveData(UiState.NORMAL),
    //val isRefreshing: MutableLiveData<Boolean> = MutableLiveData(false),
)