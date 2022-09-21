package de.erikspall.mensaapp.data.sources.remote.api.model

data class OpeningInfoApiModel(
    val isOpened: Boolean,
    val weekday: String,
    val opensAt: String,
    val closesAt: String,
   // val getFoodTill: String,

)
