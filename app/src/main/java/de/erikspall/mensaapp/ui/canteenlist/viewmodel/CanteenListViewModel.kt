package de.erikspall.mensaapp.ui.canteenlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Location
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum
import de.erikspall.mensaapp.domain.usecases.foodprovider.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.FoodProviderOrder
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.OrderType
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.domain.utils.Extensions.flattenToList
import de.erikspall.mensaapp.ui.canteenlist.viewmodel.event.CanteenListEvent
import de.erikspall.mensaapp.ui.canteenlist.viewmodel.state.CanteenListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CanteenListViewModel @Inject constructor(
    private val foodProviderUseCases: FoodProviderUseCases,
    private val sharedPreferences: SharedPreferenceUseCases,
) : ViewModel() {

    val state = CanteenListState()

    val canteens = foodProviderUseCases.getFoodProviders(
        FoodProviderOrder.Name(OrderType.Descending),
        typeId = 1 // TODO: find a way to make this an enum
    ).asLiveData()

    fun onEvent(event: CanteenListEvent) {
        when (event) {
            is CanteenListEvent.CheckIfNewLocationSet -> {
                val newLocationValue = sharedPreferences.getValueRes(
                    R.string.shared_pref_location,
                    R.string.location_wuerzburg
                )

                if (state.showingLocation.getValue() != newLocationValue)

                    state.showingLocation = StringResEnum.locationFrom(newLocationValue)


            }
            is CanteenListEvent.NewUiState -> {
                state.uiState.postValue(event.uiState)
            }
            is CanteenListEvent.GetLatestInfo -> {
                viewModelScope.launch {
                    state.isRefreshing.postValue(true)
                    onEvent(
                        CanteenListEvent.NewUiState(
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