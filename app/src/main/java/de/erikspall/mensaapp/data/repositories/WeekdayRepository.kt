package de.erikspall.mensaapp.data.repositories

import de.erikspall.mensaapp.data.sources.local.database.daos.WeekdayDao
import de.erikspall.mensaapp.data.sources.local.database.entities.Weekday

class WeekdayRepository(
    private val weekdayDao: WeekdayDao
) {
    suspend fun insert(vararg weekdays: Weekday) {
        weekdayDao.insertAll(*weekdays)
    }

    suspend fun insert(weekday: Weekday): Long {
        return weekdayDao.insert(weekday)
    }

    suspend fun exists(name: String): Boolean {
        return weekdayDao.exists(name)
    }

    suspend fun delete(weekday: Weekday) {
        weekdayDao.delete(weekday)
    }

    suspend fun get(name: String): Weekday? {
        return weekdayDao.get(name)
    }

    suspend fun update(vararg weekdays: Weekday) {
        weekdayDao.updateWeekday(*weekdays)
    }
}