package de.erikspall.mensaapp.ui.screens.foodproviders

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.ui.components.FoodProvidersList
import de.erikspall.mensaapp.ui.MensaViewModel
import de.erikspall.mensaapp.ui.theme.Shrikhand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodProvidersScreen(
    modifier: Modifier = Modifier,
    onFoodProviderClick: (Int) -> Unit = {},
    foodProviderCategory: Category,
    mensaViewModel: MensaViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val layoutDirection = LocalLayoutDirection.current

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = when (foodProviderCategory) {
                            Category.CANTEEN -> stringResource(id = R.string.text_canteens)
                            Category.CAFETERIA -> stringResource(id = R.string.text_cafes)
                            else -> stringResource(id = R.string.text_invalid)
                        }, fontFamily = Shrikhand, style = MaterialTheme.typography.headlineMedium
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        content = { innerPadding ->

            if (mensaViewModel.foodProviders.isEmpty()) {
                 // TODO: Show Lottie
            } else {
                FoodProvidersList(
                    modifier = modifier.padding(
                        innerPadding
                    ).padding(bottom = MaterialSizes.BOTTOM_NAV_HEIGHT.dp),
                    list = when (foodProviderCategory) {
                        Category.CANTEEN -> mensaViewModel.canteens
                        Category.CAFETERIA -> mensaViewModel.cafeterias
                        else -> mensaViewModel.foodProviders
                    },
                    onClickedFoodProvider = {clickedProvider ->
                        onFoodProviderClick(clickedProvider.id ?: -1)
                    }
                )
            }
        }
    )


}