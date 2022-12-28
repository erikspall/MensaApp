package de.erikspall.mensaapp.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.erikspall.mensaapp.ui.screens.foodproviders.FoodProvidersScreen

@Composable
fun MensaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Canteen.route,
        modifier = Modifier
    ) {
        composable(route = Canteen.route) {
            FoodProvidersScreen(text = "Page 1")
        }
        composable(route = Cafeteria.route) {
            FoodProvidersScreen(text = "Page 2")
        }
        composable(route = Settings.route) {
            FoodProvidersScreen(text = "Page 3")
        }
    }
}

fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }