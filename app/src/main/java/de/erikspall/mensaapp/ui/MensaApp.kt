package de.erikspall.mensaapp.ui.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import de.erikspall.mensaapp.ui.theme.ComposeMensaTheme
import de.erikspall.mensaapp.ui.theme.Shrikhand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensaApp() {
    ComposeMensaTheme {
        val navController = rememberNavController()
        val currentBackStack by navController.currentBackStackEntryAsState()
        // Fetch your currentDestination:
        val currentDestination = currentBackStack?.destination

        // Change the variable to this and use Overview as a backup screen if this returns null
        val currentScreen = bottomNavBarScreens.find { it.route == currentDestination?.route } ?: Canteen
        val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

        // var selectedItem by remember { mutableStateOf(0) }

        Scaffold(
            topBar = {
                     LargeTopAppBar (
                        title = { Text(text = currentScreen.label, fontFamily = Shrikhand) },
                         scrollBehavior = scrollBehavior
                     )
            },
            bottomBar = {
                NavigationBar {
                    bottomNavBarScreens.forEach {
                        NavigationBarItem(
                            icon = { Icon(it.icon, contentDescription = "") },
                            label = { Text(it.label) },
                            selected = it == currentScreen,
                            onClick = { navController.navigateSingleTopTo(it.route) }
                        )
                    }

                }
            }
        ) { innerPadding ->
            MensaNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
        }
    }

}