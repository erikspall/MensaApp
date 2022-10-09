package de.erikspall.mensaapp.ui.foodproviderdetail.event

sealed class DetailEvent {
    data class Init(val fid: Long, val showingCafeteria: Boolean = false): DetailEvent()
    object RefreshMenus: DetailEvent()
}