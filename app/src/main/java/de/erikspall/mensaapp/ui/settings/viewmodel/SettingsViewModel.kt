package de.erikspall.mensaapp.ui.settings.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.enums.Role
import de.erikspall.mensaapp.domain.enums.StringResEnum
import de.erikspall.mensaapp.domain.usecases.sharedpreferences.SharedPreferenceUseCases
import de.erikspall.mensaapp.ui.settings.event.SettingsEvent
import de.erikspall.mensaapp.ui.settings.viewmodel.state.SettingsState
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val sharedPreferences: SharedPreferenceUseCases
) : ViewModel() {
    val state = SettingsState()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnInit -> {
                state.role.postValue(
                    StringResEnum.roleFrom(
                        sharedPreferences.getValueRes(
                            key = R.string.shared_pref_role,
                            defaultValue = Role.STUDENT.getValue()
                        )
                    )
                )

                state.location.postValue(
                    StringResEnum.locationFrom(
                        sharedPreferences.getValueRes(
                            key = R.string.shared_pref_location,
                            defaultValue = Location.WUERZBURG.getValue()
                        )
                    )
                )

                state.warningsActivated.postValue(
                        sharedPreferences.getBoolean(
                                R.string.setting_warnings_enabled,
                                false
                        )
                )

                sharedPreferences.registerListener { prefs, key ->
                    if (key == context.getString(R.string.shared_pref_role)) {
                        state.role.postValue(
                            // TODO: Language change may cause problems here
                            StringResEnum.roleFrom(
                                prefs.getInt(
                                    context.getString(R.string.shared_pref_role),
                                    Role.STUDENT.getValue()
                                )
                            )
                        )
                    } else if (key == context.getString(R.string.shared_pref_location)) {
                        state.location.postValue(
                            StringResEnum.locationFrom(
                                prefs.getInt(
                                    context.getString(R.string.shared_pref_location),
                                    Location.WUERZBURG.getValue()
                                )
                            )
                        )
                    } else if (key == context.getString(R.string.setting_warnings_enabled)) {
                        state.warningsActivated.postValue(
                                prefs.getBoolean(
                                        context.getString(R.string.setting_warnings_enabled),
                                        false
                                )
                        )
                    }
                }
            }
            is SettingsEvent.OnNewLocation -> {
                sharedPreferences.setValue(R.string.shared_pref_location, event.location.getValue())
            }
            is SettingsEvent.OnNewRole -> {
                sharedPreferences.setValue(R.string.shared_pref_role, event.role.getValue())
            }
        }
    }
}