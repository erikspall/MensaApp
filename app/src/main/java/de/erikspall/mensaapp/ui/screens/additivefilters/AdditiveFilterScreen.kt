package de.erikspall.mensaapp.ui.screens.additivefilters

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.ui.MensaViewModel
import de.erikspall.mensaapp.ui.components.HeroToggle
import de.erikspall.mensaapp.ui.theme.Shrikhand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditiveFilterScreen(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit = {},
    mensaViewModel: MensaViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    //val switchChecked = remember {
    //    mutableStateOf(mensaViewModel.warningsActivated)
    // }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.text_warnings),
                        fontFamily = Shrikhand,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        content = {
                            Icon(
                                imageVector = Icons.Rounded.ArrowBack,
                                contentDescription = ""
                            )
                        },
                        onClick = onBackClicked
                    )
                }
            )
        },
        content = { innerPadding ->
            Column {
                HeroToggle(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp),
                    title = stringResource(id = R.string.text_hero_toggle_warnings_additives),
                    checked = mensaViewModel.warningsActivated,
                    onChecked = {
                        //switchChecked.value = it
                        mensaViewModel.enableWarnings(it)
                    })
            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAdditiveFilterScreen() {
    AdditiveFilterScreen()
}