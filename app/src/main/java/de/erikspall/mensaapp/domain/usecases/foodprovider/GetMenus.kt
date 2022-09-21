package de.erikspall.mensaapp.domain.usecases.foodprovider

import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.Menu
import java.util.*

class GetMenus(
    private val repository: AppRepository
) {
     suspend operator fun invoke(cid: Long): Optional<List<Menu>> {
        return repository.fetchLatestMenuOfCanteen(cid)
    }
}