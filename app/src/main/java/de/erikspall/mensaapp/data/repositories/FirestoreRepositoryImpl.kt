package de.erikspall.mensaapp.data.repositories

import android.util.Log
import androidx.annotation.DrawableRes
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.errorhandling.OptionalResult
import de.erikspall.mensaapp.data.repositories.interfaces.FirestoreRepository
import de.erikspall.mensaapp.data.sources.remote.firestore.FirestoreDataSource
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.*
import de.erikspall.mensaapp.domain.usecases.openinghours.OpeningHourUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.domain.utils.Extensions.toDate
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.TextStyle
import java.util.*

class FirestoreRepositoryImpl(
    private val firestoreDataSource: FirestoreDataSource,
    private val openingHourUseCases: OpeningHourUseCases,
    private val sharedPreferenceUseCases: SharedPreferenceUseCases
) : FirestoreRepository {
    override suspend fun fetchFoodProviders(
        location: Location,
        category: Category
    ): OptionalResult<List<FoodProvider>> {
        Log.d("$TAG:fetchingProcess", "Entered Repository")
        val lastUpdate = sharedPreferenceUseCases.getLocalDateTime(
            R.string.shared_pref_last_food_provider_update,
            LocalDateTime.now().minusDays(8)
        )

        val duration = Duration.between(lastUpdate, LocalDateTime.now()).toDays()

        Log.d("$TAG:fetchingProcess", "Last update was $duration days ago")

        var foodProviderSnapshot = if (duration >= 7) {
            firestoreDataSource.fetchFoodProviders(
                location = location,
                category = category,
                source = Source.SERVER
            )
        } else
            firestoreDataSource.fetchFoodProviders(
                location = location,
                category = category,
                source = Source.CACHE
            )


        if (foodProviderSnapshot.isEmpty) {
            foodProviderSnapshot = if (duration >= 7) {
                firestoreDataSource.fetchFoodProviders(
                    location = location,
                    category = category,
                    source = Source.CACHE
                )
            } else {
                firestoreDataSource.fetchFoodProviders(
                    location = location,
                    category = category,
                    source = Source.SERVER
                )
            }
        }


        val foodProviderList = mutableListOf<FoodProvider>()

        return if (foodProviderSnapshot.isPresent) {
            sharedPreferenceUseCases.setLocalDateTime(
                R.string.shared_pref_last_food_provider_update,
                LocalDateTime.now()
            )
            Log.d("$TAG:fetchingProcess", "Snapshot contains ${foodProviderSnapshot.get().size()}")
            for (document in foodProviderSnapshot.get())
                document.toFoodProvider().let {
                    foodProviderList.add(it)
                }

            OptionalResult.of(foodProviderList)
        } else {
            Log.e("$TAG:fetchingProcess", foodProviderSnapshot.getMessage())
            OptionalResult.ofMsg(foodProviderSnapshot.getMessage())
        }

    }

    override suspend fun fetchFoodProvider(
        foodProviderId: Int
    ): OptionalResult<FoodProvider> {
        val lastUpdate = sharedPreferenceUseCases.getLocalDateTime(
            R.string.shared_pref_last_food_provider_update,
            LocalDateTime.now().minusDays(8),
            /* foodProviderId -- not needed for now*/
        )

        val duration = Duration.between(lastUpdate, LocalDateTime.now()).toDays()

        var foodProviderSnapshot = if (duration >= 7)
            firestoreDataSource.fetchFoodProvider(
                source = Source.SERVER,
                foodProviderId = foodProviderId
            )
        else
            firestoreDataSource.fetchFoodProvider(
                source = Source.CACHE,
                foodProviderId = foodProviderId
            )

        if (foodProviderSnapshot.isEmpty) {
            foodProviderSnapshot = if (duration >= 7)
                firestoreDataSource.fetchFoodProvider(
                    source = Source.CACHE,
                    foodProviderId = foodProviderId
                )
            else
                firestoreDataSource.fetchFoodProvider(
                    source = Source.SERVER,
                    foodProviderId = foodProviderId
                )
        }

        return if (foodProviderSnapshot.isPresent) {
            sharedPreferenceUseCases.setLocalDateTime(
                R.string.shared_pref_last_food_provider_update,
                LocalDateTime.now(),
                foodProviderId
            )
            OptionalResult.of(foodProviderSnapshot.get().first().toFoodProvider())
        } else
            OptionalResult.ofMsg(foodProviderSnapshot.getMessage())
    }

    override suspend fun fetchAdditives(
    ): OptionalResult<List<Additive>> {
        val lastUpdate = sharedPreferenceUseCases.getLocalDateTime(
            R.string.shared_pref_last_additive_update,
            LocalDateTime.now().minusDays(30),
        )

        val duration = Duration.between(lastUpdate, LocalDateTime.now()).toDays()

        var additiveSnapshot = if (duration >= 30) {
            firestoreDataSource.fetchAdditives(source = Source.SERVER)
        } else {
            firestoreDataSource.fetchAdditives(source = Source.CACHE)
        }

        if (additiveSnapshot.isEmpty) {
            additiveSnapshot = if (duration >= 30) {
                firestoreDataSource.fetchAdditives(source = Source.CACHE)
            } else {
                firestoreDataSource.fetchAdditives(source = Source.SERVER)
            }
        }

        val additiveList = mutableListOf<Additive>()

        return if (additiveSnapshot.isPresent) {
            sharedPreferenceUseCases.setLocalDateTime(
                R.string.shared_pref_last_additive_update,
                LocalDateTime.now()
            )
            for (document in additiveSnapshot.get()) {
                additiveList.add(
                    Additive(
                        name = document.get(Additive.FIELD_NAME) as String,
                        type = AdditiveType.from(document.get(Additive.FIELD_TYPE) as String)
                            ?: AdditiveType.ALLERGEN
                    )
                )
            }

            OptionalResult.of(additiveList)
        } else {
            return OptionalResult.ofMsg(additiveSnapshot.getMessage())
        }
    }

    override suspend fun fetchMeals(
        foodProviderId: Int,
        date: LocalDate
    ): OptionalResult<QuerySnapshot> {
        val lastUpdate = sharedPreferenceUseCases.getLocalDateTime(
            R.string.shared_pref_last_menu_update,
            LocalDateTime.now().minusHours(2),
            foodProviderId
        )
        // TODO: Allow updates after cloud function
        val duration = Duration.between(lastUpdate, LocalDateTime.now()).toHours()

        var mealsSnapshot = if (duration >= 1)
            firestoreDataSource.fetchMeals(
                foodProviderId = foodProviderId,
                date = date.toDate(),
                source = Source.SERVER
            )
        else
            firestoreDataSource.fetchMeals(
                foodProviderId = foodProviderId,
                date = date.toDate(),
                source = Source.CACHE
            )

        if (mealsSnapshot.isEmpty) {
            mealsSnapshot = if (duration >= 1)
                firestoreDataSource.fetchMeals(
                    foodProviderId = foodProviderId,
                    date = date.toDate(),
                    source = Source.CACHE
                )
            else
                firestoreDataSource.fetchMeals(
                    foodProviderId = foodProviderId,
                    date = date.toDate(),
                    source = Source.SERVER
                )
        }

        sharedPreferenceUseCases.setLocalDateTime(
            R.string.shared_pref_last_menu_update,
            LocalDateTime.now(),
            foodProviderId
        )

        return if (mealsSnapshot.isEmpty || (mealsSnapshot.isPresent && mealsSnapshot.get().isEmpty))
            OptionalResult.ofMsg("no menus")
        else
            mealsSnapshot
    }

    private fun QueryDocumentSnapshot.toFoodProvider(): FoodProvider {
        this.toObject<FoodProvider>().let {
            it.photo = getImageOfFoodProvider(it.name, it.type, it.location)
            it.openingHours = getOpeningHoursFromDocument(this)
            it.openingHoursString = openingHourUseCases.formatToString(
                it.openingHours,
                LocalDateTime.now(),
                Locale.getDefault()
            )
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
                                constructedOpeningHour.getFoodTill.substringBefore(".").toInt(),
                                constructedOpeningHour.getFoodTill.substringAfter(".").toInt()
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

        const val TAG = "FirestoreRepository"
    }
}