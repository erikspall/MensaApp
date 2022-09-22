package de.erikspall.mensaapp.ui.settings.allergenic

import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import de.erikspall.mensaapp.databinding.FragmentSettingsAllergenicBinding


class AllergenicFragment : Fragment() {
    private var _binding: FragmentSettingsAllergenicBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsAllergenicBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setupListeners()

        return root
    }

    private fun setupListeners() {
        binding.settingsAllergenicSwitch.setOnCheckedChangeListener { _, isChecked ->
            Log.d("Switch", "I was checked!")
            val transitionDrawable =
                (binding.settingsAllergenicCardLayout.background as TransitionDrawable)
            if (isChecked)
                transitionDrawable.startTransition(300)
            else
                transitionDrawable.reverseTransition(300)
            /*val colorAnimator = ValueAnimator.ofArgb(colorFrom, colorTo)
            colorAnimator.duration = 500
            colorAnimator.addUpdateListener {
                binding.settingsAllergenicCard.setCardBackgroundColor(it.animatedValue as Int)
            }
            colorAnimator.start()*/
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}