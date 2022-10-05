package de.erikspall.mensaapp.domain.usecases.foodprovider

import android.util.Log
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.ui.state.UiState

class FetchLatest(
    private val repository: AppRepository,
) {
    suspend operator fun invoke(): UiState {
        val result = repository.fetchAndSaveLatestData()
        if (result.isEmpty)
            Log.d("ErrorPropagation", result.getMessage())
        return if (result.isPresent)
            UiState.NORMAL
        else {
            when (result.getMessage()) {
                "no internet" -> UiState.NO_INTERNET
                "server unreachable" -> UiState.NO_CONNECTION
                else -> UiState.ERROR
            }
        }
    }
}