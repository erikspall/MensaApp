package de.erikspall.mensaapp.ui.canteenlist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.domain.usecases.foodprovider.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.FoodProviderOrder
import de.erikspall.mensaapp.domain.usecases.foodprovider.order.OrderType
import de.erikspall.mensaapp.ui.canteenlist.viewmodel.state.CanteenListState
import javax.inject.Inject

@HiltViewModel
class CanteenListViewModel @Inject constructor(
    foodProviderUseCases: FoodProviderUseCases
) : ViewModel() {

    val state = CanteenListState()

    val canteens = foodProviderUseCases.getFoodProvidersWithoutMenu(
        FoodProviderOrder.Name(OrderType.Descending)
    ).asLiveData()


}