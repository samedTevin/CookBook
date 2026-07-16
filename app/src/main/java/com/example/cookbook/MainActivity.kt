package com.example.cookbook

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
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

        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)

        // Sets the destination based on sharedPreferences
        if(isFinishedOnBoarding()){
            navGraph.setStartDestination(R.id.homeFragment)
        }
        else{
            navGraph.setStartDestination(R.id.viewPagerFragment)
        }

        navController.graph = navGraph

    }


    // Checks has the user already entered the app
    private fun isFinishedOnBoarding(): Boolean{

        val sharedPreferences = getSharedPreferences("onboarding",Context.MODE_PRIVATE)
        val isFinished = sharedPreferences.getBoolean("Finished", false)

        return isFinished
    }
}