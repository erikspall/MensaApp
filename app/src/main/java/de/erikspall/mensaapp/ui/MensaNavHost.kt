package de.erikspall.mensaapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.AnimatedNavHost
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.ui.screens.about.AboutScreen
import de.erikspall.mensaapp.ui.screens.additivefilters.AdditiveFilterScreen
import de.erikspall.mensaapp.ui.screens.details.DetailScreen
import de.erikspall.mensaapp.ui.screens.foodproviders.FoodProvidersScreen
import de.erikspall.mensaapp.ui.screens.settings.SettingsScreen
import de.erikspall.mensaapp.ui.theme.MensaAnimation

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MensaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onHideNavBar: (Boolean) -> Unit = {},
    mensaViewModel: MensaViewModel = hiltViewModel()
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = Canteen.route,
        modifier = modifier
    ) {
        composable(
            route = Canteen.route,
            enterTransition = {
                fadeIn(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleIn(
                    animationSpec = tween(MensaAnimation.MEDIUM),
                    initialScale = 0.9f
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleOut(
                    animationSpec = tween(MensaAnimation.MEDIUM),
                    targetScale = 0.9f
                )
            }
        ) {
            FoodProvidersScreen(
                foodProviderCategory = Category.CANTEEN,
                mensaViewModel = mensaViewModel,
                onFoodProviderClick = { id ->
                    onHideNavBar(true)
                    navController.navigateToDetailScreen(id)
                },
            )
        }
        composable(
            route = Cafeteria.route,
            enterTransition = {
                fadeIn(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleIn(
                    animationSpec = tween(MensaAnimation.MEDIUM),
                    initialScale = 0.9f
                )
            },
            exitTransition = {
                fadeOut(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleOut(
                    animationSpec = tween(MensaAnimation.MEDIUM),
                    targetScale = 0.9f
                )
            }
        ) {
            FoodProvidersScreen(
                foodProviderCategory = Category.CAFETERIA,
                mensaViewModel = mensaViewModel,
                onFoodProviderClick = { id ->
                    onHideNavBar(true)
                    navController.navigateToDetailScreen(id)
                },
            )
        }
        composable(
            route = Settings.route,
            enterTransition = {
                when(this.initialState.destination.route) {
                    AdditiveWarningSettings.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.End,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 1.5f)
                    ) + fadeIn(animationSpec = tween(MensaAnimation.LONG))
                    AboutThisApp.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.End,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 1.5f)
                    ) + fadeIn(animationSpec = tween(MensaAnimation.LONG))
                    else -> fadeIn(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleIn(
                        animationSpec = tween(MensaAnimation.MEDIUM),
                        initialScale = 0.9f
                    )
                }

            },
            exitTransition = {
                when (this.targetState.destination.route) {
                    AdditiveWarningSettings.route -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Start,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 1.5f)
                    ) + fadeOut(animationSpec = tween(MensaAnimation.SHORT))
                    AboutThisApp.route -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Start,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 1.5f)
                    ) + fadeOut(animationSpec = tween(MensaAnimation.SHORT))
                    else -> fadeOut(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleOut(
                        animationSpec = tween(MensaAnimation.MEDIUM),
                        targetScale = 0.9f
                    )
                }

            }
        ) {
            SettingsScreen(
                mensaViewModel = mensaViewModel,
                onWarningsClicked = {
                    mensaViewModel.getAdditives()
                    onHideNavBar(true)
                    navController.navigate(AdditiveWarningSettings.route)
                },
                onAboutClicked = {
                    onHideNavBar(true)
                    navController.navigate(AboutThisApp.route)
                }
            )
        }
        composable(
            route = AdditiveWarningSettings.route,
            enterTransition = {
                when (this.initialState.destination.route) {
                    Settings.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Start,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 1.5f)
                    ) + fadeIn(animationSpec = tween(MensaAnimation.LONG))
                    else -> fadeIn(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleIn(
                        animationSpec = tween(MensaAnimation.MEDIUM),
                        initialScale = 0.9f
                    )
                }
            },
            exitTransition = {
                when (this.targetState.destination.route) {
                    Settings.route -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.End,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium)
                    ) + fadeOut(animationSpec = tween(MensaAnimation.SHORT))
                    else -> fadeOut(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleOut(
                        animationSpec = tween(MensaAnimation.MEDIUM),
                        targetScale = 0.9f
                    )
                }
            }
        ) {
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
            arguments = FoodProviderDetails.arguments,
            enterTransition = {
                when (this.initialState.destination.route) {
                    Canteen.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Up,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 1.5f)
                    ) + fadeIn(animationSpec = tween(MensaAnimation.LONG))
                    Cafeteria.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Up,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 1.5f)
                    ) + fadeIn(animationSpec = tween(MensaAnimation.LONG))
                    else -> fadeIn(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleIn(
                        animationSpec = tween(MensaAnimation.MEDIUM),
                        initialScale = 0.9f
                    )
                }
            },
            exitTransition = {
                when (this.targetState.destination.route) {
                    Canteen.route -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Down,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 5f)
                    ) + fadeOut(animationSpec = tween(MensaAnimation.LONG))
                    Cafeteria.route -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.Down,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 5f)
                    ) + fadeOut(animationSpec = tween(MensaAnimation.LONG))
                    else -> fadeOut(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleOut(
                        animationSpec = tween(MensaAnimation.MEDIUM),
                        targetScale = 0.9f
                    )
                }
            }
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
                },
                mensaViewModel = mensaViewModel
            )
        }
        composable(
            route = AboutThisApp.route,
            enterTransition = {
                when (this.initialState.destination.route) {
                    Settings.route -> slideIntoContainer(
                        AnimatedContentScope.SlideDirection.Start,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 1.5f)
                    ) + fadeIn(animationSpec = tween(MensaAnimation.LONG))
                    else -> fadeIn(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleIn(
                        animationSpec = tween(MensaAnimation.MEDIUM),
                        initialScale = 0.9f
                    )
                }
            },
            exitTransition = {
                when (this.targetState.destination.route) {
                    Settings.route -> slideOutOfContainer(
                        AnimatedContentScope.SlideDirection.End,
                        animationSpec = spring(stiffness = Spring.StiffnessMedium, dampingRatio = 1.5f)
                    ) + fadeOut(animationSpec = tween(MensaAnimation.SHORT))
                    else -> fadeOut(animationSpec = tween(MensaAnimation.MEDIUM)) + scaleOut(
                        animationSpec = tween(MensaAnimation.MEDIUM),
                        targetScale = 0.9f
                    )
                }

            }
        ) {
            AboutScreen(
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