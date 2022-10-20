package de.erikspall.mensaapp.domain.usecases.mealcomponents

import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.AllergenEntity
import kotlinx.coroutines.flow.Flow

class GetAllergens (
    private val repository: AppRepository,
) {
    operator fun invoke(): Flow<List<AllergenEntity>> = repository.allAllergens

}