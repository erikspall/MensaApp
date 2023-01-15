package de.erikspall.mensaapp.ui.screens.additivefilters

import android.widget.Space
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Blender
import androidx.compose.material.icons.rounded.LunchDining
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.ui.MensaViewModel
import de.erikspall.mensaapp.ui.components.AdditiveFilterSection
import de.erikspall.mensaapp.ui.components.HeroToggle
import de.erikspall.mensaapp.ui.theme.Shrikhand

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdditiveFilterScreen(
    modifier: Modifier = Modifier,
    onBackClicked: (() -> Unit)? = null,
    mensaViewModel: MensaViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val filterVisibilityState = remember {
        MutableTransitionState(mensaViewModel.warningsActivated)
    }

    val ingredients by mensaViewModel.ingredients.observeAsState()
    val allergens by mensaViewModel.allergens.observeAsState()

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
                        onClick = onBackClicked ?: {}
                    )
                }
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
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                HeroToggle(
                    modifier = Modifier
                        .padding(top = 16.dp + innerPadding.calculateTopPadding(), start = 16.dp, end = 16.dp),
                    title = stringResource(id = R.string.text_hero_toggle_warnings_additives),
                    checked = mensaViewModel.warningsActivated,
                    onChecked = {
                        //switchChecked.value = it
                        mensaViewModel.enableWarnings(it)
                        filterVisibilityState.targetState = it
                    }
                )
                AnimatedVisibility(
                    modifier = Modifier.clip(RoundedCornerShape(corner = CornerSize(28.dp))), // Looks better
                    visibleState = filterVisibilityState
                ) {
                    Column(
                        modifier = Modifier
                            .padding(top = 24.dp)
                    ) {
                        AdditiveFilterSection(
                            modifier = Modifier
                                .padding(horizontal = 24.dp),
                            icon = Icons.Rounded.Blender,
                            sectionTitle = stringResource(id = R.string.text_ingredient_section),
                            items = ingredients ?: emptyList(), // Make it null safe?
                            onAdditiveClicked = {
                                mensaViewModel.saveLikeStatus(it, !it.isNotLiked)
                            }
                        )
                        Divider(
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                        )
                        AdditiveFilterSection(
                            modifier = Modifier
                                .padding(horizontal = 24.dp),
                            icon = Icons.Rounded.LunchDining,
                            sectionTitle = stringResource(id = R.string.text_allergen_section),
                            items = allergens ?: emptyList(), // Make it null safe?
                            onAdditiveClicked = {
                                mensaViewModel.saveLikeStatus(it, !it.isNotLiked)
                            }
                        )
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                }

            }
        }
    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewAdditiveFilterScreen() {
    AdditiveFilterScreen()
}