package de.erikspall.mensaapp.data.repositories

import androidx.annotation.DrawableRes
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.getField
import com.google.firebase.firestore.ktx.toObject
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.handler.SaveTimeHandler
import de.erikspall.mensaapp.data.handler.SourceHandler
import de.erikspall.mensaapp.data.requests.*
import de.erikspall.mensaapp.domain.interfaces.data.FirestoreRepository
import de.erikspall.mensaapp.data.sources.remote.firestore.FirestoreDataSource
import de.erikspall.mensaapp.domain.const.SharedPrefKey
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.interfaces.data.Request
import de.erikspall.mensaapp.domain.model.*
import de.erikspall.mensaapp.domain.usecases.openinghours.OpeningHourUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

class FirestoreRepositoryImpl(
    private val firestoreDataSource: FirestoreDataSource,
    private val openingHourUseCases: OpeningHourUseCases,
    private val sharedPreferenceUseCases: SharedPreferenceUseCases,
    private val sourceHandler: SourceHandler,
    private val saveTimeHandler: SaveTimeHandler
) : FirestoreRepository {

    override suspend fun fetchData(request: Request): Result<QuerySnapshot> {
        sourceHandler.handle(request)
        val result = request.execute(firestoreDataSource)
        if (result.isSuccess && !result.getOrThrow().isEmpty) {
            saveTimeHandler.handle(request)
        }
        return result
    }

    override suspend fun fetchFoodProviders(
        location: Location,
        category: Category
    ): Result<List<FoodProvider>> {

        val foodProviderSnapshot = fetchData(
            FoodProviderRequest(
                requestId = "$location$category",
                parameters = FoodProviderRequestParameters(
                    category = category,
                    location = location
                )
            )
        )

        val foodProviderList = mutableListOf<FoodProvider>()

        return if (foodProviderSnapshot.isSuccess) {
            for (document in foodProviderSnapshot.getOrThrow())
                document.toFoodProvider().let {
                    foodProviderList.add(it)
                }

            Result.success(foodProviderList)
        } else {
            foodProviderSnapshot.exceptionOrNull()!!.let { Result.failure(it) }
        }

    }

    override suspend fun fetchFoodProvider(
        foodProviderId: Int
    ): Result<FoodProvider> {
        val foodProviderSnapshot = fetchData(
            FoodProviderRequest(
                requestId = "foodProvider${foodProviderId}",
                parameters = FoodProviderRequestParameters(
                    foodProviderId = foodProviderId
                )
            )
        )


        return if (foodProviderSnapshot.isSuccess) {
            Result.success(foodProviderSnapshot.getOrThrow().first().toFoodProvider())
        } else
            Result.failure(foodProviderSnapshot.exceptionOrNull()!!)
    }

    override suspend fun fetchAdditives(
    ): Result<List<Additive>> {

        val additiveSnapshot = fetchData(AdditiveRequest())

        val additiveList = mutableListOf<Additive>()

        return if (additiveSnapshot.isSuccess) {
            for (document in additiveSnapshot.getOrThrow()) {
                additiveList.add(
                    Additive(
                        name = document.get(Additive.FIELD_NAME) as String,
                        type = AdditiveType.from(document.get(Additive.FIELD_TYPE) as String)
                            ?: AdditiveType.ALLERGEN
                    )
                )
            }

            Result.success(additiveList)
        } else {
            Result.failure(additiveSnapshot.exceptionOrNull()!!)
        }
    }

    override suspend fun fetchMeals(
        foodProviderId: Int,
        offset: Int
    ): Result<QuerySnapshot> {
        return fetchData(
            MealRequest(
                requestId = "meals/${foodProviderId}/${offset}",
                expireDuration = if (offset == 0)
                    Duration.ofHours(2)
                else
                    Duration.ofHours(12),
                parameters = MealRequestParameters(
                    foodProviderId = foodProviderId,
                    offset = offset
                )
            )
        )
    }


    private fun QueryDocumentSnapshot.toFoodProvider(): FoodProvider {
        this.toObject<FoodProvider>().let {
            it.photo = getImageOfFoodProvider(it.name, it.type, it.location)
            it.openingHours = getOpeningHoursFromDocument(this)
            it.openingHoursString = openingHourUseCases.formatToString(
                it.openingHours,
                LocalDateTime.now()
            )
            it.description = this.getField(FoodProvider.FIELD_DESCRIPTION) ?: ""
            it.liked = sharedPreferenceUseCases.getBoolean(SharedPrefKey.constructFoodProviderKey(it), false)
            return it
        }

    }

    private fun getOpeningHoursFromDocument(document: QueryDocumentSnapshot): Map<DayOfWeek, List<Map<String, LocalTime>>> {
        val result = mutableMapOf<DayOfWeek, List<Map<String, LocalTime>>>()
        for (day in DayOfWeek.values()) {
            val hourArray = document.get(
                "hours_${
                    day.getDisplayName(TextStyle.SHORT_STANDALONE, Locale.ENGLISH).lowercase()
                }"
            ) as List<Any?>?

            val list = mutableListOf<Map<String, LocalTime>>()

            if (hourArray != null) {
                /*
                            0: openingAt
                            1: closingAt
                            2: getAMealTill
                            3: isOpen
                */
                for (i in hourArray.indices step 4) {
                    val constructedOpeningHour = OpeningHour(
                        opensAt = (hourArray[i] ?: "") as String,
                        closesAt = (hourArray[i + 1] ?: "") as String,
                        getFoodTill = (hourArray[i + 2] ?: "") as String,
                        isOpen = (hourArray[i + 3] ?: false) as Boolean,
                        dayOfWeek = day
                    )
                    val tempMap = if (!constructedOpeningHour.isOpen) {
                        emptyMap()
                    } else {
                        mapOf<String, LocalTime>(
                            OpeningHour.FIELD_OPENS_AT to LocalTime.of(
                                constructedOpeningHour.opensAt.substringBefore(".").toInt(),
                                constructedOpeningHour.opensAt.substringAfter(".").toInt()
                            ),
                            OpeningHour.FIELD_GET_FOOD_TILL to LocalTime.of(
                                // Workaround for cafeterias that don't have a "get food till" time
                                (constructedOpeningHour.getFoodTill.ifEmpty { constructedOpeningHour.closesAt })
                                    .substringBefore(".").toInt(),
                                (constructedOpeningHour.getFoodTill.ifEmpty { constructedOpeningHour.closesAt })
                                    .substringAfter(".").toInt()
                            ),
                            OpeningHour.FIELD_CLOSES_AT to LocalTime.of(
                                constructedOpeningHour.closesAt.substringBefore(".").toInt(),
                                constructedOpeningHour.closesAt.substringAfter(".").toInt()
                            )
                        )
                    }
                    list.add(tempMap)
                }
                result[day] = list
            }
        }
        return result
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

    @DrawableRes
    private fun getImageOfFoodProvider(
        name: String?,
        type: String?,
        location: String?
    ): Int {


        val formattedName =
            "${(type ?: "").formatToResString()}_${(name ?: "").formatToResString()}_${(location ?: "").formatToResString()}"
        return drawableMap.getOrDefault(
            formattedName,
            R.drawable.mensateria_campus_hubland_nord_wuerzburg
        )
    }

    companion object {
        val drawableMap = mapOf(
            "burse_am_studentenhaus_wuerzburg" to R.drawable.burse_am_studentenhaus_wuerzburg,
            "interimsmensa_sprachenzentrum_wuerzburg" to R.drawable.interimsmensa_sprachenzentrum_wuerzburg,
            "mensa_am_studentenhaus_wuerzburg" to R.drawable.mensa_am_studentenhaus_wuerzburg,
            "mensa_austrasse_bamberg" to R.drawable.mensa_austrasse_bamberg,
            "mensa_feldkirchenstrasse_bamberg" to R.drawable.mensa_feldkirchenstrasse_bamberg,
            "mensa_thws_campus_schweinfurt" to R.drawable.mensa_thws_campus_schweinfurt,
            "mensa_hochschulcampus_aschaffenburg" to R.drawable.mensa_hochschulcampus_aschaffenburg,
            "mensa_josef_schneider_strasse_wuerzburg" to R.drawable.mensa_josef_schneider_strasse_wuerzburg,
            "mensa_roentgenring_wuerzburg" to R.drawable.mensa_roentgenring_wuerzburg,
            "mensateria_campus_hubland_nord_wuerzburg" to R.drawable.mensateria_campus_hubland_nord_wuerzburg,
            "cafeteria_alte_universitaet_wuerzburg" to R.drawable.cafeteria_alte_universitaet_wuerzburg,
            "cafeteria_alte_weberei_bamberg" to R.drawable.cafeteria_alte_weberei_bamberg,
            "cafeteria_thws_muenzstrasse_wuerzburg" to R.drawable.cafeteria_thws_muenzstrasse_wuerzburg,
            "cafeteria_thws_roentgenring_wuerzburg" to R.drawable.cafeteria_thws_roentgenring_wuerzburg,
            "cafeteria_thws_sanderheinrichsleitenweg_wuerzburg" to R.drawable.cafeteria_thws_sanderheinrichsleitenweg_wuerzburg,
            "cafeteria_ledward_campus_schweinfurt" to R.drawable.cafeteria_ledward_campus_schweinfurt,
            "cafeteria_markusplatz_bamberg" to R.drawable.cafeteria_markusplatz_bamberg_day,
            "cafeteria_neue_universitaet_wuerzburg" to R.drawable.cafeteria_neue_universitaet_wuerzburg,
            "cafeteria_philo_wuerzburg" to R.drawable.cafeteria_philo_wuerzburg,
            "cafeteria_am_studentenhaus_wuerzburg" to R.drawable.mensa_am_studentenhaus_wuerzburg,
            "cafeteria_hochschulcampus_aschaffenburg" to R.drawable.mensa_hochschulcampus_aschaffenburg,
            "cafeteria_feldkirchenstrasse_bamberg" to R.drawable.mensa_feldkirchenstrasse_bamberg,
            "cafeteria_thws_campus_schweinfurt" to R.drawable.mensa_thws_campus_schweinfurt
        )

        const val TAG = "FirestoreRepository"
    }
}