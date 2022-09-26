package de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.Menu
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.StringResEnum

data class FoodProviderDetailState (
    var fid: Long = -1L,
    val menus: MutableLiveData<List<Menu>> = MutableLiveData(emptyList()),
    var warningsEnabled: Boolean = false,
    var role: Role = Role.STUDENT
)