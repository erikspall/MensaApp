package de.erikspall.mensaapp.ui.mensalist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import de.erikspall.mensaapp.databinding.FragmentFoodProviderCanteenLibBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.utils.Conversion
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.adapter.FoodProviderCardAdapter

class MensaListFragment : Fragment() {
    private var _binding: FragmentFoodProviderCanteenLibBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mensaListViewModel =
            ViewModelProvider(this)[MensaListViewModel::class.java]

        _binding = FragmentFoodProviderCanteenLibBinding.inflate(inflater, container, false)
        val root: View = binding.root

        pushRecyclerContentUp(
            binding.recyclerViewCanteen,
            HeightExtractor.getNavigationBarHeight(requireContext())
                + MaterialSizes.BOTTOM_NAV_HEIGHT
        )

        binding.recyclerViewCanteen.adapter = FoodProviderCardAdapter(
            requireContext(),
        )

        binding.recyclerViewCanteen.setHasFixedSize(true)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pushRecyclerContentUp(recyclerView: RecyclerView, dp: Int = 50) {
        binding.recyclerViewCanteen.setPadding(8, 0, 8, Conversion.dpToPx(dp))
    }
}