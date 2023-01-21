package de.erikspall.mensaapp.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation.NavType
import androidx.navigation.navArgument
import de.erikspall.mensaapp.R

interface MensaAppDestination {
    @get:DrawableRes
    val icon: Int
    val route: String
    @get:StringRes
    val labelId: Int
}

object Canteen : MensaAppDestination {
    override val icon = R.drawable.food_bank
    override val route = "canteen"
    override val labelId = R.string.text_canteens
}

object Cafeteria : MensaAppDestination {
    override val icon = R.drawable.coffee
    override val route = "cafeteria"
    override val labelId = R.string.text_cafes
}

object Settings: MensaAppDestination {
    override val icon = R.drawable.settings
    override val route = "settings"
    override val labelId = R.string.text_settings
}

object AdditiveWarningSettings: MensaAppDestination {
    override val icon = R.drawable.warning
    override val route = "additiveWarning"
    override val labelId = R.string.text_warnings
}

object AboutThisApp: MensaAppDestination {
    override val icon = R.drawable.info
    override val route = "aboutThisApp"
    override val labelId = R.string.text_about_this_app
}

object FoodProviderDetails: MensaAppDestination {
    override val icon = R.drawable.details
    override val route = "foodProviderDetails"
    override val labelId = R.string.text_details
    const val foodProviderIdArg = "food_provider_id"
    val routeWithArgs = "${route}/{${foodProviderIdArg}}"
    val arguments = listOf(
        navArgument(foodProviderIdArg) { type = NavType.IntType }
    )
}

val bottomNavBarScreens = listOf(Canteen, Cafeteria, Settings)