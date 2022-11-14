package de.erikspall.mensaapp.ui.foodproviderdetail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.databinding.FragmentFoodProviderDetailBinding
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.utils.MaterialTextViewExtension.setTextWithLineConstraint
import de.erikspall.mensaapp.ui.foodproviderdetail.adapter.MenuAdapter
import de.erikspall.mensaapp.ui.foodproviderlist.canteenlist.CanteenListFragmentArgs
import de.erikspall.mensaapp.ui.foodproviderdetail.event.DetailEvent
import de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.FoodProviderDetailViewModel
import de.erikspall.mensaapp.ui.foodproviderlist.cafelist.CafeListFragmentArgs
import de.erikspall.mensaapp.ui.state.UiState
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.*

@AndroidEntryPoint
class FoodProviderDetailFragment : Fragment() {
    private var _binding: FragmentFoodProviderDetailBinding? = null

    private val viewModel: FoodProviderDetailViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialFadeThrough().apply {
            duration = 300L
        }
        returnTransition = MaterialElevationScale(false).apply {
            duration = 100L
        }
        exitTransition = MaterialElevationScale(false).apply {
            duration = 300L
        }
        // reenterTransition = MaterialElevationScale(true).apply {
        //// }
        //sharedElementReturnTransition = animation
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFoodProviderDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root




        setMarginForIconButton()

        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // If CanteenListArgs contains -1, this means we navigated from cafeteria list
        val safeArgsTemp1: CanteenListFragmentArgs by navArgs()
        val safeArgsTemp2: CafeListFragmentArgs by navArgs()
        val foodProviderId = if (safeArgsTemp1.foodProviderCategory == -1)
            safeArgsTemp2.foodProviderId
        else
            safeArgsTemp1.foodProviderId


        // TODO: move this and logic to viewmodel
        val showingCafeteria = (safeArgsTemp1.foodProviderCategory == Category.CAFETERIA.ordinal) // ||
               // (safeArgsTemp2.foodProviderType == 2)


        // binding.textFoodProviderName.text = DummyDataSource.canteens[foodProviderId].getName()

        val adapter = MenuAdapter(
            requireContext(),
            binding.menusHolder,
            viewModel.state.warningsEnabled,
            viewModel.state.role,
            lifecycleScope,
            onFinishedConstructing = {
                Log.d("DetailPage", "Finished")
                makeLottieVisible(false)
            }
        )
        binding.viewPagerMenus.adapter = adapter

        /*
        binding.recyclerViewMenus.adapter = adapter

        binding.recyclerViewMenus.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    MaterialSizes.BOTTOM_NAV_HEIGHT
        )
        */
        viewModel.onEvent(
            DetailEvent.Init(
                foodProviderId = foodProviderId,
                showingCafeteria = showingCafeteria
            )
        )

