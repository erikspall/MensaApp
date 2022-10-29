package de.erikspall.mensaapp.data.repositories

import androidx.lifecycle.LiveData
import de.erikspall.mensaapp.data.repositories.interfaces.AppRepository
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.domain.model.Menu
import java.time.LocalDate

class FakeAppRepository(
    var foodProviders: List<FoodProvider> = mutableListOf(),
    var menus: Map<Int, List<Menu>> = mutableMapOf(),
    var additives: List<Additive> = mutableListOf(),
    var fakeAdditiveRepository: FakeAdditiveRepository
): AppRepository {

    override val allAllergens: LiveData<List<Additive>>
        get() = fakeAdditiveRepository.getAll().also { liveData -> liveData.value!!.filter { it.type == AdditiveType.ALLERGEN } }
    override val allIngredients: LiveData<List<Additive>>
        get() = fakeAdditiveRepository.getAll().also { liveData -> liveData.value!!.filter { it.type == AdditiveType.INGREDIENT } }

    override suspend fun fetchFoodProviders(
        location: Location,
        category: Category
    ): Result<List<FoodProvider>> {
        val filtered = foodProviders.filter {
            Location.from(it.location) == location && Category.from(it.category) == category
        }
        return if (filtered.isEmpty())
            Result.failure(NoSuchElementException())
        else
            Result.success(filtered)
    }

    override suspend fun fetchFoodProvider(foodProviderId: Int): Result<FoodProvider> {
        val filtered = foodProviders.filter {
            it.id == foodProviderId
        }

        return if (filtered.count() != 1)
            Result.failure(NoSuchElementException())
        else
            Result.success(filtered.first())
    }

    override suspend fun fetchAllAdditives(): Result<List<Additive>> {
        if (additives.isNotEmpty()) {
            for (fakeAdditive in additives) {
                fakeAdditiveRepository.insert(fakeAdditive)
            }
        } else {
            return Result.failure(NoSuchElementException())
        }
        return Result.success(additives)
    }

    override suspend fun fetchMenus(foodProviderId: Int, date: LocalDate): Result<List<Menu>> {
        val filtered = menus[foodProviderId]?.filter { it.date >= date }?.sortedBy { it.date } ?: emptyList()
        return if (filtered.isEmpty())
            Result.failure(NoSuchElementException())
        else
            Result.success(filtered)
    }

    override suspend fun setAdditiveLikeStatus(
        name: String,
        type: AdditiveType,
        userDoesNotLike: Boolean
    ) {
        return fakeAdditiveRepository.updateLike(name, type, userDoesNotLike)
    }
}