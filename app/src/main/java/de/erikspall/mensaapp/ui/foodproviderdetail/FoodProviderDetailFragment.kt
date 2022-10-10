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
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.sources.local.database.entities.FoodProviderType
import de.erikspall.mensaapp.data.sources.local.database.entities.Menu
import de.erikspall.mensaapp.data.sources.local.database.entities.enums.Role
import de.erikspall.mensaapp.data.sources.remote.api.model.FoodProviderApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.MenuApiModel
//import de.erikspall.mensaapp.data.sources.local.dummy.DummyDataSource
import de.erikspall.mensaapp.databinding.FragmentFoodProviderDetailBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.usecases.foodprovider.FoodProviderUseCases
import de.erikspall.mensaapp.domain.utils.Extensions.observeOnce
//import de.erikspall.mensaapp.domain.model.interfaces.FoodProvider
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.foodproviderdetail.adapter.MenuAdapter
import de.erikspall.mensaapp.ui.foodproviderlist.canteenlist.CanteenListFragmentArgs
import de.erikspall.mensaapp.ui.foodproviderdetail.event.DetailEvent
import de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.FoodProviderDetailViewModel
import de.erikspall.mensaapp.ui.foodproviderlist.cafelist.CafeListFragmentArgs
import de.erikspall.mensaapp.ui.state.UiState
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FoodProviderDetailFragment : Fragment() {
    private var _binding: FragmentFoodProviderDetailBinding? = null

    @Inject
    lateinit var foodProviderUseCases: FoodProviderUseCases

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
    ): View? {

        _binding = FragmentFoodProviderDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root




        setMarginForIconButton()

        binding.buttonBack.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        // If CanteenListArgs contains -1, this means we navigatet from cafeteria list
        val safeArgsTemp1: CanteenListFragmentArgs by navArgs()
        val safeArgsTemp2: CafeListFragmentArgs by navArgs()
        val foodProviderId = if (safeArgsTemp1.foodProviderType == -1)
            safeArgsTemp2.foodProviderId
        else
            safeArgsTemp1.foodProviderId


        // TODO: move this and logic to viewmodel
        val showingCafeteria = (safeArgsTemp1.foodProviderType == 2) ||
                (safeArgsTemp2.foodProviderType == 2)

        //TODO: Hard to read
        foodProviderUseCases.getInfoOfFoodProvider(fid = foodProviderId.toLong())
            .asLiveData().observe(viewLifecycleOwner) {
                if (it == null) {
                    showMessage(
                        R.raw.error,
                        "Ohje! etwas ist schief gelaufen :(",
                        forceLottie = true
                    )
                } else {
                    // Workaround so text wraps nicely
                    binding.textFoodProviderName.text = it.foodProvider.name.replace("-", " ")

                    if (it.foodProvider.info.isBlank() && it.foodProvider.additionalInfo.isBlank())
                        binding.infoFoodProviderOpening.container.visibility = View.GONE
                    else
                        binding.infoFoodProviderOpening.infoText = it.foodProvider.info
                            .replace(", ", "\n\n").replace(") ", ")\n\n") +
                                if (it.foodProvider.info.isNotBlank()) "\n\n" else "" +
                                        it.foodProvider.additionalInfo.replace(", ", "\n\n")
                                            .replace(") ", ")\n\n")
                    binding.imageFoodProvider.setImageResource(it.foodProvider.icon)
                }
            }
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

        binding.recyclerViewMenus.adapter = adapter

        binding.recyclerViewMenus.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    MaterialSizes.BOTTOM_NAV_HEIGHT
        )

        viewModel.onEvent(
            DetailEvent.Init(
                fid = foodProviderId.toLong(),
                showingCafeteria = showingCafeteria
            )
        )

        // var notFirstTime = false
        viewModel.state.menus.observe(viewLifecycleOwner) { menus ->
            // TODO: Show lotti if non found
            adapter.warningsEnabled = viewModel.state.warningsEnabled
            adapter.role = viewModel.state.role
            //if (menus.isEmpty() && notFirstTime)
            //    showMessage(R.raw.no_menus, "Keine Gerichte gefunden :(", forceLottie = true)
            // else {
            // notFirstTime = true
            menus.let { adapter.submitList(it) }
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
        viewModel.state.uiState.observe(viewLifecycleOwner) { newUiState ->
            if (viewModel.state.showingCafeteria) { // Always show cafeteria lottie if cafeteria
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
        if (forceLottie || binding.recyclerViewMenus.adapter?.itemCount == 0) {
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
            binding.lottieContainer.animate().alpha(1f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.lottieContainer.visibility = View.VISIBLE
                }
            }
            binding.recyclerViewMenus.animate().alpha(0f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.recyclerViewMenus.visibility = View.INVISIBLE
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
            binding.lottieContainer.animate().alpha(0f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.lottieContainer.visibility = View.INVISIBLE
                }
            }
            binding.recyclerViewMenus.animate().alpha(1f).apply {
                duration = 300
                interpolator = AccelerateInterpolator()
                withEndAction {
                    binding.recyclerViewMenus.visibility = View.VISIBLE
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
}