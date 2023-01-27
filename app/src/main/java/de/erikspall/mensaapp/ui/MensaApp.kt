package de.erikspall.mensaapp.ui

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import de.erikspall.mensaapp.ui.theme.ComposeMensaTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MensaApp() {
    ComposeMensaTheme {
        val navController = rememberAnimatedNavController()

        var hideNavBar by rememberSaveable { mutableStateOf(false) }

        val hideNavBarState = remember { MutableTransitionState(!hideNavBar) }

        val currentBackStack by navController.currentBackStackEntryAsState()
        // Fetch your currentDestination:
        val currentDestination = currentBackStack?.destination

        // Change the variable to this and use Overview as a backup screen if this returns null
        val currentScreen =
            bottomNavBarScreens.find { it.route == currentDestination?.route } ?: Canteen

        Scaffold(
            bottomBar = {
                AnimatedVisibility(
                    visibleState = hideNavBarState,
                    exit = slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight }
                    ),
                    enter = slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight }
                    )
                ) {
                    NavigationBar {
                        bottomNavBarScreens.forEach {
                            NavigationBarItem(
                                icon = { Icon(painter = painterResource(id = it.icon), contentDescription = "") },
                                label = { Text(stringResource(id = it.labelId)) },
                                selected = it == currentScreen,
                                onClick = { navController.navigateSingleTopTo(it.route) }
                            )
                        }

                    }
                }

            },
            content = {
                MensaNavHost(
                    navController = navController,
                    onHideNavBar = {
                        hideNavBar = it
                        hideNavBarState.targetState = !it
                    }
                )
            }
        )
    }

}

@Preview(showSystemUi = true)
@Composable
fun PreviewMensaApp() {
    MensaApp()
}