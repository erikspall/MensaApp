package de.erikspall.mensaapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.StringResEnum
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.usecases.foodproviders.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.openinghours.OpeningHourUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.screens.foodproviders.FoodProvidersState
import de.erikspall.mensaapp.ui.screens.foodproviders.events.FoodProviderScreenEvent
import de.erikspall.mensaapp.ui.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MensaAppViewModel @Inject constructor(
    private val foodProviderUseCases: FoodProviderUseCases,
    private val openingHourUseCases: OpeningHourUseCases,
    private val sharedPreferences: SharedPreferenceUseCases,
) : ViewModel() {

    private val state = FoodProvidersState()

    val foodProviders: List<FoodProvider>
        get() = state.foodProviders.filter { f ->
            if (state.location != Location.ANY) {
                Location.from(
                    f.location
                ) == state.location
            } else {
                true
            }
        }

    val canteens: List<FoodProvider>
        get() = state.foodProviders.filter { f ->
            f.category == Category.CANTEEN.getValue() && if (state.location != Location.ANY) {
                Location.from(
                    f.location
                ) == state.location
            } else {
                true
            }
        }
    val cafeterias: List<FoodProvider>
        get() = state.foodProviders.filter { f ->
            f.category == Category.CAFETERIA.getValue() && if (state.location != Location.ANY) {
                Location.from(
                    f.location
                ) == state.location
            } else {
                true
            }
        }


    fun onEvent(event: FoodProviderScreenEvent) {
        when (event) {
            is FoodProviderScreenEvent.Init -> {
                Log.d("$TAG:init", "Entered Init Block")

                val savedLocation = sharedPreferences.getValueRes(
                    R.string.shared_pref_location,
                    R.string.location_wuerzburg
                )

                if (
                    state.location.getValue() != savedLocation ||
                    state.foodProviders.isEmpty()
                ) {
                    state.location = StringResEnum.locationFrom(savedLocation)
                    state.uiState = UiState.LOADING
                    onEvent(FoodProviderScreenEvent.GetLatest)
                }
            }
            is FoodProviderScreenEvent.GetLatest -> {
                Log.d("$TAG:init", "Entered GetLatest Block")
                viewModelScope.launch {
                    val result = foodProviderUseCases.fetchAll(
                        location = state.location,
                        category = Category.ANY
                    )

                    if (result.isSuccess) {
                        Log.d("$TAG:init", "Fetched ${result.getOrThrow().size} items")
                        state.foodProviders.clear()
                        state.foodProviders.addAll(result.getOrDefault(emptyList()))
                        state.uiState = UiState.NORMAL
                    } else {
                        Log.d("$TAG:init", "Error while fetching: ${result.exceptionOrNull()}")
                        state.uiState = UiState.ERROR
                    }
                }
            }
            else -> {

            }
        }
    }

    companion object {
        const val TAG = "MensaAppViewModel"
    }
}

