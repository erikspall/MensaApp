package de.erikspall.mensaapp.ui.foodproviderlist.cafelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.databinding.FragmentCafeteriaListBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.foodproviderlist.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.foodproviderlist.event.FoodProviderListEvent
import de.erikspall.mensaapp.ui.state.UiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CafeListFragment : Fragment() {
    private var _binding: FragmentCafeteriaListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: CafeListViewModel by viewModels()

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

        //binding.recyclerViewCanteen.setHasFixedSize(true)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCafeteriaListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewCafe.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    MaterialSizes.BOTTOM_NAV_HEIGHT
        )

        val adapter = FoodProviderCardAdapter(
            requireContext(),
            findNavController()
        )

        binding.recyclerViewCafe.adapter = adapter

        binding.swipeRefresh.setProgressViewOffset(
            false,
            binding.swipeRefresh.progressViewStartOffset - 8,
            binding.swipeRefresh.progressViewEndOffset - 24
        )

        setupListeners()
        setupObservers()

        viewModel.onEvent(FoodProviderListEvent.CheckIfNewLocationSet)

        return root
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.onEvent(FoodProviderListEvent.GetLatestInfo)
            }
        }
    }

    private fun setupObservers() {
        viewModel.state.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            if (!isRefreshing)
                binding.swipeRefresh.isRefreshing = false
        }

        viewModel.cafeterias.observe(viewLifecycleOwner) { cafes ->
            if (cafes.isEmpty()) {
                viewModel.onEvent(FoodProviderListEvent.GetLatestInfo)
                viewModel.onEvent(FoodProviderListEvent.NewUiState(UiState.LOADING))
            } else {
                viewModel.onEvent(FoodProviderListEvent.NewUiState(UiState.NORMAL))

            }
            Log.d("CafeListFragment", "Cafes: $cafes")
            cafes.let {
                (binding.recyclerViewCafe.adapter as FoodProviderCardAdapter).submitList(it.filter { foodProvider ->
                    foodProvider.location.name == requireContext().getString(viewModel.state.location.getValue())
                })
            }
        }

        viewModel.state.uiState.observe(viewLifecycleOwner) { uiState ->
            when (uiState) {
                UiState.NORMAL -> {
                    makeLottieVisible(false)
                }
                UiState.ERROR -> {
                    showMessage(
                        R.raw.error,
                        "Ohje! Da ist etwas schiefgelaufen :(" +
                                "\nVersuche es bitte später erneut!"
                    )
                }
                UiState.LOADING -> {
                    showMessage(
                        R.raw.man_serving_catering_food,
                        "Sucht nach Mensen ..."
                    )
                }
                UiState.NO_CONNECTION -> {
                    showMessage(
                        R.raw.no_connection,
                        "Der Server scheint nicht zu antworten :(\n" +
                                "Versuche es bitte später erneut!",
                        0.5f
                    )
                }
                UiState.NO_INTERNET -> {
                    showMessage(
                        R.raw.no_internet,
                        "Wir können den Server nicht erreichen :(\n" +
                                "Bitte überprüfe deine Internetverbindung und versuche es erneut!"
                    )
                }
                else -> {

                }
            }
        }
    }

    private fun showMessage(@RawRes animation: Int, errorMsg: String, animationSpeed: Float = 1f) {
        if (binding.recyclerViewCafe.adapter?.itemCount == 0) {
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
            binding.lottieContainer.visibility = View.VISIBLE
            binding.cafeListAppbarLayout.visibility = View.INVISIBLE
            binding.cafeListNestedScroll.visibility = View.INVISIBLE
        } else {
            binding.lottieContainer.visibility = View.GONE
            binding.cafeListAppbarLayout.visibility = View.VISIBLE
            binding.cafeListNestedScroll.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}