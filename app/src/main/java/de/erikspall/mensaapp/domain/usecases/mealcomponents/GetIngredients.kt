package de.erikspall.mensaapp.domain.usecases.mealcomponents

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.IngredientEntity
import kotlinx.coroutines.flow.Flow
import java.util.*

class GetIngredients (
    private val repository: AppRepository,
) {
    operator fun invoke(): LiveData<List<IngredientEntity>> = repository.allIngredients
}