package de.erikspall.mensaapp.ui.foodproviderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.sources.local.database.entities.Menu
import de.erikspall.mensaapp.data.sources.remote.api.model.FoodProviderApiModel
import de.erikspall.mensaapp.data.sources.remote.api.model.MenuApiModel
//import de.erikspall.mensaapp.data.sources.local.dummy.DummyDataSource
import de.erikspall.mensaapp.databinding.FragmentFoodProviderDetailBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.usecases.foodprovider.FoodProviderUseCases
//import de.erikspall.mensaapp.domain.model.interfaces.FoodProvider
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.adapter.MenuAdapter
import de.erikspall.mensaapp.ui.canteenlist.CanteenListFragmentArgs
import de.erikspall.mensaapp.ui.foodproviderdetail.event.DetailEvent
import de.erikspall.mensaapp.ui.foodproviderdetail.viewmodel.FoodProviderDetailViewModel
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
        // exitTransition = MaterialElevationScale(false).apply {
        //     duration = 100L
        // }
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

        val safeArgs: CanteenListFragmentArgs by navArgs()
        val foodProviderId = safeArgs.canteenId

        foodProviderUseCases.getInfoOfFoodProvider(fid = foodProviderId.toLong())
            .asLiveData().observe(viewLifecycleOwner) {
                binding.textFoodProviderName.text = it.foodProvider.name
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
        // binding.textFoodProviderName.text = DummyDataSource.canteens[foodProviderId].getName()

        val adapter = MenuAdapter(
            requireContext(),
            binding.menusHolder
        )

        //binding.recyclerViewMenus.setHasFixedSize(true)

        binding.recyclerViewMenus.adapter = adapter

        binding.recyclerViewMenus.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    MaterialSizes.BOTTOM_NAV_HEIGHT
        )

        viewModel.state.menus.observe(viewLifecycleOwner) { menus ->
            // TODO: Show lotti if non found
            menus.let { adapter.submitList(it) }
        }

        viewModel.onEvent(DetailEvent.Init(fid = foodProviderId.toLong()))

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun fillInMenus(menus: List<Menu>) {

    }

    fun setMarginForIconButton() {
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