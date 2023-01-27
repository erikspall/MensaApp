package de.erikspall.mensaapp.ui.screens.details

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.SnapLayoutInfoProvider
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.pagerTabIndicatorOffset
import com.google.accompanist.pager.rememberPagerState
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.ui.MensaViewModel
import de.erikspall.mensaapp.ui.components.*
import de.erikspall.mensaapp.ui.state.UiState
import de.erikspall.mensaapp.ui.theme.Shrikhand
import de.erikspall.mensaapp.ui.utils.ClockBroadcastReceiver
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalPagerApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun DetailScreen(
    modifier: Modifier = Modifier,
    foodProvider: FoodProvider,

    descriptionState: ExpandableTextState = remember {
        ExpandableTextState()
    },
    additionalInfoState: ExpandableTextState = remember {
        ExpandableTextState()
    },
    onBackClicked: (() -> Unit)? = null,
    mensaViewModel: MensaViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val pagerState = rememberPagerState()

    val listState = rememberLazyListState()

    val snappingLayout = remember(listState) {
        SnapLayoutInfoProvider(
            listState
        )
    }

    val flingBehavior = rememberSnapFlingBehavior(snappingLayout)

    val configuration = LocalConfiguration.current

    val screenHeight = configuration.screenHeightDp.dp

    val isScrolledDownState = remember {
        MutableTransitionState(false)
    }

    //TODO Extract to own component
    var currentPageIndex by remember {
        mutableStateOf(0)
    }
    //val scope = rememberCoroutineScope()

    val menuUiStates = mutableListOf<MutableState<UiState>>()

    // TODO: Merge this
    val pages = (0..13).toList()
    val menuMap = mutableMapOf<Int, SnapshotStateList<Meal>>()
    pages.forEach { offset ->
        menuMap[offset] = remember {
            mutableStateListOf()
        }
        menuUiStates.add(remember {
            mutableStateOf(UiState.LOADING)
        })
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


    BackHandler(enabled = onBackClicked != null) {
        if (onBackClicked != null) {
            onBackClicked()
        }
    }

    ClockBroadcastReceiver(systemAction = Intent.ACTION_TIME_TICK) {
        mensaViewModel.updateOpeningHourTexts(Category.ANY)
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = foodProvider.name,
                        fontFamily = Shrikhand,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                        //style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (onBackClicked != null) {
                                onBackClicked()
                            }
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.expand_more),
                            contentDescription = ""
                        )
                    }
                },
                actions = {
                    IconToggleButton(
                        checked = true,
                        onCheckedChange = {
                            mensaViewModel.saveLikeStatus(foodProvider, !foodProvider.liked)
                        }
                    ) {
                        if (foodProvider.liked) {
                            Icon(
                                imageVector = Icons.Rounded.Favorite,
                                contentDescription = "",
                                tint = Color.Red
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Rounded.FavoriteBorder,
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.outline
                            )
                        }
                    }
                },
                scrollBehavior = scrollBehavior,
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
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            flingBehavior = flingBehavior
        ) {
            item {
                DetailHeader(
                    modifier = Modifier.padding(top = 8.dp),
                    foodProvider = foodProvider,
                    descriptionState = descriptionState,
                    additionalInfoState = additionalInfoState
                )
            }
            // Tabs
            if (Category.from(foodProvider.category) == Category.CANTEEN) {
                stickyHeader {
                    Column(
                        //  modifier = Modifier.padding(top = 16.dp)
                    ) {
                        // TODO: Check if pager support m3 tabrow
                        androidx.compose.material.ScrollableTabRow(
                            edgePadding = 20.dp,
                            backgroundColor = if (listState.firstVisibleItemIndex == 0)
                                MaterialTheme.colorScheme.background
                            else
                                MaterialTheme.colorScheme.surfaceColorAtElevation(
                                    3.dp
                                ),
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
                            pages.forEach { dayOffset ->
                                val date = LocalDate.now().plusDays(dayOffset.toLong())
                                Tab(
                                    modifier = Modifier.wrapContentWidth(),
                                    text = {
                                        val dayOfWeek = date.dayOfWeek.getDisplayName(
                                            TextStyle.SHORT_STANDALONE,
                                            Locale.getDefault()
                                        )
                                        val dateFormatted =
                                            date.format(
                                                DateTimeFormatter.ofLocalizedDate(
                                                    FormatStyle.SHORT
                                                )
                                            )

                                        Text("$dayOfWeek\n$dateFormatted")
                                    },
                                    selected = pagerState.currentPage == dayOffset,
                                    onClick = {
                                        Log.d("TABS", "new index: $dayOffset")
                                        currentPageIndex = dayOffset
                                    }
                                )
                            }
                        }
                    }
                }
                item {
                    HorizontalPager(
                        modifier = Modifier
                            .height(screenHeight - 64.dp), // 64dp is the height of the top appbar
                        count = pages.size,
                        state = pagerState,
                        verticalAlignment = Alignment.Top
                    ) { page ->
                        LaunchedEffect(key1 = "$page" + "menus") {
                            /*scope.*/launch {
                            //var menuUiState by mutableStateOf(UiState.LOADING)
                            menuUiStates[page].value = UiState.LOADING

                            val menu = mensaViewModel.getMenu(
                                foodProvider.id!!,
                                page
                            )

                            val meals = if (menu.isSuccess) {
                                val temp = menu.getOrThrow().meals
                                menuUiStates[page].value = if (temp.isEmpty()) {
                                    UiState.NO_INFO
                                } else {
                                    UiState.NORMAL
                                }
                                temp
                            } else {
                                UiState.ERROR
                                emptyList()
                            }

                            menuMap[page]!!.clear()
                            menuMap[page]!!.addAll(meals)
                        }
                        }
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .verticalScroll(
                                    rememberScrollState(),
                                    enabled = isScrolledDownState.currentState
                                ),
                            verticalArrangement = Arrangement.Top
                            // contentPadding = PaddingValues(vertical = 16.dp)
                        ) {
                            LaunchedEffect(listState) {
                                snapshotFlow { listState.firstVisibleItemIndex }
                                    .collect { isScrolledDownState.targetState = it != 0 }
                            }

                            AnimatedVisibility(
                                modifier = Modifier.clip(RoundedCornerShape(corner = CornerSize(28.dp))), // Looks better
                                visibleState = isScrolledDownState
                            ) {
                                Spacer(modifier = Modifier.height(48.dp))
                            }

                            when (menuUiStates[page].value) {
                                UiState.NORMAL -> {
                                    menuMap[page]?.forEach { meal ->
                                        MealCard(
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            meal = meal,
                                            role = mensaViewModel.role
                                        )
                                    }
                                }
                                UiState.NO_INFO -> {
                                    Column(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        LottieWithInfo(
                                            lottie = R.raw.no_info,
                                            iterations = 1,
                                            description = stringResource(
                                                id = R.string.text_lottie_no_meals
                                            )
                                        )
                                    }
                                }
                                UiState.LOADING -> {
                                    Column(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        LottieWithInfo(
                                            lottie = R.raw.loading_menus,
                                            description = stringResource(
                                                id = R.string.text_lottie_fetching_meals
                                            )
                                        )
                                    }
                                }
                                else -> {
                                    Column(
                                        modifier = Modifier.fillMaxHeight(),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        LottieWithInfo(
                                            lottie = R.raw.error,
                                            description = stringResource(
                                                id = R.string.text_lottie_error
                                            )
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(80.dp))
                        }

                    }
                }
            } else {
                // It is a cafeteria, so show lottie for it
                item {
                    Column(
                        modifier = Modifier
                            .padding(top = 24.dp)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.Center
                    ) {
                        LottieWithInfo(
                            lottie = R.raw.cafeteria,
                            description = "Life without Coffee is scary", // TODO: random quote
                            speed = 0.5f
                        )
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
            location = "Würzburg",
            type = "Mensateria",
            additionalInfo = "Die Abendmensa hat geschlossen!",
            description = "Die Mensateria mit 500 Innen- und 60 Balkonplätzen befindet sich im Obergeschoss des Gebäudes am westlichen Rand des Campus Nord am Hubland. Zur Südseite hin trägt die Mensateria einen Balkon, so dass ein Aufenthalt im Freien mit Blick auf den Hubland-Campus möglich ist.\n"
                    + "\nAusgestattet ist die Mensateria mit einem modernen Speiseleitsystem sowie speziellen Angebotsvarianten."
        ).apply {
            this.openingHoursString = "Öffnet in 5 min"
        }
    )
}