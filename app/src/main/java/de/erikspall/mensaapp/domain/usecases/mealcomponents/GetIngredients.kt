package de.erikspall.mensaapp.domain.usecases.mealcomponents

import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.Ingredient
import kotlinx.coroutines.flow.Flow

class GetIngredients (
    private val repository: AppRepository,
) {
    operator fun invoke(): Flow<List<Ingredient>> = repository.allIngredients
}