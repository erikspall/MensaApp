package de.erikspall.mensaapp.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.MaterialSharedAxis.Axis
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Location
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.databinding.FragmentSettingsBinding
import de.erikspall.mensaapp.domain.utils.Dialogs
import de.erikspall.mensaapp.ui.settings.event.SettingsEvent
import de.erikspall.mensaapp.ui.settings.viewmodel.SettingsViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialElevationScale(true).apply {
            duration = 150L
        }
        exitTransition = MaterialElevationScale(false).apply {
            duration = 100L
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = 150L
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

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
            binding.settingLocation.settingsValue = requireContext().getString(it.getValue())
        }
        viewModel.state.warningsActivated.observe(viewLifecycleOwner) {
            if (it)
                binding.settingAllergenics.settingsValue = requireContext().getString(R.string.text_warnings_enabled)
            else
                binding.settingAllergenics.settingsValue = requireContext().getString(R.string.text_warnings_disabled)
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
                iconRes = R.drawable.ic_role,
                onSave = {
                    viewModel.onEvent(SettingsEvent.OnNewRole(it))
                }
            ).show()
        }

        binding.settingLocation.container.setOnClickListener {
            Dialogs.createSettingsRadioDialog(
                context = requireContext(),
                inflater = layoutInflater,
                title = "Standort auswählen",
                message = "Wähle hier welche Mensen und Cafeterien du sehen möchtest",
                items = listOf(Location.WUERZBURG, Location.ASCHAFFENBURG, Location.BAMBERG, Location.SCHWEINFURT),
                selectedValue = viewModel.state.location.value ?: Location.WUERZBURG,
                iconRes = R.drawable.ic_location,
                onSave = {
                    viewModel.onEvent(SettingsEvent.OnNewLocation(it))
                }
            ).show()
        }

        binding.settingInfo.container.setOnClickListener {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
                duration = 250L
            }
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
                duration = 500L
            }
            val directions = SettingsFragmentDirections.actionSettingsDestToAboutFragment()
            findNavController().navigate(directions)
        }

        binding.settingAllergenics.container.setOnClickListener {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
                duration = 250L
            }
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
                duration = 500L
            }
            val directions = SettingsFragmentDirections.actionSettingsDestToAllergenicFragment()
            findNavController().navigate(directions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}