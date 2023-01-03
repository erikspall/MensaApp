package de.erikspall.mensaapp.ui.screens.details

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.ui.components.DetailHeader
import de.erikspall.mensaapp.ui.components.ExpandableTextState
import de.erikspall.mensaapp.ui.components.FancyIndicator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    foodProvider: FoodProvider,
    chipScrollState: ScrollState = rememberScrollState(),
    descriptionState: ExpandableTextState = remember {
        ExpandableTextState()
    },
    additionalInfoState: ExpandableTextState = remember {
        ExpandableTextState()
    },
    onBackClicked: (() -> Unit)? = null
) {

    var hideBackButton by remember { mutableStateOf(false) }

    val hideBackButtonState = remember { MutableTransitionState(!hideBackButton) }

    val pagerState = rememberPagerState()

    //TODO Extract to own component
    var currentPageIndex by remember {
        mutableStateOf(0)
    }

    with(pagerState) {
        LaunchedEffect(key1 = currentPageIndex) {
            launch {
                animateScrollToPage(
                    page = (currentPageIndex)
                )
            }
        }
    }

    // Reuse the default offset animation modifier, but use our own indicator
    /* val indicator = @Composable { tabPositions ->
        FancyIndicator(
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]).padding(horizontal = 20.dp)
        )
    }*/
    val pages = listOf("1", "2", "3")

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y

                // Smoother
                if (delta.absoluteValue > 1.0) {
                    hideBackButton = delta < 0
                    hideBackButtonState.targetState = !hideBackButton
                }

                // called when you scroll the content
                return Offset.Zero
            }
        }
    }


    BackHandler(enabled = onBackClicked != null) {
        if (onBackClicked != null) {
            onBackClicked()
        }
    }

    Scaffold { innerPadding ->
        Box {
            AnimatedVisibility(
                modifier = Modifier
                    .zIndex(1f),
                visibleState = hideBackButtonState
            ) {

                FilledIconButton(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(start = 8.dp),
                    onClick = onBackClicked ?: {}
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = ""
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(125.dp)
                    .zIndex(0.9f)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.background,
                                Color.Transparent
                            )
                        )
                    )
            ) {

            }
            LazyColumn(
                modifier = Modifier.nestedScroll(nestedScrollConnection)
            ) {
                item {
                    DetailHeader(
                        foodProvider = foodProvider,
                        chipScrollState = chipScrollState,
                        descriptionState = descriptionState,
                        additionalInfoState = additionalInfoState
                    )
                }
                // Tabs
                item {
                    Column {
                        // TODO: Check if pager support m3 tabrow
                        androidx.compose.material.TabRow(
                            backgroundColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            selectedTabIndex = pagerState.currentPage,
                            indicator = { tabPositions ->
                                FancyIndicator(
                                    Modifier
                                        .pagerTabIndicatorOffset(pagerState, tabPositions)
                                )

                            }
                        ) {
                            // Add tabs for all of our pages
                            pages.forEachIndexed { index, title ->
                                Tab(
                                    text = { Text(title) },
                                    selected = pagerState.currentPage == index,
                                    onClick = {
                                        Log.d("TABS", "new index: $index")
                                        currentPageIndex = index
                                    },
                                )
                            }
                        }
                        HorizontalPager(
                            count = pages.size,
                            state = pagerState,
                        ) { page ->
                            // TODO: page content
                            Text(
                                text = "Page: $page",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        /*Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text = "Text tab ${selectedTab + 1} selected",
                            style = MaterialTheme.typography.bodyLarge
                        )*/
                    }
                }

            }
        }
    }


}

@Preview(showSystemUi = true)
@Composable
fun PreviewDetailScreen() {


    DetailScreen(
        foodProvider = FoodProvider(
            name = "Campus Hubland Nord",
            photo = R.drawable.mensateria_campus_hubland_nord_wuerzburg,
            openingHoursString = "Öffnet in 5 min",
            location = "Würzburg",
            type = "Mensateria",
            additionalInfo = "Die Abendmensa hat geschlossen!",
            description = "Die Mensateria mit 500 Innen- und 60 Balkonplätzen befindet sich im Obergeschoss des Gebäudes am westlichen Rand des Campus Nord am Hubland. Zur Südseite hin trägt die Mensateria einen Balkon, so dass ein Aufenthalt im Freien mit Blick auf den Hubland-Campus möglich ist.\n"
                    + "\nAusgestattet ist die Mensateria mit einem modernen Speiseleitsystem sowie speziellen Angebotsvarianten."
        )
    )
}