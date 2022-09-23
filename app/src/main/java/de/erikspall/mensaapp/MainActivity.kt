package de.erikspall.mensaapp

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.databinding.ActivityMainBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupBottomNavMenu(navController)
    }

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNav = findViewById<BottomNavigationView>(R.id.nav_view)
        bottomNav?.setupWithNavController(navController)

            navController.addOnDestinationChangedListener { _, destination, _ ->
                if(destination.id == R.id.cafe_list_dest || destination.id == R.id.mensa_list_dest || destination.id == R.id.settings_dest) {
                    //val height = bottomNav.translationY
                    ObjectAnimator.ofFloat(bottomNav, "translationY", 0f).apply {
                        duration = 300
                        doOnStart {
                            bottomNav.visibility = View.VISIBLE
                        }
                        start()
                    }
                } else {
                    ObjectAnimator.ofFloat(bottomNav, "translationY", bottomNav.height.toFloat()-50f).apply {
                        duration = 300
                        doOnEnd {
                            bottomNav.visibility = View.GONE
                        }
                        start()
                    }
                }
            }
    }

        //setContentView(R.layout.activity_main)

}