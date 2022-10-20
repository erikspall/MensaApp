package de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.state

import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Menu
import de.erikspall.mensaapp.ui.state.UiState

data class FoodProviderDetailState (
    var foodProvider: MutableLiveData<FoodProvider?> = MutableLiveData(),
    var foodProviderId: Int = -1,
    val menus: MutableLiveData<List<Menu>> = MutableLiveData(emptyList()),
    var warningsEnabled: Boolean = false,
    var role: Role = Role.STUDENT,
    val uiState: MutableLiveData<UiState> = MutableLiveData(UiState.LOADING),
    var category: Category = Category.CANTEEN
)