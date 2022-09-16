package de.erikspall.mensaapp.data.sources.remote.api.model

data class OpeningInfoApiModel(
    val id: Int,
    val opensAt: String,
    val closesAt: String,
    val getFoodTill: String,
    val opened: Boolean
)
