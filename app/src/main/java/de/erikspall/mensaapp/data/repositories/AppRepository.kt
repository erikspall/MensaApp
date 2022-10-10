package de.erikspall.mensaapp.data.repositories

import androidx.annotation.DrawableRes
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.sources.local.database.entities.*
import de.erikspall.mensaapp.data.sources.local.database.relationships.FoodProviderWithInfo
import de.erikspall.mensaapp.data.sources.local.database.relationships.FoodProviderWithoutMenus
import de.erikspall.mensaapp.data.sources.remote.RemoteApiDataSource
import de.erikspall.mensaapp.data.sources.remote.api.model.FoodProviderApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.MealApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.MenuApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.OpeningInfoApiModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.merge
import java.time.LocalDate
import java.util.Objects
import java.util.Optional
import kotlin.jvm.optionals.getOrDefault
import kotlin.jvm.optionals.getOrElse
import kotlin.streams.toList

class AppRepository(
    private val foodProviderRepository: FoodProviderRepository,
    private val locationRepository: LocationRepository,
    private val openingHoursRepository: OpeningHoursRepository,
    private val weekdayRepository: WeekdayRepository,
    private val foodProviderTypeRepository: FoodProviderTypeRepository,
    private val allergenicRepository: AllergenicRepository,
    private val ingredientRepository: IngredientRepository,
    private val remoteApiDataSource: RemoteApiDataSource,
    private val externalScope: CoroutineScope
) {

    val cachedProviders: Flow<List<FoodProviderWithoutMenus>> =
        foodProviderRepository.getFoodProvidersWithoutMenus()

    val allAllergenic: Flow<List<Allergenic>> =
        allergenicRepository.getAll()

    val allIngredients: Flow<List<Ingredient>> =
        ingredientRepository.getAll()


    /**
     * Fetches and saves all new data
     */
    suspend fun fetchAndSaveLatestData(): OptionalResult<List<FoodProviderApiModel>> {
        return externalScope.async { // TODO: reduce to withContext?
            remoteApiDataSource.fetchLatestFoodProviders().also { networkResult ->
                if (networkResult.isPresent) {
                    val result = emptyList<FoodProviderApiModel>()

                    networkResult.get().forEach { fetchedProvider ->
                        val fid = getOrInsertFoodProvider(fetchedProvider)
                        val hours = fetchedProvider.openingHours
                        hours.forEach { openingHours ->
                            val wid = getOrInsertWeekday(openingHours.weekday)
                            getOrInsertOpeningHours(openingHours, fid, wid)
                        }
                    }
                    OptionalResult.of(result) // Don't actually propagate result, as its handled by flow
                } else {
                    // Propagate possible error
                    OptionalResult.ofMsg(networkResult.getMessage())
                }
            }
        }.await()

    }

    fun getProvidersByTypeAndLocation(tid: Long, wid: Long): Flow<List<FoodProviderWithoutMenus>> {
        return foodProviderRepository.getFoodProvidersByTypeAndLocation(tid, wid)
    }

    suspend fun setAllergenicLikeStatus(name: String, userDoesNotLike: Boolean) {
        allergenicRepository.updateLike(name, userDoesNotLike)
    }

    suspend fun setIngredientLikeStatus(name: String, userDoesNotLike: Boolean) {
        ingredientRepository.updateLike(name, userDoesNotLike)
    }


    suspend fun fetchLatestMenuOfCanteen(cid: Long): OptionalResult<List<Menu>> {
        val result = remoteApiDataSource.fetchMenusOfCanteen(cid)
        return if (result.isPresent) { // Has to be done this way, because coroutine
            OptionalResult.of(result.get().map {
                parseMenu(it)
            })
        } else {
            OptionalResult.ofMsg(result.getMessage())
        }
    }

    private suspend fun parseMenu(fetchMenuOfCanteen: MenuApiModel): Menu {
        return Menu(
            date = LocalDate.parse(fetchMenuOfCanteen.date),
            meals = fetchMenuOfCanteen.meals.map {
                parseMeal(it)
            }
        )
    }

    private suspend fun parseMeal(fetchedMealOfCanteen: MealApiModel): Meal {
        return Meal(
            name = fetchedMealOfCanteen.name,
            priceStudent = fetchedMealOfCanteen.priceStudent,
            priceEmployee = fetchedMealOfCanteen.priceEmployee,
            priceGuest = fetchedMealOfCanteen.priceGuest,
            ingredients = fetchedMealOfCanteen.ingredients.split(",")
                .map { getOrInsertIngredient(it.trim()) }
                .toList(),
            allergens = fetchedMealOfCanteen.allergens.split(",")
                .map { getOrInsertAllergenic(it.trim()) }
                .toList()
        )
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

    fun getFoodProviderWithInfo(fid: Long): Flow<FoodProviderWithInfo> {
        return foodProviderRepository.getFoodProviderWithInfo(fid)
    }

    private suspend fun getOrInsertOpeningHours(
        apiOpeningHours: OpeningInfoApiModel,
        fid: Long,
        wid: Long
    ): Long {
        return if (openingHoursRepository.exists(fid, wid)) {
            openingHoursRepository.get(fid, wid)!!.oid
        } else {
            openingHoursRepository.insert(
                OpeningHours(
                    foodProviderId = fid,
                    weekdayId = getOrInsertWeekday(apiOpeningHours.weekday),
                    opensAt = apiOpeningHours.opensAt,
                    closesAt = apiOpeningHours.closesAt,
                    //getFoodTill = apiOpeningHours.getFoodTill,
                    opened = apiOpeningHours.isOpened
                )
            )
        }
    }

    private suspend fun getOrInsertWeekday(apiWeekday: String): Long {
        return if (weekdayRepository.exists(apiWeekday)) {
            weekdayRepository.get(apiWeekday)!!.wid
        } else {
            weekdayRepository.insert(
                Weekday(
                    name = apiWeekday
                )
            )
        }
    }

    private suspend fun getOrInsertFoodProvider(apiFoodProvider: FoodProviderApiModel): Long {
        val foodProviderImageMap = mapOf(
            "burse_am_studentenhaus_wuerzburg" to R.drawable.burse_am_studentenhaus_wuerzburg,
            "interimsmensa_sprachenzentrum_wuerzburg" to R.drawable.interimsmensa_sprachenzentrum_wuerzburg,
            "mensa_am_studentenhaus_wuerzburg" to R.drawable.mensa_am_studentenhaus_wuerzburg,
            "mensa_austrasse_bamberg" to R.drawable.mensa_austrasse_bamberg,
            "mensa_feldkirchenstrasse_bamberg" to R.drawable.mensa_feldkirchenstrasse_bamberg,
            "mensa_fhws_campus_schweinfurt" to R.drawable.mensa_fhws_campus_schweinfurt,
            "mensa_hochschulcampus_aschaffenburg" to R.drawable.mensa_hochschulcampus_aschaffenburg,
            "mensa_josef_schneider_strasse_wuerzburg" to R.drawable.mensa_josef_schneider_strasse_wuerzburg,
            "mensa_roentgenring_wuerzburg" to R.drawable.mensa_roentgenring_wuerzburg,
            "mensateria_campus_hubland_nord_wuerzburg" to R.drawable.mensateria_campus_hubland_nord_wuerzburg,
            "cafeteria_alte_universitaet_wuerzburg" to R.drawable.cafeteria_alte_universitaet_wuerzburg,
            "cafeteria_alte_weberei_bamberg" to R.drawable.cafeteria_alte_weberei_bamberg,
            "cafeteria_fhws_muenzstrasse_wuerzburg" to R.drawable.cafeteria_fhws_muenzstrasse_wuerzburg,
            "cafeteria_fhws_roentgenring_wuerzburg" to R.drawable.cafeteria_fhws_roentgenring_wuerzburg,
            "cafeteria_fhws_sanderheinrichsleitenweg_wuerzburg" to R.drawable.cafeteria_fhws_sanderheinrichsleitenweg_wuerzburg,
            "cafeteria_ledward_campus_schweinfurt" to R.drawable.cafeteria_ledward_campus_schweinfurt,
            "cafeteria_markusplatz_bamberg" to R.drawable.cafeteria_markusplatz_bamberg_day,
            "cafeteria_neue_universitaet_wuerzburg" to R.drawable.cafeteria_neue_universitaet_wuerzburg,
            "cafeteria_philo_wuerzburg" to R.drawable.cafeteria_philo_wuerzburg,
            "cafeteria_am_studentenhaus_wuerzburg" to R.drawable.mensa_am_studentenhaus_wuerzburg,
            "cafeteria_hochschulcampus_aschaffenburg" to R.drawable.mensa_hochschulcampus_aschaffenburg,
            "cafeteria_feldkirchenstrasse_bamberg" to R.drawable.mensa_feldkirchenstrasse_bamberg,
            "cafeteria_fhws_campus_schweinfurt" to R.drawable.mensa_fhws_campus_schweinfurt
        )

        return if (foodProviderRepository.exists(apiFoodProvider.id)) {
            apiFoodProvider.id
        } else {
            val type = apiFoodProvider.name.substringBefore(" ", "unknown")
            val name = apiFoodProvider.name.substringAfter(" ", "unknown")
                .substringBeforeLast(" ").replaceFirstChar { c -> c.uppercase() }
            foodProviderRepository.insert(
                FoodProvider(
                    fid = apiFoodProvider.id,
                    name = name,
                    foodProviderTypeId = getOrInsertFoodProviderType(apiFoodProvider.type),
                    locationId = getOrInsertLocation(apiFoodProvider.location),
                    info = apiFoodProvider.info,
                    additionalInfo = apiFoodProvider.additionalInfo,
                    type = type,
                    isFavorite = false,
                    icon = getIconId(name, type, apiFoodProvider.location, foodProviderImageMap)
                )
            )
        }
    }

    private suspend fun getOrInsertFoodProviderType(foodProviderType: String): Long {
        return if (foodProviderTypeRepository.exists(foodProviderType)) {
            foodProviderTypeRepository.get(foodProviderType)!!.tid
        } else {
            foodProviderTypeRepository.insert(FoodProviderType(name = foodProviderType))
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

    private suspend fun getOrInsertLocation(apiLocation: String): Long {
        return if (locationRepository.exists(apiLocation)) {
            locationRepository.get(apiLocation)!!.lid
        } else {
            locationRepository.insert(
                Location(
                    name = apiLocation
                )
            )
        }
    }

    suspend fun getLocation(apiLocation: String): Long? {
        return locationRepository.get(apiLocation)?.lid
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