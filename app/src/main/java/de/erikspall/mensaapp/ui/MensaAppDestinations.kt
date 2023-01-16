package de.erikspall.mensaapp.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument
import de.erikspall.mensaapp.R

interface MensaAppDestination {
    val icon: ImageVector
    val route: String
    @get:StringRes
    val labelId: Int
}

object Canteen : MensaAppDestination {
    override val icon = Icons.Rounded.FoodBank
    override val route = "canteen"
    override val labelId = R.string.text_canteens
}

object Cafeteria : MensaAppDestination {
    override val icon = Icons.Rounded.Coffee
    override val route = "cafeteria"
    override val labelId = R.string.text_cafes
}

object Settings: MensaAppDestination {
    override val icon = Icons.Rounded.Settings
    override val route = "settings"
    override val labelId = R.string.text_settings
}

object AdditiveWarningSettings: MensaAppDestination {
    override val icon = Icons.Rounded.Warning
    override val route = "additiveWarning"
    override val labelId = R.string.text_warnings
}

object AboutThisApp: MensaAppDestination {
    override val icon = Icons.Rounded.Info
    override val route = "aboutThisApp"
    override val labelId = R.string.text_about_this_app
}

object FoodProviderDetails: MensaAppDestination {
    override val icon = Icons.Rounded.Details
    override val route = "foodProviderDetails"
    override val labelId = R.string.text_details
    const val foodProviderIdArg = "food_provider_id"
    val routeWithArgs = "${route}/{${foodProviderIdArg}}"
    val arguments = listOf(
        navArgument(foodProviderIdArg) { type = NavType.IntType }
    )
}

val bottomNavBarScreens = listOf(Canteen, Cafeteria, Settings)