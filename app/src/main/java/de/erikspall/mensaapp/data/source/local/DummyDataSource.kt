package de.erikspall.mensaapp.data.source.local

import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.domain.model.OpeningHours
import de.erikspall.mensaapp.domain.model.enums.FoodProviderType
import de.erikspall.mensaapp.domain.model.enums.Location
import de.erikspall.mensaapp.domain.model.interfaces.Food
import de.erikspall.mensaapp.domain.model.interfaces.FoodProvider
import de.erikspall.mensaapp.domain.model.interfaces.Menu
import de.erikspall.mensaapp.domain.model.interfaces.OpeningInfo
import java.time.DayOfWeek
import java.time.LocalDate

object DummyDataSource {
    val food: Set<Food> = setOf(
        Food.createMeal(
            name = "Köttbullar mit Rahmsoße",
            priceStudent = 280,
            priceGuest = 510,
            priceEmployee = 415
        ),
        Food.createMeal(
            name = "Super duper Cheeseburger mit Pommes etc.",
            priceStudent = 445,
            priceGuest = 685,
            priceEmployee = 590
        ),
        Food.createMeal(
            name = "Mensa-Vital: Gemüse Couscous-Pfanne mit Joghurt-Ingwer-Dip",
            priceStudent = 235,
            priceGuest = 460,
            priceEmployee = 365
        )
    )



    val menus: List<Menu> = listOf(
        Menu.createMenuOfDay(
            date = LocalDate.of(2022, 8, 23),
            food = food
        ),
        Menu.createMenuOfDay(
            date = LocalDate.of(2022, 8, 24),
            food = food
        ),
        Menu.createMenuOfDay(
            date = LocalDate.of(2022, 8, 25),
            food = food
        )
    )

    val openingHours: List<OpeningInfo> = listOf(
        OpeningInfo.createOpeningHours(
            dayOfWeek = DayOfWeek.MONDAY,
            isOpen = true,
            openingAt = "11:00",
            closingAt = "14:00",
            getAMealTill = "13:30",
        ),
        OpeningInfo.createOpeningHours(
            dayOfWeek = DayOfWeek.TUESDAY,
            isOpen = true,
            openingAt = "11:00",
            closingAt = "14:00",
            getAMealTill = "13:30",
        ),
        OpeningInfo.createOpeningHours(
            dayOfWeek = DayOfWeek.WEDNESDAY,
            isOpen = true,
            openingAt = "11:00",
            closingAt = "14:00",
            getAMealTill = "13:30",
        ),
        OpeningInfo.createOpeningHours(
            dayOfWeek = DayOfWeek.THURSDAY,
            isOpen = true,
            openingAt = "11:00",
            closingAt = "14:00",
            getAMealTill = "13:30",
        ),
        OpeningInfo.createOpeningHours(
            dayOfWeek = DayOfWeek.FRIDAY,
            isOpen = true,
            openingAt = "11:00",
            closingAt = "14:00",
            getAMealTill = "13:30",
        ),
        OpeningInfo.createOpeningHours(
            dayOfWeek = DayOfWeek.SATURDAY,
            isOpen = false,
            openingAt = "",
            closingAt = "",
            getAMealTill = "",
        ),
        OpeningInfo.createOpeningHours(
            dayOfWeek = DayOfWeek.SUNDAY,
            isOpen = false,
            openingAt = "",
            closingAt = "",
            getAMealTill = "",
        )
    )

    val canteens: List<FoodProvider> = listOf(
        FoodProvider.createCanteen(
            name = "Campus Hubland Nord",
            location = Location.WUERZBURG,
            titleInfo = "",
            openingHours = openingHours,
            additionalInfo = "",
            menus = menus,
            imageResourceId = R.drawable.m1,
            type = FoodProviderType.MENSATERIA
        ),
        FoodProvider.createCanteen(
            name = "Studentenhaus Würzburg",
            location = Location.WUERZBURG,
            titleInfo = "",
            openingHours = openingHours,
            additionalInfo = "",
            menus = menus,
            imageResourceId = R.drawable.m2,
            type = FoodProviderType.BURSE
        ),
        FoodProvider.createCanteen(
            name = "Josef-Schneider-Straße Würzburg",
            location = Location.WUERZBURG,
            titleInfo = "",
            openingHours = openingHours,
            additionalInfo = "",
            menus = menus,
            imageResourceId = R.drawable.m3,
            type = FoodProviderType.MENSA
        ),
        FoodProvider.createCanteen(
            name = "Studentenhaus Würzburg",
            location = Location.WUERZBURG,
            titleInfo = "",
            openingHours = openingHours,
            additionalInfo = "",
            menus = menus,
            imageResourceId = R.drawable.m4,
            type = FoodProviderType.MENSA
        )
    )
}