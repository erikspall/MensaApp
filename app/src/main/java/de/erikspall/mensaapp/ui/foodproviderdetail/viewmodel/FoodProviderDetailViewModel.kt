package de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import de.erikspall.mensaapp.domain.usecases.foodprovider.FoodProviderUseCases
import de.erikspall.mensaapp.ui.foodproviderdetail.event.DetailEvent
import de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.state.FoodProviderDetailState
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodProviderDetailViewModel @Inject constructor(
    private val foodProviderUseCases: FoodProviderUseCases
) : ViewModel() {
    val state = FoodProviderDetailState()

    //val menus = foodProviderUseCases.getMenus()

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.Init -> {
                if (state.fid == -1L) {
                    state.fid = event.fid
                    onEvent(DetailEvent.RefreshMenus)
                }
            }
            is DetailEvent.RefreshMenus -> {
                viewModelScope.launch {
                    foodProviderUseCases.getMenus(state.fid).apply {
                        if (this.isPresent)
                            state.menus.postValue(this.get())
                        else
                            state.menus.postValue(emptyList())
                    }
                }
            }
        }
    }
}