package de.erikspall.mensaapp.domain.usecases.additives

import de.erikspall.mensaapp.domain.interfaces.data.AppRepository
import de.erikspall.mensaapp.domain.model.Additive

class FetchLatest(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(): Result<List<Additive>> {
        return appRepository.fetchAllAdditives()
    }
}