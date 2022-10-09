package de.erikspall.mensaapp.ui.canteenlist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RawRes
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.databinding.FragmentCanteenListBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.canteenlist.viewmodel.CanteenListViewModel
import de.erikspall.mensaapp.ui.canteenlist.viewmodel.event.CanteenListEvent
import de.erikspall.mensaapp.ui.state.UiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CanteenListFragment : Fragment() {
    private var _binding: FragmentCanteenListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: CanteenListViewModel by viewModels()

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
        _binding = FragmentCanteenListBinding.inflate(inflater, container, false)
        val root: View = binding.root



        binding.recyclerViewCanteen.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    MaterialSizes.BOTTOM_NAV_HEIGHT
        )

        val adapter = FoodProviderCardAdapter(
            requireContext(),
            findNavController()
        )

        binding.recyclerViewCanteen.adapter = adapter

        binding.swipeRefresh.setProgressViewOffset(
            false,
            binding.swipeRefresh.progressViewStartOffset - 8,
            binding.swipeRefresh.progressViewEndOffset - 24
        )

        setupListeners()
        setupObservers()

        viewModel.onEvent(CanteenListEvent.CheckIfNewLocationSet)

        return root
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.onEvent(CanteenListEvent.GetLatestInfo)
            }
        }
    }

    private fun setupObservers() {
        viewModel.state.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            if (!isRefreshing)
                binding.swipeRefresh.isRefreshing = false
        }

        viewModel.canteens.observe(viewLifecycleOwner) { canteens ->
            if (canteens.isEmpty()) {
                viewModel.onEvent(CanteenListEvent.GetLatestInfo)
                viewModel.onEvent(CanteenListEvent.NewUiState(UiState.LOADING))
            } else {
                viewModel.onEvent(CanteenListEvent.NewUiState(UiState.NORMAL))

            }
            Log.d("CanteenListFragment", "Canteens: $canteens")
            canteens.let {
                (binding.recyclerViewCanteen.adapter as FoodProviderCardAdapter).submitList(it.filter { foodProvider ->
                    foodProvider.location.name == requireContext().getString(viewModel.state.showingLocation.getValue())
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
        if (binding.recyclerViewCanteen.adapter?.itemCount == 0) {
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
            binding.lottieContainer.visibility = VISIBLE
            binding.libraryAppbarLayout.visibility = INVISIBLE
            binding.libraryNestedScroll.visibility = INVISIBLE
        } else {
            binding.lottieContainer.visibility = GONE
            binding.libraryAppbarLayout.visibility = VISIBLE
            binding.libraryNestedScroll.visibility = VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}