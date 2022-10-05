package de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum
import de.erikspall.mensaapp.domain.usecases.foodprovider.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.foodproviderdetail.event.DetailEvent
import de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.state.FoodProviderDetailState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodProviderDetailViewModel @Inject constructor(
    private val foodProviderUseCases: FoodProviderUseCases,
    private val preferencesUseCases: SharedPreferenceUseCases
) : ViewModel() {
    val state = FoodProviderDetailState()

    //val menus = foodProviderUseCases.getMenus()

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.Init -> {
                if (state.fid == -1L) {
                    state.fid = event.fid
                    state.warningsEnabled =
                        preferencesUseCases.getBoolean(R.string.setting_warnings_enabled, false)

                    state.role =
                        StringResEnum.roleFrom(
                            preferencesUseCases.getValueRes(
                                key = R.string.shared_pref_role,
                                defaultValue = Role.STUDENT.getValue()
                            )
                        )
                    onEvent(DetailEvent.RefreshMenus)
                }
            }
            is DetailEvent.RefreshMenus -> {
                viewModelScope.launch {
                    val result = foodProviderUseCases.getMenus(state.fid).apply {
                        if (this.isPresent)
                            state.menus.postValue(this.get())
                        else
                            state.menus.postValue(emptyList())
                    }
                    if (result.isEmpty)
                        Log.d("ErrorPropagation", "Error while retrieving menus: ${result.getMessage()}")
                }
            }
        }
    }
}