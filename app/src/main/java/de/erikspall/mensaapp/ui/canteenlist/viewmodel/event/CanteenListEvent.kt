package de.erikspall.mensaapp.ui.canteenlist.viewmodel.event

sealed class CanteenListEvent {
    object CheckIfNewLocationSet: CanteenListEvent()
}