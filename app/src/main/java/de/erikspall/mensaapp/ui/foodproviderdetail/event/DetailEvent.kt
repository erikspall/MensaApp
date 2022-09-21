package de.erikspall.mensaapp.ui.foodproviderdetail.event

sealed class DetailEvent {
    data class Init(val fid: Long): DetailEvent()
    object RefreshMenus: DetailEvent()
}