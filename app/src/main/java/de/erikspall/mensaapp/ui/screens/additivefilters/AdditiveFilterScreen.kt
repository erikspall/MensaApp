package de.erikspall.mensaapp.ui.screens.additivefilters

import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.ui.MensaViewModel
import de.erikspall.mensaapp.ui.components.FoodProvidersList
import de.erikspall.mensaapp.ui.theme.Shrikhand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditiveFilterScreen(
    modifier: Modifier = Modifier,
    mensaViewModel: MensaViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.text_warnings), fontFamily = Shrikhand, style = MaterialTheme.typography.headlineMedium
                    )
                },
                scrollBehavior = scrollBehavior,
            )
        },
        content = { innerPadding ->

        }
    )
}