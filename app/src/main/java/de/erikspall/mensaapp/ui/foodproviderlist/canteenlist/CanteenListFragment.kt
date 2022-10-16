package de.erikspall.mensaapp.ui.foodproviderlist.canteenlist

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
import com.google.android.material.transition.MaterialElevationScale
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.databinding.FragmentCanteenListBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.enums.Category
import de.erikspall.mensaapp.domain.enums.Location
import de.erikspall.mensaapp.domain.model.FoodProvider
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.domain.utils.queries.QueryUtils
import de.erikspall.mensaapp.ui.foodproviderlist.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.foodproviderlist.event.FoodProviderListEvent
import de.erikspall.mensaapp.ui.state.UiState
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CanteenListFragment : Fragment(), FoodProviderCardAdapter.OnFoodProviderSelectedListener {
    private var _binding: FragmentCanteenListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CanteenListViewModel by viewModels()

    lateinit var firestore: FirebaseFirestore
    private var query: Query? = null
    private var adapter: FoodProviderCardAdapter? = null

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

        firestore = Firebase.firestore

        query = firestore.collection("foodProviders")
            .whereEqualTo(FoodProvider.FIELD_LOCATION, requireContext().getString(Location.WUERZBURG.getValue()))
            .whereEqualTo(FoodProvider.FIELD_CATEGORY, Category.CANTEEN.getValue())


        query?.let {
            adapter = object : FoodProviderCardAdapter(it, this@CanteenListFragment) {
                override fun onDataChanged() {
                    makeLottieVisible(false)
                    Log.d("$TAG:query", "DataChanged")
                    super.onDataChanged()
                }

                override fun onError(e: FirebaseFirestoreException) {
                    // TODO
                    super.onError(e)
                }
            }
            binding.recyclerViewCanteen.adapter = adapter
        }

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

        setupListeners()
        setupObservers()

        viewModel.onEvent(FoodProviderListEvent.CheckIfNewLocationSet)

        return root
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
               // viewModel.onEvent(FoodProviderListEvent.GetLatestInfo)
            }
        }
    }

    private fun setupObservers() {
        viewModel.state.isRefreshing.observe(viewLifecycleOwner) { isRefreshing ->
            if (!isRefreshing)
                binding.swipeRefresh.isRefreshing = false
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

        // Start listening for Firestore updates
        adapter?.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter?.stopListening()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onFoodProviderSelected(foodProvider: DocumentSnapshot) {
        Log.e("$TAG:onFoodProviderSelected", "Not yet implemented")
    }

    companion object {
        const val TAG = "CanteenListFragment"
    }
}