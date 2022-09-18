package de.erikspall.mensaapp.ui.canteenlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.databinding.FragmentFoodProviderCanteenLibBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.usecases.foodprovider.GetOpeningHoursAsString
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.canteenlist.viewmodel.CanteenListViewModel

@AndroidEntryPoint
class CanteenListFragment : Fragment() {
    private var _binding: FragmentFoodProviderCanteenLibBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val viewModel: CanteenListViewModel by viewModels()

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
        _binding = FragmentFoodProviderCanteenLibBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val adapter = FoodProviderCardAdapter(
            requireContext(),
            findNavController()
        )

        binding.recyclerViewCanteen.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    MaterialSizes.BOTTOM_NAV_HEIGHT
        )

        binding.recyclerViewCanteen.adapter = adapter

        viewModel.canteens.observe(viewLifecycleOwner) { canteens ->
            canteens.let { adapter.submitList(it) }
        }

        binding.recyclerViewCanteen.setHasFixedSize(true)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}