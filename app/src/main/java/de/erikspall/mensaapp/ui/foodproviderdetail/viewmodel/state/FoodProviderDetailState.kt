package de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.Menu
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum
import de.erikspall.mensaapp.ui.state.UiState

data class FoodProviderDetailState (
    var fid: Long = -1L,
    val menus: MutableLiveData<List<Menu>> = MutableLiveData(emptyList()),
    var warningsEnabled: Boolean = false,
    var role: Role = Role.STUDENT,
    val uiState: MutableLiveData<UiState> = MutableLiveData(UiState.LOADING),
    var showingCafeteria: Boolean = false
)