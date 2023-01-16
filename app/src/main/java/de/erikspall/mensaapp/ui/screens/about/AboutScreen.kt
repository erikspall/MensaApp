package de.erikspall.mensaapp.ui.screens.about

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.ui.components.InfoCard
import de.erikspall.mensaapp.ui.theme.Shrikhand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(
    modifier: Modifier = Modifier,
    onBackClicked: (() -> Unit)? = null,
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val uriHandler = LocalUriHandler.current
    /**
     * If onBackClicked is null it was not called from the app -> let system handle it
     */
    BackHandler(enabled = onBackClicked != null) {
        if (onBackClicked != null) {
            onBackClicked()
        }
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MediumTopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.text_label_about),
                        fontFamily = Shrikhand,
                       // style = MaterialTheme.typography.headlineMedium
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
                        onClick = onBackClicked ?: {}
                    )
                },
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.background
                            )
                        )
                    )
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
                .verticalScroll(rememberScrollState())
            ,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            InfoCard(
                modifier.padding(horizontal = 16.dp).padding(top = 8.dp),
                icon = Icons.Outlined.Info,
                title = stringResource(id = R.string.text_about_this_app),
                content = {
                    Column {
                        Text(
                            text = "${stringResource(id = R.string.text_app_info_1)}\n",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${stringResource(id = R.string.text_app_info_2)}\n",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${stringResource(id = R.string.text_app_info_3)}\n",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(id = R.string.text_app_info_4),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
            InfoCard(
                modifier.padding(horizontal = 16.dp),
                icon = Icons.Rounded.Code,
                title = stringResource(id = R.string.text_label_for_developers),
                content = {
                    Column {
                        Text(
                            text = "${stringResource(id = R.string.text_open_source_body_1)}\n",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${stringResource(id = R.string.text_open_source_body_2)}\n",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(id = R.string.text_open_source_body_3),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                },
                buttonText = stringResource(id = R.string.text_label_open_github),
                onButtonClick = {
                    uriHandler.openUri("https://github.com/mensa-app-wuerzburg/Android")
                }
            )
            InfoCard(
                modifier.padding(horizontal = 16.dp),
                icon = Icons.Rounded.AccountBalance,
                title = stringResource(id = R.string.text_impressum_title),
                content = {
                    Column {
                        Text(
                            text = "${stringResource(id = R.string.text_impressum_body_1)}\n",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${stringResource(id = R.string.text_impressum_body_2)}\n",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${stringResource(id = R.string.text_impressum_body_3)}\n",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(id = R.string.text_impressum_body_4),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
            InfoCard(
                modifier.padding(horizontal = 16.dp),
                icon = Icons.Rounded.LocalPolice,
                title = stringResource(id = R.string.text_disclaimer_title),
                content = {
                    Column {
                        Text(
                            text = stringResource(id = R.string.text_disclaimer_body_1),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${stringResource(id = R.string.text_disclaimer_body_2)}\n",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(id = R.string.text_disclaimer_body_3),
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = stringResource(id = R.string.text_disclaimer_body_4),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Preview
@Composable
fun PreviewAboutScreen() {
    AboutScreen()
}