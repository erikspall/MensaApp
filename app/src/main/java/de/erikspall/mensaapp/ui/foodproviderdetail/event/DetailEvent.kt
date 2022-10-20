package de.erikspall.mensaapp.ui.foodproviderdetail.event

sealed class DetailEvent {
    data class Init(val foodProviderId: Int, val showingCafeteria: Boolean = false): DetailEvent()
    object RefreshMenus: DetailEvent()
}