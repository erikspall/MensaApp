package de.erikspall.mensaapp.domain.usecases.additives

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.data.repositories.interfaces.AppRepository
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.model.Additive

class GetAdditives(
    private val repository: AppRepository,
) {
    operator fun invoke(type: AdditiveType): LiveData<List<Additive>> =
        if (type == AdditiveType.ALLERGEN)
            repository.allAllergens
        else
            repository.allIngredients

}