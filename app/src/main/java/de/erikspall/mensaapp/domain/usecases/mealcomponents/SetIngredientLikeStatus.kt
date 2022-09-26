package de.erikspall.mensaapp.domain.usecases.mealcomponents

import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.Allergenic
import kotlinx.coroutines.flow.Flow

class SetIngredientLikeStatus(
    private val repository: AppRepository,
) {
    suspend operator fun invoke(name: String, userDoesNotLike: Boolean) =
        repository.setIngredientLikeStatus(name, userDoesNotLike)

}