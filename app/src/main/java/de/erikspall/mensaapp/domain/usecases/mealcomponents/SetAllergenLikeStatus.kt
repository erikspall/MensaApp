package de.erikspall.mensaapp.domain.usecases.mealcomponents

import de.erikspall.mensaapp.data.repositories.AppRepository

class SetAllergenLikeStatus (
    private val repository: AppRepository,
) {
    suspend operator fun invoke(name: String, userDoesNotLike: Boolean) =
        repository.setAllergenicLikeStatus(name, userDoesNotLike)

}