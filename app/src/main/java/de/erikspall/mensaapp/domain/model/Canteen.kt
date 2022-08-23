package de.erikspall.mensaapp.domain.model

import androidx.annotation.DrawableRes
import de.erikspall.mensaapp.domain.model.enums.FoodProviderType
import de.erikspall.mensaapp.domain.model.enums.Location
import de.erikspall.mensaapp.domain.model.interfaces.FoodProvider
import de.erikspall.mensaapp.domain.model.interfaces.Food
import de.erikspall.mensaapp.domain.model.interfaces.Menu
import de.erikspall.mensaapp.domain.model.interfaces.OpeningInfo
import java.util.*

class Canteen(
    private val name: String,
    private val location: Location,
    private val titleInfo: String,
    private val openingHours: List<OpeningInfo>,
    private val additionalInfo: String,
    private val menus: List<Menu>,
    @DrawableRes private val imageResourceId: Int,
    private val type: FoodProviderType
) : FoodProvider {

    @DrawableRes
    override fun getImageResourceId(): Int {
        return imageResourceId
    }

    override fun getType(): FoodProviderType {
        return type
    }


    override fun getName(): String {
        return name
    }

    override fun getLocation(): Location {
        return location
    }

    override fun getTitleInfo(): String {
        return titleInfo
    }

    override fun getOpeningHours(): List<OpeningInfo> {
        return openingHours
    }

    override fun getAdditionalInfo(): String {
        return additionalInfo
    }

    override fun getMenus(): List<Menu> {
        return menus
    }

    override fun getMenuOfDay(): Optional<Menu> {
        TODO("Not yet implemented")
    }
}