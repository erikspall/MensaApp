package de.erikspall.mensaapp.ui

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.ui.theme.ComposeMensaTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensaApp() {
    ComposeMensaTheme {
        val navController = rememberNavController()

        // TODO: On first composition navBarHeight is 0
        // Possible work around: Hide Navbar when animation finished
        val navBarHeight = with(LocalDensity.current) {
            WindowInsets.navigationBars.getBottom(this)
        }

        var hideNavBar by rememberSaveable { mutableStateOf(false) }
        val bottomNavOffsetY = remember {
            if (hideNavBar)
                Animatable(0f)
            else
                Animatable(MaterialSizes.BOTTOM_NAV_HEIGHT.toFloat() + navBarHeight)
        }

        if (hideNavBar) {
            LaunchedEffect(Unit) {
                bottomNavOffsetY.animateTo(
                    MaterialSizes.BOTTOM_NAV_HEIGHT.toFloat() + navBarHeight,
                    tween(200)
                )
            }
        } else {
            LaunchedEffect(Unit) {
                bottomNavOffsetY.animateTo(0f, tween(200))
            }
        }

        val currentBackStack by navController.currentBackStackEntryAsState()
        // Fetch your currentDestination:
        val currentDestination = currentBackStack?.destination

        // Change the variable to this and use Overview as a backup screen if this returns null
        val currentScreen =
            bottomNavBarScreens.find { it.route == currentDestination?.route } ?: Canteen

        Scaffold(
            bottomBar = {

                NavigationBar(
                    modifier = Modifier
                        .offset {
                            IntOffset(
                                x = 0,
                                y = bottomNavOffsetY.value.toInt().dp.roundToPx()
                            )
                        }
                ) {
                    bottomNavBarScreens.forEach {
                        NavigationBarItem(
                            icon = { Icon(it.icon, contentDescription = "") },
                            label = { Text(stringResource(id = it.labelId)) },
                            selected = it == currentScreen,
                            onClick = { navController.navigateSingleTopTo(it.route) }
                        )
                    }

                }
            },
            content = { innerPadding ->
                MensaNavHost(
                    navController = navController,
                    onHideNavBar = {
                        hideNavBar = it
                    },
                    modifier = Modifier
                        .padding(
                            bottom = innerPadding.calculateBottomPadding()
                        )
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