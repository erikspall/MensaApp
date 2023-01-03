package de.erikspall.mensaapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.ui.screens.additivefilters.AdditiveFilterScreen
import de.erikspall.mensaapp.ui.screens.details.DetailScreen
import de.erikspall.mensaapp.ui.screens.foodproviders.FoodProvidersScreen
import de.erikspall.mensaapp.ui.screens.settings.SettingsScreen

@Composable
fun MensaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onHideNavBar: (Boolean) -> Unit = {},
    mensaViewModel: MensaViewModel = hiltViewModel()
) {
    NavHost(
        navController = navController,
        startDestination = Canteen.route,
        modifier = modifier
    ) {
        composable(route = Canteen.route) {
            FoodProvidersScreen(
                foodProviderCategory = Category.CANTEEN,
                mensaViewModel = mensaViewModel,
                onFoodProviderClick = {id ->
                    onHideNavBar(true)
                    navController.navigateToDetailScreen(id)
                },
            )
        }
        composable(route = Cafeteria.route) {
            FoodProvidersScreen(
                foodProviderCategory = Category.CAFETERIA,
                mensaViewModel = mensaViewModel,
                onFoodProviderClick = {id ->
                    onHideNavBar(true)
                    navController.navigateToDetailScreen(id)
                },
            )
        }
        composable(route = Settings.route) {
            SettingsScreen(
                mensaViewModel = mensaViewModel,
                onWarningsClicked = {
                    mensaViewModel.getAdditives()
                    onHideNavBar(true)
                    navController.navigate(AdditiveWarningSettings.route)
                }
            )
        }
        composable(route = AdditiveWarningSettings.route) {
            AdditiveFilterScreen(
                mensaViewModel = mensaViewModel,
                onBackClicked = {
                    onHideNavBar(false)
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = FoodProviderDetails.routeWithArgs,
            arguments = FoodProviderDetails.arguments
        ) { navBackStackEntry ->
            val foodProviderId =
                navBackStackEntry.arguments?.getInt(FoodProviderDetails.foodProviderIdArg)
            DetailScreen(
                foodProvider = mensaViewModel.foodProviders.find { foodProvider -> foodProvider.id == foodProviderId }
                ?: FoodProvider(
                    name = stringResource(
                        R.string.text_invalid
                    )
                ),
                onBackClicked = {
                    onHideNavBar(false)
                    navController.popBackStack()
                }
            )
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

private fun NavHostController.navigateToDetailScreen(foodProviderId: Int) {
    this.navigate("${FoodProviderDetails.route}/$foodProviderId")
}