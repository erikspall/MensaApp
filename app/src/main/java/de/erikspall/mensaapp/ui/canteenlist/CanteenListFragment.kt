package de.erikspall.mensaapp.ui.canteenlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.erikspall.mensaapp.databinding.FragmentFoodProviderCanteenLibBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.utils.Conversion
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.adapter.FoodProviderCardAdapter

class CanteenListFragment : Fragment() {
    private var _binding: FragmentFoodProviderCanteenLibBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val canteenListViewModel =
            ViewModelProvider(this)[CanteenListViewModel::class.java]

        _binding = FragmentFoodProviderCanteenLibBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.recyclerViewCanteen.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    MaterialSizes.BOTTOM_NAV_HEIGHT
        )

        binding.recyclerViewCanteen.adapter = FoodProviderCardAdapter(
            requireContext(),
            findNavController()
        )

        binding.recyclerViewCanteen.setHasFixedSize(true)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}