package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProvider
import de.erikspall.mensaapp.data.sources.local.database.entities.Location
import de.erikspall.mensaapp.data.sources.local.database.entities.OpeningHours
import de.erikspall.mensaapp.data.sources.local.database.entities.Weekday
import de.erikspall.mensaapp.data.sources.local.database.relationships.FoodProviderWithoutMenus
import de.erikspall.mensaapp.data.sources.remote.RemoteApiDataSource
import de.erikspall.mensaapp.data.sources.remote.api.model.FoodProviderApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.LocationApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.OpeningInfoApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.WeekdayApiModel
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val foodProviderRepository: FoodProviderRepository,
    private val locationRepository: LocationRepository,
    private val openingHoursRepository: OpeningHoursRepository,
    private val weekdayRepository: WeekdayRepository,
    private val remoteApiDataSource: RemoteApiDataSource
) {

    val allFoodProvidersWithoutMenus: Flow<List<FoodProviderWithoutMenus>> =
        foodProviderRepository.getFoodProvidersWithoutMenus()

    /**
     * Fetches and saves all new data (including weekdays, menus, etc.)
     */
    suspend fun fetchAndSaveLatestData() {
        val fetched = remoteApiDataSource.fetchLatestData()

        if (fetched.isPresent){
            for (fetchedProvider in fetched.get()) {
                val fid = getOrInsertFoodProvider(fetchedProvider)
                for (openingHours in fetchedProvider.openingHours) {
                    getOrInsertOpeningHours(openingHours, fid)
                }
            }
        }

    }

    private suspend fun getOrInsertOpeningHours(apiOpeningHours: OpeningInfoApiModel, fid: Long): Long {
        return if (openingHoursRepository.exists(apiOpeningHours.id)) {
            apiOpeningHours.id
        } else {
            openingHoursRepository.insert(OpeningHours(
                oid = apiOpeningHours.id,
                foodProviderId = fid,
                weekdayId = getOrInsertWeekday(apiOpeningHours.weekday),
                opensAt = apiOpeningHours.opensAt,
                closesAt = apiOpeningHours.closesAt,
                getFoodTill = apiOpeningHours.getFoodTill,
                opened = apiOpeningHours.opened
            )
            )
        }
    }

    private suspend fun getOrInsertWeekday(apiWeekday: WeekdayApiModel): Long {
        return if (weekdayRepository.exists(apiWeekday.id)) {
            apiWeekday.id
        } else {
            weekdayRepository.insert(
                Weekday(
                wid = apiWeekday.id,
                name = apiWeekday.name
            )
            )
        }
    }

    private suspend fun getOrInsertFoodProvider(apiFoodProvider: FoodProviderApiModel): Long {
        return if (foodProviderRepository.exists(apiFoodProvider.id)) {
            apiFoodProvider.id
        } else {
            foodProviderRepository.insert(
                FoodProvider(
                    fid = apiFoodProvider.id,
                    name = apiFoodProvider.name,
                    locationId = getOrInsertLocation(apiFoodProvider.location),
                    info = apiFoodProvider.info,
                    additionalInfo = apiFoodProvider.additionalInfo
                )
            )
        }
    }

    private suspend fun getOrInsertLocation(apiLocation: LocationApiModel): Long {
        return if (locationRepository.exists(apiLocation.id)) {
            apiLocation.id
        } else {
            locationRepository.insert(Location(
                lid = apiLocation.id,
                name = apiLocation.name
            ))
        }
    }
}