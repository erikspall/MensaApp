package de.erikspall.mensaapp.ui.settings.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.databinding.FragmentSettingsAboutBinding
import de.erikspall.mensaapp.databinding.FragmentSettingsAllergenicBinding
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor

@AndroidEntryPoint
class AboutFragment: Fragment() {
    private var _binding: FragmentSettingsAboutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 500L
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = 250L
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsAboutBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.containerAboutCards.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    /* buffer so user does not accidentally hit nav buttons */ 8
        )

        //setupObservers()
        setupListeners()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        binding.settingsAboutToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }
}