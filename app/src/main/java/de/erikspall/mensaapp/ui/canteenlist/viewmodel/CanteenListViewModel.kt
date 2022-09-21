package de.erikspall.mensaapp.ui.canteenlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.usecases.foodprovider.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.FoodProviderOrder
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.OrderType
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.canteenlist.viewmodel.state.CanteenListState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CanteenListViewModel @Inject constructor(
    foodProviderUseCases: FoodProviderUseCases,
    sharedPreferences: SharedPreferenceUseCases,
) : ViewModel() {

    val state = CanteenListState()

    val canteens = foodProviderUseCases.getFoodProviders(
        FoodProviderOrder.Name(OrderType.Descending),
        location = sharedPreferences.getValue(R.string.shared_pref_location, R.string.location_wuerzburg),
        typeId = 1 // TODO: find a way to make this an enum
    ).asLiveData()


}