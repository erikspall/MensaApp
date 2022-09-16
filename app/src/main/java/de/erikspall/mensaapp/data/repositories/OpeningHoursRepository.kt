package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.OpeningHoursDao
import de.erikspall.mensaapp.data.sources.local.database.entities.OpeningHours

class OpeningHoursRepository(
    private val openingHoursDao: OpeningHoursDao
) {
    suspend fun insert(vararg openingHours: OpeningHours) {
        openingHoursDao.insertAll(*openingHours)
    }

    suspend fun insert(location: OpeningHours): Long {
        return openingHoursDao.insert(location)
    }

    suspend fun exists(oid: Long): Boolean {
        return openingHoursDao.exists(oid)
    }

    suspend fun delete(location: OpeningHours) {
        openingHoursDao.delete(location)
    }

    suspend fun update(vararg locations: OpeningHours) {
        openingHoursDao.updateOpeningHours(*locations)
    }
}