        // var notFirstTime = false
        viewModel.state.menus.observe(viewLifecycleOwner) { menus ->


            Log.d("$TAG:menus", "received ${menus.size} menus")

            adapter.warningsEnabled = viewModel.state.warningsEnabled
            adapter.role = viewModel.state.role
            //if (menus.isEmpty() && notFirstTime)
            //    showMessage(R.raw.no_menus, "Keine Gerichte gefunden :(", forceLottie = true)
            // else {
            // notFirstTime = true
            menus.let { adapter.submitList(it) }

            if (menus.isNotEmpty())
                TabLayoutMediator(binding.tabLayout, binding.viewPagerMenus) { tab, position ->
                    Log.d("$TAG:viewPager", "Lets set this stuff")
                    if (menus.isNotEmpty()) {
                        val dayOfWeek = menus[position].date.dayOfWeek.getDisplayName(
                            TextStyle.SHORT_STANDALONE,
                            Locale.getDefault()
                        )
                        val date =
                            menus[position].date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))

                        tab.text = "$dayOfWeek\n$date"
                    }
                }.attach()
            // }
        }


        setupObservers()

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        viewModel.state.foodProvider.observe(viewLifecycleOwner) { foodProvider ->
            if (foodProvider != null) {
                // Workaround so text wraps nicely
                binding.textFoodProviderName.text = foodProvider.name.replace("-", " ")

                if (foodProvider.info.isBlank() && foodProvider.additionalInfo.isBlank())
                    binding.infoFoodProviderOpening.container.visibility = View.GONE
                else {
                    val infoString = foodProvider.info
                        .replace(", ", "\n\n")
                        .replace(") ", ")\n\n")

                    val additionalInfoString = foodProvider.additionalInfo
                        .replace(", ", "\n\n")
                        .replace(") ", ")\n\n")

                    val combinedString = if (infoString.isBlank()) {
                        additionalInfoString
                    } else if (additionalInfoString.isBlank()) {
                        infoString
                    } else {
                        infoString + "\n\n" + additionalInfoString
                    }
                    binding.infoFoodProviderOpening.textView.setTextWithLineConstraint(
                        combinedString,
                        1
                    )
                }

                if (foodProvider.description.isBlank())
                    binding.infoFoodProviderDescription.container.visibility = View.GONE
                else
                    binding.infoFoodProviderDescription.textView.setTextWithLineConstraint(
                        foodProvider.description,
                        1
                    )

                binding.imageFoodProvider.setImageResource(foodProvider.photo)
            }
        }

        viewModel.state.uiState.observe(viewLifecycleOwner) { newUiState ->
            if (viewModel.state.category == Category.CAFETERIA) { // Always show cafeteria lottie if cafeteria
                when (newUiState) {
                    UiState.NORMAL -> {
                        // How did you end up here?
                        makeLottieVisible(false)
                    }
                    else -> {
                        showMessage(
                            R.raw.cafeteria,
                            "",
                            animationSpeed = 0.5f,
                            forceLottie = true
                        )
                    }
                }
            } else when (newUiState) {
                UiState.LOADING -> {
                    showMessage(
                        R.raw.loading_menus,
                        "Sucht nach Gerichten ...",
                        forceLottie = true
                    )
                }
                UiState.NO_INFO -> {

                    showMessage(
                        R.raw.no_menus,
                        "Keine Gerichte gefunden :(",
                        forceLottie = true
                    )
                }
                UiState.NORMAL -> {
                    makeLottieVisible(false)
                }
                UiState.ERROR -> {
                    showMessage(
                        R.raw.error,
                        "Ohje! Etwas ist schiefgelaufen :(",
                        forceLottie = true
                    )
                }
                UiState.NO_CONNECTION -> {
                    showMessage(
                        R.raw.no_connection,
                        "Der Server antwortet nicht :(",
                    )
                }
                UiState.NO_INTERNET -> {
                    showMessage(
                        R.raw.no_internet,
                        "Der Server ist nicht erreichbar :("
                    )
                }
                else -> {

                }
            }
        }
    }

    private fun showMessage(
        @RawRes animation: Int,
        errorMsg: String,
        animationSpeed: Float = 1f,
        forceLottie: Boolean = false
    ) {
        if (forceLottie /*|| binding.recyclerViewMenus.adapter?.itemCount == 0*/) {
            binding.lottieAnimationView.speed = animationSpeed
            binding.lottieAnimationView.clearAnimation()
            binding.lottieAnimationView.setAnimation(animation)
            binding.lottieAnimationView.playAnimation()
            binding.textLottie.text = errorMsg
            makeLottieVisible(true)
        } else {
            Toast.makeText(requireContext(), errorMsg, Toast.LENGTH_LONG).show()
        }
    }

    private fun makeLottieVisible(visible: Boolean) {
        if (visible) {
            //binding.dividerInfoMenu.visibility = View.VISIBLE
            binding.lottieLinear.animate().alpha(1f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.lottieLinear.visibility = View.VISIBLE
                }
            }
            binding.tabLayout.animate().alpha(0f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.tabLayout.visibility = View.INVISIBLE
                }
            }
            binding.fadeBottom.animate().alpha(0f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.fadeBottom.visibility = View.INVISIBLE
                }
            }
        } else {
            //binding.dividerInfoMenu.visibility = View.INVISIBLE
            binding.lottieLinear.animate().alpha(0f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.lottieLinear.visibility = View.INVISIBLE
                }
            }
            binding.tabLayout.animate().alpha(1f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.tabLayout.visibility = View.VISIBLE
                }
            }
            binding.fadeBottom.animate().alpha(1f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.fadeBottom.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setMarginForIconButton() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.buttonBack) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                leftMargin = insets.left + 16
                rightMargin = insets.right + 16
                topMargin = insets.top
            }

            windowInsets
        }


        // val params = binding.iconButton.layoutParams as ViewGroup.MarginLayoutParams
        //val statusBarHeight = view.rootWindowInsets.getInsetsIgnoringVisibility(WindowInsets.Type.systemBars()).top
        // params.setMargins(
        //     8, statusBarHeight, 8, 0
        // )

        //binding.iconButton.layoutParams = params
    }

    override fun onResume() {
        super.onResume()
        binding.tabLayout.requestLayout()
    }

    companion object {
        const val TAG = "DetailFragment"
    }
}