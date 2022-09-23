package de.erikspall.mensaapp.domain.usecases.mealcomponents

import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.Allergenic
import kotlinx.coroutines.flow.Flow

class GetAllergenic (
    private val repository: AppRepository,
) {
    operator fun invoke(): Flow<List<Allergenic>> = repository.allAllergenic
}