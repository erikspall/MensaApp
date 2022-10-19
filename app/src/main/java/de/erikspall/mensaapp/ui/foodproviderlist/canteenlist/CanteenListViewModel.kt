package de.erikspall.mensaapp.ui.foodproviderlist.canteenlist

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.StringResEnum
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.OpeningHour
import de.erikspall.mensaapp.domain.usecases.foodproviders.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.openinghours.OpeningHourUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.domain.utils.queries.QueryUtils
import de.erikspall.mensaapp.ui.foodproviderlist.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.foodproviderlist.event.FoodProviderListEvent
import de.erikspall.mensaapp.ui.foodproviderlist.state.FoodProviderListState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CanteenListViewModel @Inject constructor(
    private val foodProviderUseCases: FoodProviderUseCases,
    private val openingHourUseCases: OpeningHourUseCases,
    private val sharedPreferences: SharedPreferenceUseCases,
) : ViewModel() {

    val state = FoodProviderListState()

    var canteens = foodProviderUseCases.getAll(
        Category.CANTEEN
    )


    fun onEvent(event: FoodProviderListEvent) {
        when (event) {
            is FoodProviderListEvent.Init -> {
                val newLocationValue = sharedPreferences.getValueRes(
                    R.string.shared_pref_location,
                    R.string.location_wuerzburg
                )

                if (state.location.getValue() != newLocationValue || canteens.value == null) {
                    state.location = StringResEnum.locationFrom(newLocationValue)
                }
            }
            is FoodProviderListEvent.SetUiState -> {
                state.uiState.postValue(event.uiState)
            }
            is FoodProviderListEvent.GetLatest -> {
                // TODO: only allow server fetch every x mins/hours/days?

                canteens = foodProviderUseCases.getAll(
                    Category.CANTEEN,
                    Source.SERVER
                )
                // Notify Fragment that it should update its list
                state.receivedData.postValue(!state.receivedData.value!!)
            }
            is FoodProviderListEvent.UpdateOpeningHours -> {
                canteens.value ?: return

                Log.d("$TAG:event-updating-hours", "Let's update!")

                canteens.value!!.get().forEach { oldCanteen ->
                    oldCanteen.openingHoursString = openingHourUseCases.formatToString(
                        oldCanteen.openingHours,
                        LocalDateTime.now(),
                        Locale.getDefault()
                    )
                }
                // Notify Fragment that it should update its list
                state.receivedData.postValue(!state.receivedData.value!!)
            }
        }
    }

    companion object {
        const val TAG = "CanteenListViewModel"
    }
}