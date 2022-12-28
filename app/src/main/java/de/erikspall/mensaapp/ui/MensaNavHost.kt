package de.erikspall.mensaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.ui.screens.foodproviders.FoodProvidersScreen
import de.erikspall.mensaapp.ui.screens.foodproviders.FoodProviderViewModel
import de.erikspall.mensaapp.ui.screens.settings.SettingsScreen

@Composable
fun MensaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    foodProviderViewModel: FoodProviderViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Canteen.route,
        modifier = modifier
    ) {
        composable(route = Canteen.route) {
            FoodProvidersScreen(foodProviderCategory = Category.CANTEEN, foodProviderViewModel = foodProviderViewModel)
        }
        composable(route = Cafeteria.route) {
            FoodProvidersScreen(foodProviderCategory = Category.CAFETERIA, foodProviderViewModel = foodProviderViewModel)
        }
        composable(route = Settings.route) {
            SettingsScreen()
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) {
        popUpTo(
            this@navigateSingleTopTo.graph.findStartDestination().id
        ) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }