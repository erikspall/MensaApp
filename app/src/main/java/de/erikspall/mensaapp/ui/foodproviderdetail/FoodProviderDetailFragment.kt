package de.erikspall.mensaapp.ui.foodproviderdetail

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.transition.TransitionInflater
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import com.google.android.material.transition.MaterialFadeThrough
import de.erikspall.mensaapp.R
import de.erikspall.mensaapp.data.source.local.DummyDataSource
import de.erikspall.mensaapp.databinding.FragmentFoodProviderDetailBinding
import de.erikspall.mensaapp.domain.const.MaterialSizes
import de.erikspall.mensaapp.domain.model.interfaces.FoodProvider
import de.erikspall.mensaapp.domain.utils.Extensions.getDynamicColorIfAvailable
import de.erikspall.mensaapp.domain.utils.Extensions.pushContentUpBy
import de.erikspall.mensaapp.domain.utils.HeightExtractor
import de.erikspall.mensaapp.ui.adapter.MenuAdapter
import de.erikspall.mensaapp.ui.canteenlist.CanteenListFragmentArgs


class FoodProviderDetailFragment : Fragment() {
    private var _binding: FragmentFoodProviderDetailBinding? = null

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
            activity?.onBackPressed()
        }

        val safeArgs: CanteenListFragmentArgs by navArgs()
        val foodProviderId = safeArgs.canteenId
        binding.textFoodProviderName.text = DummyDataSource.canteens[foodProviderId].getName()
        binding.imageFoodProvider.setImageResource(DummyDataSource.canteens[foodProviderId].getImageResourceId())



        binding.recyclerViewMenus.pushContentUpBy(
            HeightExtractor.getNavigationBarHeight(requireContext()) +
                    MaterialSizes.BOTTOM_NAV_HEIGHT
        )


        fillInMenus(DummyDataSource.canteens[foodProviderId])



        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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