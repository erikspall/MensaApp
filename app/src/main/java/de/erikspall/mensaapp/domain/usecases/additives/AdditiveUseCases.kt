package de.erikspall.mensaapp.domain.usecases.additives

data class AdditiveUseCases (
    val getAdditives: GetAdditives,
    val setAdditiveLikeStatus: SetAdditiveLikeStatus,
    val fetchLatest: FetchLatest
)