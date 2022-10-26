package de.erikspall.mensaapp.domain.usecases.additives

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.data.repositories.AppRepositoryImpl
import de.erikspall.mensaapp.data.repositories.interfaces.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.AdditiveEntity
import de.erikspall.mensaapp.domain.enums.AdditiveType

class GetAdditives(
    private val repository: AppRepository,
) {
    operator fun invoke(type: AdditiveType): LiveData<List<AdditiveEntity>> =
        if (type == AdditiveType.ALLERGEN)
            repository.allAllergens
        else
            repository.allIngredients

}