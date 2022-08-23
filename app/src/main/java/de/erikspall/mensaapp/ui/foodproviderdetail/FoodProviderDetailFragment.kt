package de.erikspall.mensaapp.ui.foodproviderdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import de.erikspall.mensaapp.data.source.local.DummyDataSource
import de.erikspall.mensaapp.databinding.FragmentFoodProviderDetailBinding
import de.erikspall.mensaapp.domain.model.interfaces.FoodProvider
import de.erikspall.mensaapp.ui.adapter.FoodProviderCardAdapter
import de.erikspall.mensaapp.ui.adapter.MenuAdapter
import de.erikspall.mensaapp.ui.canteenlist.CanteenListFragmentArgs

class FoodProviderDetailFragment : Fragment() {
    private var _binding: FragmentFoodProviderDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentFoodProviderDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val safeArgs: CanteenListFragmentArgs by navArgs()
        val foodProviderId = safeArgs.canteenId
        binding.textFoodProviderName.text = DummyDataSource.canteens[foodProviderId].getName()
        binding.imageFoodProvider.setImageResource(DummyDataSource.canteens[foodProviderId].getImageResourceId())

        fillInMenus(DummyDataSource.canteens[foodProviderId])

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /* BAD PRACTICE */
    fun fillInMenus(foodProvider: FoodProvider) {
        binding.recyclerViewMenus.adapter = MenuAdapter(
            requireContext(),
            foodProvider.getMenus()
        )
    }
}