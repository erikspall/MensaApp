package de.erikspall.mensaapp.ui.foodproviderlist.canteenlist

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.repositories.FakeAdditiveRepository
import de.erikspall.mensaapp.data.repositories.FakeAppRepository
import de.erikspall.mensaapp.domain.enums.AdditiveType
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.model.Additive
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.model.Meal
import de.erikspall.mensaapp.domain.model.Menu
import de.erikspall.mensaapp.domain.usecases.foodproviders.FetchFoodProvider
import de.erikspall.mensaapp.domain.usecases.foodproviders.FetchFoodProviders
import de.erikspall.mensaapp.domain.usecases.foodproviders.FetchMenus
import de.erikspall.mensaapp.domain.usecases.foodproviders.FoodProviderUseCases
import de.erikspall.mensaapp.domain.usecases.openinghours.FormatToString
import de.erikspall.mensaapp.domain.usecases.openinghours.OpeningHourUseCases
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.*
import de.erikspall.mensaapp.getOrAwaitValue
import de.erikspall.mensaapp.ui.foodproviderlist.event.FoodProviderListEvent
import de.erikspall.mensaapp.ui.state.UiState
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
@Config(sdk = [Build.VERSION_CODES.R])
class CanteenListViewModelTest {
    private lateinit var fakeAppRepository: FakeAppRepository
    private lateinit var sharedPreferenceUseCases: SharedPreferenceUseCases

    // Subject under test
    private lateinit var canteenListViewModel: CanteenListViewModel

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        val additives = mutableListOf(
            Additive(name = "Schwein", type = AdditiveType.INGREDIENT)
        )

        fakeAppRepository = FakeAppRepository(
            fakeAdditiveRepository = FakeAdditiveRepository(
                additives = mutableListOf(
                    Additive(name = "Schwein", type = AdditiveType.INGREDIENT)
                )
            ),
            foodProviders = mutableListOf(
                FoodProvider(
                    id = 0,
                    name = "Campus Hubland Nord",
                    category = "Canteen",
                    type = "Mensateria",
                    location = "Würzburg"
                )
            ),
            additives = additives,
            menus = mutableMapOf(
                0 to listOf(
                    Menu(
                        date = LocalDate.now(),
                        meals = listOf(
                            Meal(
                                name = "Kötbullar",
                                additives = listOf(
                                    additives[0]
                                ),
                                prices = mapOf(
                                    Role.STUDENT to "1,00€",
                                    Role.EMPLOYEE to "2,00€",
                                    Role.GUEST to "3,00€"
                                )
                            )
                        )
                    )

                )
            )
        )

        val sharedPreferences = ApplicationProvider.getApplicationContext<Context>()
            .getSharedPreferences("Test", MODE_PRIVATE)


        sharedPreferenceUseCases = SharedPreferenceUseCases(
            setBoolean = SetBoolean(
                ApplicationProvider.getApplicationContext(),
                sharedPreferences
            ),
            getBoolean = GetBoolean(
                ApplicationProvider.getApplicationContext(),
                sharedPreferences
            ),
            getValue = GetValue(ApplicationProvider.getApplicationContext(), sharedPreferences),
            setValue = SetValue(ApplicationProvider.getApplicationContext(), sharedPreferences),
            getLocalDateTime = GetLocalDateTime(
                ApplicationProvider.getApplicationContext(),
                sharedPreferences
            ),
            setLocalDateTime = SetLocalDateTime(
                ApplicationProvider.getApplicationContext(),
                sharedPreferences
            ),
            getValueRes = GetValueRes(
                ApplicationProvider.getApplicationContext(),
                sharedPreferences
            ),
            registerListener = RegisterListener(sharedPreferences)
        )

        canteenListViewModel = CanteenListViewModel(
            foodProviderUseCases = FoodProviderUseCases(
                fetch = FetchFoodProvider(fakeAppRepository),
                fetchAll = FetchFoodProviders(fakeAppRepository),
                fetchMenus = FetchMenus(fakeAppRepository)
            ),
            sharedPreferences = sharedPreferenceUseCases,
            openingHourUseCases = OpeningHourUseCases(
                formatToString = FormatToString()
            )
        )
    }

    @Test
    fun successfulInit_setsDefaultLocationAndShowsNormal() {
        // Given a fresh ViewModel, with food providers in repository and no location set
        val copy = canteenListViewModel

        // When initialising
        copy.onEvent(FoodProviderListEvent.Init)

        // Then ViewModel fetched food providers and shows normal
        val uiState = copy.state.uiState.getOrAwaitValue()
        // and set the default location
        val location = copy.state.location

        assertEquals(
            UiState.NORMAL,
            uiState
        ) // If UiState is loading it also means a fetch was triggered

        assertEquals(Location.WUERZBURG, location)
    }

    @Test
    fun initWithNewLocation_refreshesFoodProviders() {
        // Given a viewModel with location set to Würzburg, and sharedPreference set to Aschaffenburg
        val copy = canteenListViewModel
        copy.state.location = Location.WUERZBURG
        sharedPreferenceUseCases.setValue(R.string.shared_pref_location, Location.ASCHAFFENBURG.getValue())

        // When initialising
        copy.onEvent(FoodProviderListEvent.Init)

        // Then the location should be updated
        val location = copy.state.location
        assertEquals(Location.ASCHAFFENBURG, location)
        // And (here) the list food providers is empty (no foodproviders for Aschaffenburg
        assertEquals(0, copy.state.foodProviders.getOrAwaitValue().size)
    }

    @Test
    fun zeroFoodProviders_showsNoInfo() {
        val copy = canteenListViewModel
        copy.state.location = Location.ASCHAFFENBURG
        sharedPreferenceUseCases.setValue(R.string.shared_pref_location, Location.ASCHAFFENBURG.getValue())

        copy.onEvent(FoodProviderListEvent.Init)

        assertEquals(0, copy.state.foodProviders.getOrAwaitValue().size)
        assertEquals(UiState.NO_INFO, copy.state.uiState.getOrAwaitValue())
    }
}