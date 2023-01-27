package de.erikspall.mensaapp.ui.screens.foodproviders

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.ui.components.FoodProvidersList
import de.erikspall.mensaapp.ui.MensaViewModel
import de.erikspall.mensaapp.ui.components.LottieWithInfo
import de.erikspall.mensaapp.ui.state.UiState
import de.erikspall.mensaapp.ui.theme.Shrikhand
import de.erikspall.mensaapp.ui.utils.ClockBroadcastReceiver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodProvidersScreen(
    modifier: Modifier = Modifier,
    onFoodProviderClick: (Int) -> Unit = {},
    foodProviderCategory: Category,
    mensaViewModel: MensaViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    ClockBroadcastReceiver(systemAction = Intent.ACTION_TIME_TICK) {
        mensaViewModel.updateOpeningHourTexts(Category.ANY)
    }
    
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

            when (mensaViewModel.foodProviderScreenState) {
                UiState.LOADING -> {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        LottieWithInfo(
                            lottie = R.raw.man_serving_catering_food,
                            description = stringResource(
                                id = when (foodProviderCategory) {
                                    Category.CANTEEN -> R.string.text_lottie_fetching_canteens
                                    Category.CAFETERIA -> R.string.text_lottie_fetching_cafeterias
                                    else -> R.string.text_lottie_error
                                }
                            )
                        )
                    }
                }
                UiState.NO_INFO -> {
                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        LottieWithInfo(
                            lottie = R.raw.no_info_variant,
                            description = stringResource(
                                id = when (foodProviderCategory) {
                                    Category.CANTEEN -> R.string.text_lottie_no_info_canteens
                                    Category.CAFETERIA -> R.string.text_lottie_no_info_cafeterias
                                    else -> R.string.text_lottie_no_info_canteens
                                }
                            )
                        )
                    }
                }
                else -> {
                    FoodProvidersList(
                        modifier = modifier
                            .padding(
                                innerPadding
                            )
                            .padding(bottom = MaterialSizes.BOTTOM_NAV_HEIGHT.dp),
                        list = when (foodProviderCategory) {
                            Category.CANTEEN -> mensaViewModel.canteens
                            Category.CAFETERIA -> mensaViewModel.cafeterias
                            else -> mensaViewModel.foodProviders
                        }.sortedByDescending { it.liked },
                        onClickedFoodProvider = { clickedProvider ->
                            onFoodProviderClick(clickedProvider.id ?: -1)
                        }
                    )
                }
            }
        }
    )
}