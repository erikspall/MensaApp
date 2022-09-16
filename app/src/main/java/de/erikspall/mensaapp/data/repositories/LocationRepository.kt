package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.LocationDao
import de.erikspall.mensaapp.data.sources.local.database.entities.Location

class LocationRepository(
    private val locationDao: LocationDao
) {
    suspend fun insert(vararg locations: Location) {
        locationDao.insertAll(*locations)
    }

    suspend fun insert(location: Location): Long {
        return locationDao.insert(location)
    }

    suspend fun exists(lid: Long): Boolean {
        return locationDao.exists(lid)
    }

    suspend fun get(lid: Int): Location? {
        return locationDao.get(lid)
    }

    suspend fun delete(location: Location) {
        locationDao.delete(location)
    }

    suspend fun update(vararg locations: Location) {
        locationDao.updateLocations(*locations)
    }

}