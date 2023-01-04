package de.erikspall.mensaapp.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.domain.const.SharedPrefKey
import de.erikspall.mensaapp.domain.enums.*
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.usecases.additives.AdditiveUseCases
import de.erikspall.mensaapp.domain.usecases.foodproviders.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.openinghours.OpeningHourUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MensaViewModel @Inject constructor(
    private val foodProviderUseCases: FoodProviderUseCases,
    private val openingHourUseCases: OpeningHourUseCases,
    private val sharedPreferences: SharedPreferenceUseCases,
    private val additiveUseCases: AdditiveUseCases
) : ViewModel() {

    private val state = MensaAppState()

    val role: Role
        get() = state.role
    val location: Location
        get() = state.location
    val warningsActivated: Boolean
        get() = state.warningsActivated
    val settingsInitialized: Boolean
        get() = state.settingsInitialized


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

    val additives
        get() = additiveUseCases.getAdditives(AdditiveType.ALLERGEN)

    val ingredients
        get() = additiveUseCases.getAdditives(AdditiveType.INGREDIENT)



    val allergens
        get() = additiveUseCases.getAdditives(AdditiveType.ALLERGEN)

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

    // Init Block has to be below var declarations!
    init {
        initViewModel()
    }

    private fun initViewModel() {
        initSettings()
        getFoodProviders()
    }
    private fun initSettings() {
        state.role = StringResEnum.roleFrom(
            sharedPreferences.getValueRes(
                key = SharedPrefKey.ROLE,
                defaultValue = Role.STUDENT.getValue()
            )
        )

        Log.d("${TAG}:Init", "Read role: ${state.role}")

        state.location = StringResEnum.locationFrom(
            sharedPreferences.getValueRes(
                key = SharedPrefKey.LOCATION,
                defaultValue = Location.WUERZBURG.getValue()
            )
        )

        state.warningsActivated = sharedPreferences.getBoolean(
            key = SharedPrefKey.WARNING,
            false
        )

        sharedPreferences.registerListener { prefs, key ->
            Log.d("${TAG}:SharedPrefListener", "Triggered")
            when (key) {
                SharedPrefKey.ROLE -> {
                    Log.d("${TAG}:SharedPrefListener", "New Role received")
                    state.role = StringResEnum.roleFrom(
                        prefs.getInt(
                            SharedPrefKey.ROLE,
                            Role.STUDENT.getValue()
                        )
                    )
                }
                SharedPrefKey.LOCATION -> {

                    val newLocation = StringResEnum.locationFrom(
                        prefs.getInt(
                            SharedPrefKey.LOCATION,
                            Location.WUERZBURG.getValue()
                        )
                    )
                    if (state.location != newLocation) {
                        state.location = StringResEnum.locationFrom(
                            prefs.getInt(
                                SharedPrefKey.LOCATION,
                                Location.WUERZBURG.getValue()
                            )
                        )
                        getFoodProviders()
                    }

                }
                SharedPrefKey.WARNING -> {
                    state.warningsActivated = prefs.getBoolean(
                        SharedPrefKey.WARNING,
                        false
                    )
                }
            }
        }
    }

    fun enableWarnings(enabled: Boolean) {
        sharedPreferences.setBoolean(
            SharedPrefKey.WARNING, enabled
        )
    }

    fun saveNewRole(role: Role) {
        Log.d("${TAG}:saveNewRole", "Saving role '$role' ...")
        sharedPreferences.setValue(SharedPrefKey.ROLE, role.getValue())
    }

    fun saveNewLocation(location: Location) {
        Log.d("${TAG}:saveNewRole", "Saving location '$location' ...")
        sharedPreferences.setValue(SharedPrefKey.LOCATION, location.getValue())
    }

    /**
     * Retrieves FoodProviders according to location.
     * Data-Layer will decide what source to use
     */
    private fun getFoodProviders() {
        viewModelScope.launch {
            val result = foodProviderUseCases.fetchAll(
                location = state.location,
                category = Category.ANY
            )

            if (result.isSuccess) {
                Log.d("$TAG:init", "Fetched ${result.getOrThrow().size} items")
                state.foodProviders.clear()
                state.foodProviders.addAll(result.getOrDefault(emptyList()))
                //   state.canteenUiState = UiState.NORMAL
            } else {
                Log.d("$TAG:init", "Error while fetching: ${result.exceptionOrNull()}")
                // state.canteenUiState = UiState.ERROR
            }
        }
    }

    /**
     * Retrieves Additives.
     * Data-Layer will decide what source to use
     */
    fun getAdditives() {
        viewModelScope.launch {
            additiveUseCases.fetchLatest()
        }
    }

    fun saveLikeStatus(additive: Additive, isNotLiked: Boolean) {
        viewModelScope.launch {
            additiveUseCases.setAdditiveLikeStatus(additive, isNotLiked)
        }

    }

    suspend fun getMenus(foodProviderId: Int, date: LocalDate) =
        foodProviderUseCases.fetchMenus(foodProviderId, date)



    companion object {
        const val TAG = "MensaViewModel"
    }
}

