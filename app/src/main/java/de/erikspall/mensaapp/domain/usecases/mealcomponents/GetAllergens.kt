package de.erikspall.mensaapp.domain.usecases.mealcomponents

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.AllergenEntity
import kotlinx.coroutines.flow.Flow

class GetAllergens (
    private val repository: AppRepository,
) {
    operator fun invoke(): LiveData<List<AllergenEntity>> = repository.allAllergens

}