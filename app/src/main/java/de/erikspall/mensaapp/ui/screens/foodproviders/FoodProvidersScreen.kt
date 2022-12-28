package de.erikspall.mensaapp.ui.screens.foodproviders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.hilt.navigation.compose.hiltViewModel
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.ui.MensaAppViewModel
import de.erikspall.mensaapp.ui.components.FoodProvidersList
import de.erikspall.mensaapp.ui.screens.foodproviders.events.FoodProviderScreenEvent
import de.erikspall.mensaapp.ui.theme.Shrikhand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodProvidersScreen(
    modifier: Modifier = Modifier,
    onFoodProviderClick: (String) -> Unit = {},
    foodProviderCategory: Category,
    mensaAppViewModel: MensaAppViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar (
                title = { Text(text = foodProviderCategory.getValue(), fontFamily = Shrikhand, style = MaterialTheme.typography.headlineMedium) },
                scrollBehavior = scrollBehavior,
            )
        },
        content = { innerPadding ->
            if (mensaAppViewModel.foodProviders.isEmpty()) {
                mensaAppViewModel.onEvent(FoodProviderScreenEvent.Init) // TODO: Sus
            } else {
                FoodProvidersList(
                    modifier = modifier.padding(
                        start = innerPadding.calculateStartPadding(layoutDirection),
                        end = innerPadding.calculateEndPadding(layoutDirection),
                        top = innerPadding.calculateTopPadding()
                    ),
                    list = when (foodProviderCategory) {
                        Category.CANTEEN -> mensaAppViewModel.canteens
                        Category.CAFETERIA -> mensaAppViewModel.cafeterias
                        else -> mensaAppViewModel.foodProviders
                    }
                )
            }
        }
    )


}