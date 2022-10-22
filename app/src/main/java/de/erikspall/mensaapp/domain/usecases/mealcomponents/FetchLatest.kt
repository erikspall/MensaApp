package de.erikspall.mensaapp.domain.usecases.mealcomponents

import com.google.firebase.firestore.Source
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.domain.model.Additive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FetchLatest(
    private val appRepository: AppRepository
) {
    suspend operator fun invoke(): OptionalResult<List<Additive>> {
        return appRepository.fetchAllAdditives()
    }
}