package de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.state

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import de.erikspall.mensaapp.data.sources.local.database.entities.Menu

data class FoodProviderDetailState (
    var fid: Long = -1L,
    val menus: MutableLiveData<List<Menu>> = MutableLiveData(emptyList())
)