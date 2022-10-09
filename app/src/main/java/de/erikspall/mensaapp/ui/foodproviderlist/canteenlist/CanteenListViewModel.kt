package de.erikspall.mensaapp.ui.foodproviderlist.canteenlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum
import de.erikspall.mensaapp.domain.usecases.foodprovider.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.FoodProviderOrder
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.OrderType
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.foodproviderlist.event.FoodProviderListEvent
import de.erikspall.mensaapp.ui.foodproviderlist.state.FoodProviderListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CanteenListViewModel @Inject constructor(
    private val foodProviderUseCases: FoodProviderUseCases,
    private val sharedPreferences: SharedPreferenceUseCases,
) : ViewModel() {

    val state = FoodProviderListState()

    val canteens = foodProviderUseCases.getFoodProviders(
        FoodProviderOrder.Name(OrderType.Descending),
        typeId = 1 // TODO: find a way to make this an enum
    ).asLiveData()

    fun onEvent(event: FoodProviderListEvent) {
        when (event) {
            is FoodProviderListEvent.CheckIfNewLocationSet -> {
                val newLocationValue = sharedPreferences.getValueRes(
                    R.string.shared_pref_location,
                    R.string.location_wuerzburg
                )

                if (state.location.getValue() != newLocationValue)

                    state.location = StringResEnum.locationFrom(newLocationValue)


            }
            is FoodProviderListEvent.NewUiState -> {
                state.uiState.postValue(event.uiState)
            }
            is FoodProviderListEvent.GetLatestInfo -> {
                viewModelScope.launch {
                    state.isRefreshing.postValue(true)
                    onEvent(
                        FoodProviderListEvent.NewUiState( // useless!
                            foodProviderUseCases.fetchLatest()
                        )
                    )
                    withContext(Dispatchers.Main) {
                        state.isRefreshing.postValue(false)
                    }
                }
            }
        }
    }
}