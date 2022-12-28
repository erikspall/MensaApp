package de.erikspall.mensaapp.ui.screens.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.enums.StringResEnum
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.screens.settings.events.SettingsScreenEvent
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context, // This should be okay
    private val sharedPreferences: SharedPreferenceUseCases
) : ViewModel() {
    private val state = SettingsState()

    val role: Role
        get() = state.role
    val location: Location
        get() = state.location
    val warningsActivated: Boolean
        get() = state.warningsActivated
    val settingsInitialized: Boolean
        get() = state.settingsInitialized

    fun onEvent(event: SettingsScreenEvent) {
        when (event) {
            is SettingsScreenEvent.Init -> {

                state.role = StringResEnum.roleFrom(
                    sharedPreferences.getValueRes(
                        key = R.string.shared_pref_role,
                        defaultValue = Role.STUDENT.getValue()
                    )
                )

                state.location = StringResEnum.locationFrom(
                    sharedPreferences.getValueRes(
                        key = R.string.shared_pref_location,
                        defaultValue = Location.WUERZBURG.getValue()
                    )
                )

                state.warningsActivated = sharedPreferences.getBoolean(
                    R.string.setting_warnings_enabled,
                    false
                )

                sharedPreferences.registerListener { prefs, key ->
                    if (key == context.getString(R.string.shared_pref_role)) {
                        state.role = StringResEnum.roleFrom(
                            prefs.getInt(
                                context.getString(R.string.shared_pref_role),
                                Role.STUDENT.getValue()
                            )
                        )
                    } else if (key == context.getString(R.string.shared_pref_location)) {
                        state.location = StringResEnum.locationFrom(
                            prefs.getInt(
                                context.getString(R.string.shared_pref_location),
                                Location.WUERZBURG.getValue()
                            )
                        )
                    } else if (key == context.getString(R.string.setting_warnings_enabled)) {
                        state.warningsActivated = prefs.getBoolean(
                            context.getString(R.string.setting_warnings_enabled),
                            false
                        )
                    }
                }

                state.settingsInitialized = true
            }
        }
    }
}