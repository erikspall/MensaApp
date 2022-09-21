package de.erikspall.mensaapp.ui.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.iterator
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.repositories.AppRepository
import de.erikspall.mensaapp.data.sources.remote.RemoteApiDataSource
import de.erikspall.mensaapp.databinding.FragmentSettingsBinding
import de.erikspall.mensaapp.domain.utils.Dialogs
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
        val settingsViewModel =
            ViewModelProvider(this)[SettingsViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupListeners()

        return root
    }

    fun setupListeners() {
        binding.settingRole.container.setOnClickListener {
            Dialogs.createSettingsRadioDialog(
                context = requireContext(),
                inflater = layoutInflater,
                title = "Rolle auswählen",
                message = "Die Preise für Gerichte passen sich an deine Rolle an",
                items = listOf("Student", "Bediensteter", "Gast"),
                selectedValue = "Student",
                onSave = {
                    Log.d("SettingDialog", it)
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