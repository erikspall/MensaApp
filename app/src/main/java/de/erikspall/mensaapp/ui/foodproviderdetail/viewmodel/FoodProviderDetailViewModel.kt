package de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.Source
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.errorhandling.OptionalResultMsg
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.enums.StringResEnum
import de.erikspall.mensaapp.domain.usecases.foodproviders.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.foodproviderdetail.event.DetailEvent
import de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.state.FoodProviderDetailState
import de.erikspall.mensaapp.ui.state.UiState
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.*
import javax.inject.Inject

@HiltViewModel
class FoodProviderDetailViewModel @Inject constructor(
    private val preferencesUseCases: SharedPreferenceUseCases,
    private val foodProviderUseCases: FoodProviderUseCases
) : ViewModel() {
    val state = FoodProviderDetailState()
    //val menus = foodProviderUseCases.getMenus()

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.Init -> {
                if (event.showingCafeteria) {
                    state.category = Category.CAFETERIA
                    state.uiState.value = UiState.NO_INFO
                } else if (state.foodProvider.value == null) {
                    state.foodProviderId = event.foodProviderId

                    viewModelScope.launch {
                        Log.d("$TAG:setters", "Retrieving info of FoodProvider ${event.foodProviderId}")
                        foodProviderUseCases.fetch(
                            event.foodProviderId
                        ).apply {
                            if (this.isPresent) {
                                Log.d("$TAG:setters", "Setting FoodProvider ...")
                                state.foodProvider.value = (this.get())
                            }
                        }

                    }

                    state.warningsEnabled =
                        preferencesUseCases.getBoolean(R.string.setting_warnings_enabled, false)


                    state.role =
                        StringResEnum.roleFrom(
                            preferencesUseCases.getValueRes(
                                key = R.string.shared_pref_role,
                                defaultValue = Role.STUDENT.getValue()
                            )
                        )
                    // TODO block and then do that
                    Log.d("$TAG:setters", "Refreshing menus ...")
                    onEvent(DetailEvent.RefreshMenus)

                }
            }
            is DetailEvent.RefreshMenus -> {
                if (state.category != Category.CAFETERIA) // Obsolete, always false here ?
                    viewModelScope.launch {
                        Log.d("$TAG:setters", "Retrieving menus of foodprovider ${state.foodProviderId}")
                        val result = foodProviderUseCases.fetchMenus(
                            foodProviderId = state.foodProviderId,
                            date = LocalDate.now()
                        ).apply {

                            if (this.isPresent) {
                                Log.d("$TAG:setters", "Setting ${this.get().size} menus")
                                state.menus.postValue(this.get())
                            } else {
                                Log.d("$TAG:setters", "No menus found!")
                                state.menus.postValue(emptyList())
                            }

                        }
                        if (result.isEmpty) {
                            // Set UI state
                            Log.d(
                                "ErrorPropagation",
                                "Error while retrieving menus: ${result.getMessage()}"
                            )
                            when (result.getMessage()) {
                                "server unreachable" -> state.uiState.postValue(UiState.NO_INTERNET)
                                "server not responding" -> state.uiState.postValue(UiState.NO_CONNECTION)
                                "no menus" -> state.uiState.postValue(UiState.NO_INFO)
                                else -> state.uiState.postValue(UiState.ERROR)
                            }
                        }
                    }
            }
        }
    }
    companion object {
        const val TAG = "DetailViewModel"
    }
}