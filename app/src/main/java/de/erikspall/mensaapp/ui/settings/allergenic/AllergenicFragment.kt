package de.erikspall.mensaapp.ui.settings.allergenic

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.google.android.material.transition.MaterialSharedAxis
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.databinding.FragmentSettingsAllergenicBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.utils.Extensions.observeOnce
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.settings.allergenic.event.AllergenicEvent
import de.erikspall.mensaapp.ui.settings.allergenic.viewmodel.AllergenicViewModel

@AndroidEntryPoint
class AllergenicFragment() : Fragment() {
    private var _binding: FragmentSettingsAllergenicBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: AllergenicViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true).apply {
            duration = 500L
        }
        returnTransition = MaterialSharedAxis(MaterialSharedAxis.X, false).apply {
            duration = 250L
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }*/

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsAllergenicBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.containerFilterGroups.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    /* buffer so user does not accidentally hit nav buttons */ 8
        )

        prepareSwitchAndCard()
        setupObservers()
        setupListeners()

        return root
    }

    private fun prepareSwitchAndCard() {

        if (viewModel.state.warningsActivated.value == true) {
            binding.containerFilterGroups.visibility = View.VISIBLE
            // Color card
            val transitionDrawable =
                (binding.settingsAllergenicCardLayout.background as TransitionDrawable)
            transitionDrawable.startTransition(1)
            // Check switch
            binding.settingsAllergenicSwitch.isChecked = true
        }
    }

    private fun setupObservers() {
        viewModel.state.ingredients.observeOnce(viewLifecycleOwner) { ingredients ->
            Log.d("SettingsAllergenic", "${ingredients.size}")
            binding.filterGroupIngredients.chipGroupFilterGroup.removeAllViews()
            ingredients.forEach { ingredient ->
                binding.filterGroupIngredients.chipGroupFilterGroup.addView(
                    (layoutInflater.inflate(
                        R.layout.layout_filter_chip,
                        binding.filterGroupIngredients.chipGroupFilterGroup,
                        false
                    ) as Chip).apply {
                        text = ingredient.getName()
                        setEnsureMinTouchTargetSize(false)
                        isCheckable = true
                        isChecked = ingredient.getUserDoesNotLike()
                        setOnCheckedChangeListener { chip, isChecked ->
                            viewModel.onEvent(AllergenicEvent.OnIngredientChecked(chip.text.toString(), isChecked))
                        }
                    }
                )
            }
        }
        viewModel.state.allergenic.observeOnce(viewLifecycleOwner) { allergenic ->
            binding.filterGroupAllergenic.chipGroupFilterGroup.removeAllViews()
            allergenic.forEach { allergenic ->
                binding.filterGroupAllergenic.chipGroupFilterGroup.addView(
                    (layoutInflater.inflate(
                        R.layout.layout_filter_chip,
                        binding.filterGroupIngredients.chipGroupFilterGroup,
                        false
                    ) as Chip).apply {
                        text = allergenic.getName()
                        setEnsureMinTouchTargetSize(false)
                        isCheckable = true
                        isChecked = allergenic.getUserDoesNotLike()
                        setOnCheckedChangeListener { chip, isChecked ->
                            viewModel.onEvent(AllergenicEvent.OnAllergenicChecked(chip.text.toString(), isChecked))
                        }
                    }
                )
            }
        }
    }

    private fun setupListeners() {
        binding.settingsAllergenicSwitch.setOnCheckedChangeListener { _, isChecked ->
            //sharedPreferences.setBoolean(R.string.setting_warnings_enabled, isChecked)
            viewModel.onEvent(AllergenicEvent.OnWarningsChanged(isChecked))
            val transitionDrawable =
                (binding.settingsAllergenicCardLayout.background as TransitionDrawable)
            if (isChecked) {
                transitionDrawable.startTransition(300)
                binding.containerFilterGroups.visibility = View.VISIBLE
            } else {
                transitionDrawable.reverseTransition(300)
                binding.containerFilterGroups.visibility = View.INVISIBLE
            }
        }
        binding.settingsAllergenicToolbar.setNavigationOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}