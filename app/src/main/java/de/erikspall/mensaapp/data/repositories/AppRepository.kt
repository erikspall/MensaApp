package de.erikspall.mensaapp.data.repositories

import androidx.annotation.DrawableRes
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.sources.local.database.entities.*
import de.erikspall.mensaapp.data.sources.remote.api.RemoteApiDataSource
import de.erikspall.mensaapp.data.sources.remote.api.model.FoodProviderApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.MealApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.MenuApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.OpeningInfoApiModel
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.FoodProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class AppRepository(
    private val allergenicRepository: AllergenicRepository,
    private val ingredientRepository: IngredientRepository,
    private val foodProvidersRef: CollectionReference
) {

    val allAllergenic: Flow<List<Allergenic>> =
        allergenicRepository.getAll()

    val allIngredients: Flow<List<Ingredient>> =
        ingredientRepository.getAll()


    suspend fun getFoodProvidersFromFirestore(location: String, category: String): OptionalResult<List<FoodProvider>> {
        try {
            val foodProviderList = mutableListOf<FoodProvider>()
            val foodProviders = foodProvidersRef
                .whereEqualTo(FoodProvider.FIELD_LOCATION, location)
                .whereEqualTo(FoodProvider.FIELD_CATEGORY, category)
                .orderBy(FoodProvider.FIELD_NAME, Query.Direction.ASCENDING)
                .get()
                .await()
            for (document in foodProviders) {
                document.toObject<FoodProvider>().let {
                    foodProviderList.add(it)
                }
            }
            return OptionalResult.of(foodProviderList)
        } catch (e: FirebaseFirestoreException) {
            return OptionalResult.ofMsg(e.message ?: "An error occurred")
        }
    }


    suspend fun setAllergenicLikeStatus(name: String, userDoesNotLike: Boolean) {
        allergenicRepository.updateLike(name, userDoesNotLike)
    }

    suspend fun setIngredientLikeStatus(name: String, userDoesNotLike: Boolean) {
        ingredientRepository.updateLike(name, userDoesNotLike)
    }

    private suspend fun getOrInsertIngredient(name: String): MealComponent {
        return if (ingredientRepository.exists(name)) {
            ingredientRepository.get(name)!!
        } else {
            val ingredient = Ingredient(
                name = name,
                icon = R.drawable.ic_mensa // TODO: extract icon
            )
            if (ingredient.getName()
                    .isNotBlank()
            ) // TODO: find a better way if meal cannot be parsed
                ingredientRepository.insert(
                    ingredient
                )
            ingredient
        }
    }

    private suspend fun getOrInsertAllergenic(name: String): MealComponent {
        return if (allergenicRepository.exists(name)) {
            allergenicRepository.get(name)!!
        } else {
            val allergenic = Allergenic(
                name = name,
                icon = R.drawable.ic_mensa // TODO: extract icon
            )
            if (allergenic.getName().isNotBlank())
                allergenicRepository.insert(
                    allergenic
                )
            allergenic
        }
    }


    @DrawableRes
    private fun getIconId(
        name: String,
        type: String,
        location: String,
        imgMap: Map<String, Int>
    ): Int {
        val formattedName =
            "${type.formatToResString()}_${name.formatToResString()}_${location.formatToResString()}"
        return imgMap[formattedName]
            ?: R.drawable.mensateria_campus_hubland_nord_wuerzburg // TODO: set default img
    }

    private fun String.formatToResString(): String {
        return this.lowercase()
            .replace("-", "_")
            .replace("ä", "ae")
            .replace("ö", "oe")
            .replace("ü", "ue")
            .replace("ß", "ss")
            .replace(" ", "_")
    }
}