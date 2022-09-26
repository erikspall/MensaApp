package de.erikspall.mensaapp.data.sources.remote.api.model

data class ApiResponse<T> (
    val data: T,
    val status: Int
)