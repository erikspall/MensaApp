package de.erikspall.mensaapp.domain.usecases.additives

import de.erikspall.mensaapp.domain.interfaces.data.AppRepository
import de.erikspall.mensaapp.domain.model.Additive

class SetAdditiveLikeStatus (
    private val repository: AppRepository,
) {
    suspend operator fun invoke(additive: Additive, userDoesNotLike: Boolean) =
        repository.setAdditiveLikeStatus(additive.name, additive.type, userDoesNotLike)

}