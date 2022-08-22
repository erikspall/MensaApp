package de.erikspall.mensaapp.ui.cafelist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import de.erikspall.mensaapp.databinding.FragmentCafelistBinding
import de.erikspall.mensaapp.databinding.FragmentMensalibBinding

class CafeListFragment : Fragment() {
    private var _binding: FragmentCafelistBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val cafeListViewModel =
            ViewModelProvider(this)[CafeListViewModel::class.java]

        _binding = FragmentCafelistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}