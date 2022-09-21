package de.erikspall.mensaapp.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.databinding.FragmentSettingsBinding
import de.erikspall.mensaapp.domain.utils.Dialogs
import de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.FoodProviderDetailViewModel
import de.erikspall.mensaapp.ui.settings.event.SettingsEvent
import de.erikspall.mensaapp.ui.settings.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    @Inject
    lateinit var test: AppRepository

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 300L
        }
        exitTransition = MaterialElevationScale(false).apply {
            duration = 100L
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 300L
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupListeners()
        setupObservers()

        viewModel.onEvent(SettingsEvent.OnInit)

        return root
    }

    fun setupObservers() {
        viewModel.state.role.observe(viewLifecycleOwner) {
            binding.settingRole.settingsValue = requireContext().getString(it.getValue())
        }
        viewModel.state.location.observe(viewLifecycleOwner) {
            binding.settingLocation.settingsValue = it
        }
    }

    fun setupListeners() {
        binding.settingRole.container.setOnClickListener {
            Dialogs.createSettingsRadioDialog(
                context = requireContext(),
                inflater = layoutInflater,
                title = "Rolle auswählen",
                message = "Die Preise für Gerichte passen sich an deine Rolle an",
                items = listOf(Role.STUDENT, Role.EMPLOYEE, Role.GUEST),
                selectedValue = viewModel.state.role.value ?: Role.STUDENT,
                onSave = {
                    viewModel.onEvent(SettingsEvent.OnNewRole(it))
                }
            ).show()
        }
        binding.settingInfo.container.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                test.fetchAndSaveLatestData()
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}