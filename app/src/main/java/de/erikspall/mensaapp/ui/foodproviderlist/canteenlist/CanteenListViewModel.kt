package de.erikspall.mensaapp.ui.foodproviderlist.canteenlist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.StringResEnum
import de.erikspall.mensaapp.domain.usecases.foodproviders.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.domain.utils.queries.QueryUtils
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

    val canteens = foodProviderUseCases.get(Location.WUERZBURG, Category.CANTEEN)


    fun onEvent(event: FoodProviderListEvent) {
        if (event is FoodProviderListEvent.Init) {
            val newLocationValue = sharedPreferences.getValueRes(
                R.string.shared_pref_location,
                R.string.location_wuerzburg
            )

            if (state.location.getValue() != newLocationValue) {
                state.location = StringResEnum.locationFrom(newLocationValue)

            }
        }
    }

    companion object {
        const val TAG = "CanteenListViewModel"
    }
}