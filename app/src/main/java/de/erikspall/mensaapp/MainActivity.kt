package de.erikspall.mensaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import de.erikspall.mensaapp.ui.MensaApp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    // private lateinit var binding: ActivityMainBinding
    // private lateinit var navController: NavController

   // private val viewModel: MensaAppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This will lay out our app behind the system bars
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            MensaApp()
        }

        /*WindowCompat.setDecorFitsSystemWindows(window, false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        setupBottomNavMenu(navController)*/
    }

    /* private fun setupBottomNavMenu(navController: NavController) {
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
     }*/

    //setContentView(R.layout.activity_main)

}