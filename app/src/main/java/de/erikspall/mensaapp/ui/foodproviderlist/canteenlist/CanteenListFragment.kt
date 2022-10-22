package de.erikspall.mensaapp.ui.foodproviderlist.canteenlist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.databinding.FragmentCanteenListBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.utils.Extensions.observeOnce
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.foodproviderlist.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.foodproviderlist.cafelist.CafeListFragmentDirections
import de.erikspall.mensaapp.ui.foodproviderlist.cafelist.CafeListViewModel
import de.erikspall.mensaapp.ui.foodproviderlist.event.FoodProviderListEvent
import de.erikspall.mensaapp.ui.state.UiState


@AndroidEntryPoint
class CanteenListFragment : Fragment() {
    private var _binding: FragmentCanteenListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CanteenListViewModel by viewModels()

    lateinit var timeTickReceiver: BroadcastReceiver

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
        _binding = FragmentCanteenListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewCanteen.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    MaterialSizes.BOTTOM_NAV_HEIGHT
        )

        binding.swipeRefresh.setProgressViewOffset(
            false,
            binding.swipeRefresh.progressViewStartOffset - 8,
            binding.swipeRefresh.progressViewEndOffset - 24
        )

        val adapter = FoodProviderCardAdapter { foodProviderId, category ->
            val directions = if (category == Category.CAFETERIA)
                CafeListFragmentDirections.actionOpenDetails(foodProviderId, category.ordinal)
            else
                CanteenListFragmentDirections.actionOpenDetails(foodProviderId, category.ordinal)

            findNavController().navigate(directions)
        }

        binding.recyclerViewCanteen.adapter = adapter

        setupListeners()
        setupObservers()

        viewModel.onEvent(FoodProviderListEvent.Init)

        return root
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.onEvent(FoodProviderListEvent.GetLatest)
        }
    }

    private fun setupObservers() {
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

        //viewModel.state.receivedData.observe(viewLifecycleOwner) {
        viewModel.state.foodProviders.observe(viewLifecycleOwner) { canteens ->

            binding.swipeRefresh.isRefreshing = false


            Log.d(
                "$TAG:livedata-canteens",
                "New livedata received! ${canteens.size} items"
            )
            if (canteens.isNotEmpty()) {
                viewModel.onEvent(FoodProviderListEvent.SetUiState(UiState.NORMAL))
                canteens.let {
                    (binding.recyclerViewCanteen.adapter as FoodProviderCardAdapter).submitList(
                        it
                    )
                }
            } else {
                showMessage(
                    R.raw.error,
                    "Irgendetwas ist schiefgelaufen :(\nBesteht eine Internetverbindung?"
                )
            }

        }

        viewModel.state.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            if (!isRefreshing)
                binding.swipeRefresh.isRefreshing = false
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
            binding.canteenListAppbarLayout.visibility = INVISIBLE
            binding.canteenListNestedScroll.visibility = INVISIBLE
        } else {
            binding.lottieContainer.visibility = GONE
            binding.canteenListAppbarLayout.visibility = VISIBLE
            binding.canteenListNestedScroll.visibility = VISIBLE
        }
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        // TODO: does not update when resumed after fragment paused and minute passes
        timeTickReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.d("$TAG:broadcast-receiver", "Tick!")
                // Update content of recyclerview if present
                viewModel.onEvent(FoodProviderListEvent.UpdateOpeningHours)
            }
        }
        requireActivity().registerReceiver(timeTickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onPause() {
        super.onPause()

        requireActivity().unregisterReceiver(timeTickReceiver)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "CanteenListFragment"
    }
}