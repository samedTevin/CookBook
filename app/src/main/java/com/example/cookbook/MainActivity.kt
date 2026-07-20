package com.example.cookbook

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.cookbook.data.Meal
import com.example.cookbook.data.Meals
import com.example.cookbook.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {


        // Splash Screen
        installSplashScreen().setOnExitAnimationListener { splashScreenView ->

            val iconView = splashScreenView.iconView
            iconView.animate().rotation(360f).setDuration(600).withEndAction {
                iconView.animate()
                    .scaleX(10f)
                    .scaleY(10f)
                    .alpha(0f)
                    .setDuration(600)
                    .withEndAction{
                        splashScreenView.remove()
                    }
                    .start()
            }.start()
        }


        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        // Sets the destination based on sharedPreferences
        if(isFinishedOnBoarding()){
            navGraph.setStartDestination(R.id.welcomeFragment)
        }
        else{
            navGraph.setStartDestination(R.id.viewPagerFragment)
        }

        navController.graph = navGraph

        val bottomNav = binding.bottomNav

        bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener{_, destination, _ ->
            if(destination.id == R.id.viewPagerFragment){
                bottomNav.visibility = View.GONE
            }
            else if(destination.id == R.id.welcomeFragment){
                bottomNav.visibility = View.GONE
            }
            else if(destination.id == R.id.loginFragment){
                bottomNav.visibility = View.GONE
            }
            else if(destination.id == R.id.registerFragment){
                bottomNav.visibility = View.GONE
            }
            else{
                bottomNav.visibility = View.VISIBLE
            }

        }


    }


    // Checks has the user already entered the app
    private fun isFinishedOnBoarding(): Boolean{

        val sharedPreferences = getSharedPreferences("onboarding",Context.MODE_PRIVATE)
        val isFinished = sharedPreferences.getBoolean("Finished", false)

        return isFinished
    }
}