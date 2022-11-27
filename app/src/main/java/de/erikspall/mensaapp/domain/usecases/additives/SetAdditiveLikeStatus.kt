package de.erikspall.mensaapp.domain.usecases.additives

import de.erikspall.mensaapp.domain.interfaces.data.AppRepository
import de.erikspall.mensaapp.domain.enums.AdditiveType

class SetAdditiveLikeStatus (
    private val repository: AppRepository,
) {
    suspend operator fun invoke(name: String, type: AdditiveType, userDoesNotLike: Boolean) =
        repository.setAdditiveLikeStatus(name, type, userDoesNotLike)

}